package uk.satyampi.SecurityMs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import uk.satyampi.SecurityMs.enums.BlogType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class BlogDataDTO implements Serializable {

    @NotNull
    private BlogType blogType;
    @NotNull(message = "title must not be null")
    @NotBlank(message = "title must not be blank")
    private String title;
    @NotNull(message = "slug must not be null")
    @NotBlank(message = "slug must not be blank")
    private String slug;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    @NotNull(message = "authorId must not be null")
    private Long authorId;
    @NotNull(message = "content must not be null")
    @NotBlank(message = "content must not be blank")
    private String content;
    private List<String> imageUrls;
    private boolean publishedStatus;

}
