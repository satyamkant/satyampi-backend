package uk.satyampi.BlogMs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.satyampi.BlogMs.dto.BlogImageDTO;

@Entity
@Table(name = "Blog_Image")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BlogImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Blog_Content_Id", nullable = false)
    private BlogContent blogContent;

    @Column(name = "Image_Path", nullable = false)
    private String imagePath;

    public void convertToEntity(BlogImageDTO blogImageDTO) {
        this.imagePath = blogImageDTO.getImagePath();
        this.blogContent = blogImageDTO.getBlogContent();
    }

    // Getters and Setters
}
