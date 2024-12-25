package uk.satyampi.BlogMs.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.satyampi.BlogMs.enums.UserRole;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDTO implements Serializable {
    private Long userId;
    private String name;
    private String email;
    private String bio;
    private UserRole role;
}
