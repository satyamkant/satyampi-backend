package uk.satyampi.SecurityMs.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import uk.satyampi.SecurityMs.dto.*;
import uk.satyampi.SecurityMs.exception.SatyamPiLogicalException;
import uk.satyampi.SecurityMs.service.JwtService;

import java.io.Console;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/security")
public class JwtController {

    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private final String USER_DB_URL_REGISTER_USER;
    private final String BLOG_DB_URL_SAVE_BLOG;
    private final String BLOG_DB_URL_GET_BLOG_BY_TITLE;
    private final String BLOG_DB_BASE_URL;
    private final long EXPIRATION_TIME;
    private final String DOMAIN;

    private void generateCookie(HttpServletResponse response, String jwtToken,int maxAge) {
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(true);  // Make sure it's HttpOnly
        cookie.setSecure(true);    // Make sure it's Secure (use in production)
        cookie.setPath("/");       // The path where the cookie is valid
        cookie.setMaxAge(maxAge); // Set cookie expiration time (in seconds)
        cookie.setDomain(DOMAIN);
        response.addCookie(cookie); // Add cookie to response
    }

    @Autowired
    public JwtController(RestTemplate restTemplate, JwtService jwtService,
                         @Value("${USER_DB_URL_REGISTER_USER}") String userDbUrlRegisterUser,
                         @Value("${BLOG_DB_URL_SAVE_BLOG}") String blogDbUrlSaveBlog,
                         @Value("${BLOG_DB_URL_GET_BLOG_BY_TITLE}") String blogDbUrlGetBlogByTitle,
                         @Value("${BLOG_DB_BASE_URL}") String blogDbBaseUrl,
                         @Value("${EXPIRATION_TIME}") long expirationTime,
                         @Value("${DOMAIN}") String domain) {
        this.restTemplate = restTemplate;
        this.jwtService = jwtService;
        USER_DB_URL_REGISTER_USER = userDbUrlRegisterUser;
        BLOG_DB_URL_SAVE_BLOG = blogDbUrlSaveBlog;
        BLOG_DB_URL_GET_BLOG_BY_TITLE = blogDbUrlGetBlogByTitle;
        BLOG_DB_BASE_URL = blogDbBaseUrl;
        EXPIRATION_TIME = expirationTime;
        DOMAIN = domain;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto,
                                   HttpServletResponse response) throws SatyamPiLogicalException {

        // Create a cookie with the JWT token
        String jwtToken = jwtService.verifyUser(userDto).getJwtToken();
        generateCookie(response, jwtToken,(int)TimeUnit.HOURS.toSeconds(EXPIRATION_TIME));


        userDto.setPasswordHash(null);
        userDto.setJwtToken(null);

        userDto.setName(jwtService.getClaimFromJwtToken(jwtToken));
        userDto.setUserId(jwtService.getUserIdFromJwtToken(jwtToken));

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userDto);
        responseDTO.setMessage("Successfully logged in");
        responseDTO.setStatus(HttpStatus.OK.toString());

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Create a cookie with the JWT token
        generateCookie(response, "",0);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Successfully Logged out");
        responseDTO.setStatus(HttpStatus.OK.toString());

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserDto userDto) throws SatyamPiLogicalException {

        // Set HTTP headers (if needed, e.g., Authorization)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        try {
            // Make the REST call

            ResponseDTO responseDTO = new ResponseDTO();

            restTemplate.exchange(
                    USER_DB_URL_REGISTER_USER,
                    HttpMethod.POST,
                    requestEntity,
                    UserDto.class
            );

            userDto.setPasswordHash(null);

            responseDTO.setData(userDto);
            responseDTO.setMessage("User registered successfully");
            responseDTO.setStatus(HttpStatus.OK.toString());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            throw new SatyamPiLogicalException("Http exception", e, responseBody, e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new SatyamPiLogicalException("Server not available",e);
        } catch (Exception e) {
            throw new SatyamPiLogicalException("Internal server error",e);
        }
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<?> isAuthenticated(HttpServletRequest request) {
        String jwt = jwtService.getJwtFromHeader(request);
        String userName = jwtService.getUserNameFromJwtToken(jwt);




        UserDto userDto = new UserDto();
        userDto.setEmail(userName);
        userDto.setName(jwtService.getClaimFromJwtToken(jwt));

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Authenticated user");
        responseDTO.setData(userDto);

        return new  ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/blog/saveBlog")
    public ResponseEntity<?> saveBlog(@Valid @RequestBody BlogDataDTO blogDataDTO) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<BlogDataDTO> requestEntity = new HttpEntity<>(blogDataDTO, headers);

        try {
            ResponseDTO ResponseDto = restTemplate.exchange(
                    BLOG_DB_URL_SAVE_BLOG,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseDTO.class
            ).getBody();
            return new ResponseEntity<>(ResponseDto, HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            throw new SatyamPiLogicalException("Http exception", e, responseBody, e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new SatyamPiLogicalException("Server not available",e);
        } catch (Exception e) {
            throw new SatyamPiLogicalException("Internal server error",e);
        }
    }

    @GetMapping("/blog/getBlogByTitle/{blogTitle}")
    public ResponseEntity<?> getBlogByTitle(@PathVariable String blogTitle) throws Exception {
        String url = BLOG_DB_URL_GET_BLOG_BY_TITLE + "?title=" + blogTitle;

        try {
            ResponseDTO ResponseDto = restTemplate.getForObject(url, ResponseDTO.class);
            return new ResponseEntity<>(ResponseDto, HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new SatyamPiLogicalException("Http exception",e);
        } catch (ResourceAccessException e) {
            throw new SatyamPiLogicalException("Server not available",e);
        } catch (Exception e) {
            throw new SatyamPiLogicalException("Internal server error",e);
        }

    }

    @GetMapping("/blog/getBlogsByType/{blogType}")
    public ResponseEntity<?> getBlogsByType(@PathVariable String blogType) throws Exception {
        String url = BLOG_DB_BASE_URL + "/blogs/type" + "?type=" + blogType;

        try {
            ResponseDTO ResponseDto = restTemplate.getForObject(url, ResponseDTO.class);
            return new ResponseEntity<>(ResponseDto, HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new SatyamPiLogicalException("Http exception",e);
        } catch (ResourceAccessException e) {
            throw new SatyamPiLogicalException("Server not available",e);
        } catch (Exception e) {
            throw new SatyamPiLogicalException("Internal server error",e);
        }


    }

    @GetMapping("/blog/getAllBlogs")
    public ResponseEntity<?> getAllBlogs() throws Exception {
        String url = BLOG_DB_BASE_URL + "/blogs/allblogs";

        try {
            ResponseDTO ResponseDto = restTemplate.getForObject(url, ResponseDTO.class);
            return new ResponseEntity<>(ResponseDto, HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new SatyamPiLogicalException("Http exception",e);
        } catch (ResourceAccessException e) {
            throw new SatyamPiLogicalException("Server not available",e);
        } catch (Exception e) {
            throw new SatyamPiLogicalException("Internal server error",e);
        }
    }

    @PostMapping("/blog/updateblog")
    public ResponseEntity<?> updateBlog(@Valid @RequestBody BlogDataDTO blogDataDTO) throws Exception {
        String url = BLOG_DB_BASE_URL + "/blogs/updateblog";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<BlogDataDTO> requestEntity = new HttpEntity<>(blogDataDTO, headers);

        try {
            ResponseDTO ResponseDto = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseDTO.class
            ).getBody();
            return new ResponseEntity<>(ResponseDto, HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new SatyamPiLogicalException("Http exception",e);
        } catch (ResourceAccessException e) {
            throw new SatyamPiLogicalException("Server not available",e);
        } catch (Exception e) {
            throw new SatyamPiLogicalException("Internal server error",e);
        }
    }

}
