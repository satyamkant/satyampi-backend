package uk.satyampi.SecurityMs.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import uk.satyampi.SecurityMs.enums.UserRole;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class UserDto implements Serializable {
    private Long userId;

    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]*(\\s[A-Z][a-z]*)*$", message = "Invalid Name")
    private String name;
    @Email(message = "Enter a valid Email")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",message = "Enter Valid Password")
    private String passwordHash;

    private String bio;
    private UserRole role;
    private Boolean isActive;
    private Boolean emailVerified;
    private String profilePicturePath;
    private LocalDateTime lastLogin;
    private Integer loginAttempts;
    private Boolean accountLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String jwtToken;

}
