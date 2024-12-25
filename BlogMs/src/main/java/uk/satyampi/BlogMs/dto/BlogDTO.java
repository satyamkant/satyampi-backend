package uk.satyampi.BlogMs.dto;

import lombok.*;
import uk.satyampi.BlogMs.entity.BlogPost;
import uk.satyampi.BlogMs.enums.BlogType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO implements Serializable {
        private Long blogId;
        private BlogType blogType;
        private String title;
        private String slug;
        private LocalDateTime dateCreated;
        private LocalDateTime dateUpdated;
        private boolean publishedStatus;
        private Long authorId;
        private BlogContentDTO blogContentDTO;

        public void convertToDTO (BlogPost blogPost) {
            this.blogId = blogPost.getBlogId();
            this.blogType = blogPost.getBlogType();
            this.title = blogPost.getTitle();
            this.slug = blogPost.getSlug();
            this.dateCreated = blogPost.getDateCreated();
            this.dateUpdated = blogPost.getDateUpdated();
            this.publishedStatus = blogPost.isPublishedStatus();
            this.authorId = blogPost.getAuthorId();

            this.blogContentDTO = new BlogContentDTO();
            this.blogContentDTO.convertToDTO(blogPost.getBlogContent());
        }

}
