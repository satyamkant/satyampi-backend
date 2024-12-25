package uk.satyampi.SecurityMs.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto implements Serializable {
    private String message;
    private String error;
    private UserDto userDto;
}
