package uk.satyampi.SecurityMs.service;

import jakarta.servlet.http.HttpServletRequest;
import uk.satyampi.SecurityMs.dto.UserDto;
import uk.satyampi.SecurityMs.exception.SatyamPiLogicalException;


public interface JwtService {
    UserDto verifyUser(UserDto userDto) throws SatyamPiLogicalException;

    String getJwtFromHeader(HttpServletRequest request);

    String generateTokenFromUsername(UserDto userDto);

    String getUserNameFromJwtToken(String token);

    boolean validateJwtToken(String authToken) throws SatyamPiLogicalException;

    String getClaimFromJwtToken(String token);
}
