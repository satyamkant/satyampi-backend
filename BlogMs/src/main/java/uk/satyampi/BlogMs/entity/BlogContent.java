package uk.satyampi.BlogMs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @OneToMany(mappedBy = "blogContent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BlogImage> blogImages;

    // Getters and Setters
}
