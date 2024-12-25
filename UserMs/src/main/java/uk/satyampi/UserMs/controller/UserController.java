package uk.satyampi.UserMs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.satyampi.UserMs.dto.UserDto;
import uk.satyampi.UserMs.exception.SatyamPiLogicalException;
import uk.satyampi.UserMs.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) throws SatyamPiLogicalException {
        return new ResponseEntity<>(userService.registerUser(userDto),HttpStatus.OK);
    }

    @PostMapping("/getuser")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) throws SatyamPiLogicalException {
        return new ResponseEntity<>(userService.getUserDto(userDto), HttpStatus.OK);
    }

}
