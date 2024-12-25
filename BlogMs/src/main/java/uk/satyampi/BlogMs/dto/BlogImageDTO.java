package uk.satyampi.BlogMs.dto;

import lombok.*;
import uk.satyampi.BlogMs.entity.BlogContent;
import uk.satyampi.BlogMs.entity.BlogImage;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogImageDTO implements Serializable {
    private Long id;
    private BlogContent blogContent;
    private String imagePath;

    public void convertToDTO(BlogImage blogImage) {
        this.blogContent = blogImage.getBlogContent();
        this.imagePath = blogImage.getImagePath();
        this.id = blogImage.getId();
    }
}
