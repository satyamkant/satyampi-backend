package uk.satyampi.SecurityMs.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import uk.satyampi.SecurityMs.dto.ResponseDto;
import uk.satyampi.SecurityMs.dto.UserDto;
import uk.satyampi.SecurityMs.exception.SatyamPiLogicalException;
import uk.satyampi.SecurityMs.service.JwtService;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/security")
public class JwtController {

    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private final String USER_DB_URL_REGISTER_USER;
    private final long EXPIRATION_TIME;
    private final String DOMAIN;

    @Autowired
    public JwtController(RestTemplate restTemplate, JwtService jwtService,
                         @Value("${USER_DB_URL_REGISTER_USER}") String userDbUrlRegisterUser,
                         @Value("${EXPIRATION_TIME}") long expirationTime,
                         @Value("${DOMAIN}") String domain) {
        this.restTemplate = restTemplate;
        this.jwtService = jwtService;
        USER_DB_URL_REGISTER_USER = userDbUrlRegisterUser;
        EXPIRATION_TIME = expirationTime;
        DOMAIN = domain;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto,
                                   HttpServletResponse response) throws SatyamPiLogicalException {

        // Create a cookie with the JWT token
        String jwtToken = jwtService.verifyUser(userDto).getJwtToken();
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(true);  // Make sure it's HttpOnly
        cookie.setSecure(true);    // Make sure it's Secure (use in production)
        cookie.setPath("/");       // The path where the cookie is valid
        cookie.setMaxAge((int)TimeUnit.HOURS.toSeconds(EXPIRATION_TIME)); // Set cookie expiration time (in seconds)
        cookie.setDomain(DOMAIN);

        response.addCookie(cookie); // Add cookie to response


        ResponseDto responseDto = new ResponseDto();
        userDto.setPasswordHash(null);
        userDto.setJwtToken(null);
        userDto.setName(jwtService.getClaimFromJwtToken(jwtToken));

        responseDto.setUserDto(userDto);
        responseDto.setMessage("Successfully logged in");

        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Create a cookie with the JWT token
        String jwtToken = "";
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(true);  // Make sure it's HttpOnly
        cookie.setSecure(true);    // Make sure it's Secure (use in production)
        cookie.setPath("/");       // The path where the cookie is valid
        cookie.setMaxAge(0); // Set cookie expiration time (in seconds)
        cookie.setDomain(DOMAIN);
        response.addCookie(cookie); // Add cookie to response
        ResponseDto responseDto = new ResponseDto();

        responseDto.setMessage("Successfully Logged out");

        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserDto userDto) throws Exception {

        // Set HTTP headers (if needed, e.g., Authorization)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        try {
            // Make the REST call

            ResponseDto responseDto = new ResponseDto();

            restTemplate.exchange(
                    USER_DB_URL_REGISTER_USER,
                    HttpMethod.POST,
                    requestEntity,
                    UserDto.class
            );

            userDto.setPasswordHash(null);

            responseDto.setUserDto(userDto);
            responseDto.setMessage("User registered successfully");
            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Specific exception handling for HTTP errors
            throw new SatyamPiLogicalException("Http exception",e);
        } catch (ResourceAccessException e) {
            // Handle network or connection issues (e.g., service down)
            throw new SatyamPiLogicalException("Server not available",e);
        } catch (Exception e) {
            // General exception handling
            throw new SatyamPiLogicalException("Internal server error",e);
        }
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<?> isAuthenticated(HttpServletRequest request) {
        String jwt = jwtService.getJwtFromHeader(request);
        String userName = jwtService.getUserNameFromJwtToken(jwt);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("Authenticated user");

        UserDto userDto = new UserDto();
        userDto.setEmail(userName);
        userDto.setName(jwtService.getClaimFromJwtToken(jwt));

        responseDto.setUserDto(userDto);

        return new  ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
