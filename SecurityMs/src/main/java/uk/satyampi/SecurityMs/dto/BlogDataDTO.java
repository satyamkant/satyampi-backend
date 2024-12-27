package uk.satyampi.SecurityMs.dto;

import lombok.*;
import uk.satyampi.SecurityMs.enums.BlogType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogDataDTO implements Serializable {
    private BlogType blogType;
    private String title;
    private String slug;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private Long authorId;
    private String content;
    private List<String> imageUrls;
    private boolean publishedStatus;

}
