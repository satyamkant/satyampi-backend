package uk.satyampi.UserMs.service;

import uk.satyampi.UserMs.dto.UserDto;
import uk.satyampi.UserMs.exception.SatyamPiLogicalException;

public interface UserService {
    UserDto registerUser(UserDto userDto) throws SatyamPiLogicalException;
    UserDto getUserDto(UserDto userDto) throws SatyamPiLogicalException;
}
