package uk.satyampi.BlogMs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.satyampi.BlogMs.dto.BlogContentDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Blog_Content")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BlogContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Blog_Content_Id")
    private Long blogContentId;

    @OneToOne
    @JoinColumn(name = "Blog_Id", nullable = false)
    private BlogPost blogPost;

    @Column(name = "Content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "blogContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogImage> blogImages;

    public void convertToEntity(BlogContentDTO blogContentDTO) {
        this.blogContentId = blogContentDTO.getBlogContentId();
        this.blogPost = blogContentDTO.getBlogPost();
        this.content = blogContentDTO.getContent();
        this.blogImages = blogContentDTO.getBlogImages().stream().map((e)->{
            BlogImage blogImage = new BlogImage();
            blogImage.convertToEntity(e);
            return blogImage;
        }).toList();
    }


    // Getters and Setters
}
