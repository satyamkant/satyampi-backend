package uk.satyampi.SecurityMs.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import uk.satyampi.SecurityMs.dto.UserDetails;
import uk.satyampi.SecurityMs.dto.UserDto;
import uk.satyampi.SecurityMs.exception.SatyamPiLogicalException;
import uk.satyampi.SecurityMs.service.JwtService;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtServiceImpl implements JwtService {

    private final String  SECRET_KEY;
    private final long EXPIRATION_TIME;
    private final AuthenticationManager authenticationManager;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    @Autowired
    public JwtServiceImpl(@Value("${SECRET_KEY}") String secretKey,
                          @Value("${EXPIRATION_TIME}")long jwtExpirationMs,
                          AuthenticationManager authenticationManager) {
        this.SECRET_KEY = secretKey;
        this.EXPIRATION_TIME = jwtExpirationMs;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDto verifyUser(UserDto userDto) throws SatyamPiLogicalException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPasswordHash()));
            if (authentication.isAuthenticated()) {
                userDto.setName(((UserDetails)authentication.getPrincipal()).getUserName());
                userDto.setJwtToken(generateTokenFromUsername(userDto));
                return userDto;
            }
        }
        catch (AuthenticationException e) {
            throw new SatyamPiLogicalException(e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public String getJwtFromHeader(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();  // Return the JWT token from the cookie
                }
            }
        }
        return null;
    }

    @Override
    public String generateTokenFromUsername(UserDto userDto) {
        return Jwts.builder()
                .subject(userDto.getEmail())
                .claim("userName", userDto.getName())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + TimeUnit.HOURS.toMillis(EXPIRATION_TIME)))
                .signWith(key())
                .compact();
    }

    @Override
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public String getClaimFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload().get("userName", String.class);
    }


    @Override
    public boolean validateJwtToken(String authToken) throws SatyamPiLogicalException {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException | IllegalArgumentException e) {
            throw new SatyamPiLogicalException("Invalid JWT token",e);
        } catch (ExpiredJwtException e) {
            throw new SatyamPiLogicalException("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            throw new SatyamPiLogicalException("Unsupported JWT token", e);
        }
    }
}
