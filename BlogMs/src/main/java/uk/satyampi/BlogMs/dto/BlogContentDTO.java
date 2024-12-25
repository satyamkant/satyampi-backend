package uk.satyampi.BlogMs.dto;

import lombok.*;
import uk.satyampi.BlogMs.entity.BlogContent;
import uk.satyampi.BlogMs.entity.BlogPost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogContentDTO implements Serializable {
    private Long blogContentId;
    private BlogPost blogPost;
    private String content;
    private List<BlogImageDTO> blogImages;

    public void convertToDTO(BlogContent blogContent) {
        this.blogContentId = blogContent.getBlogContentId();
        this.blogPost = blogContent.getBlogPost();
        this.content = blogContent.getContent();
        this.blogImages = blogContent.getBlogImages().stream().map((a)->{
            BlogImageDTO blogImageDTO = new BlogImageDTO();
            blogImageDTO.convertToDTO(a);
            return blogImageDTO;
        }).toList();
    }
}
