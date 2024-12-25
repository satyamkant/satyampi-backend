package uk.satyampi.UserMs.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uk.satyampi.UserMs.dto.UserDto;
import uk.satyampi.UserMs.entity.User;
import uk.satyampi.UserMs.exception.SatyamPiLogicalException;
import uk.satyampi.UserMs.repository.UserRepository;
import uk.satyampi.UserMs.service.UserService;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto registerUser(UserDto userDto) throws SatyamPiLogicalException {
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new SatyamPiLogicalException("User Already Exists");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setBio(userDto.getBio());
        user.setPasswordHash(bCryptPasswordEncoder.encode(userDto.getPasswordHash()));
        user.setProfilePicturePath(userDto.getProfilePicturePath());
        userRepository.save(user);
        userDto.setPasswordHash("");
        return userDto;
    }

    @Override
    public UserDto getUserDto(UserDto userDto) throws SatyamPiLogicalException {
        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        if(userOptional.isEmpty()) {
            throw new SatyamPiLogicalException("User Not Found");
        }

        User user = userOptional.get();

        UserDto response = new UserDto();
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setPasswordHash(user.getPasswordHash());
        response.setName(user.getName());
        response.setBio(user.getBio());
        response.setProfilePicturePath(user.getProfilePicturePath());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setIsActive(user.getIsActive());
        response.setAccountLocked(user.getAccountLocked());

        return response;
    }

}
