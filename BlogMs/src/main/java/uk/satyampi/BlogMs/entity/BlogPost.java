package uk.satyampi.BlogMs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uk.satyampi.BlogMs.enums.BlogType;

import java.time.LocalDateTime;

@Entity
@Table(name = "Blog_Post", indexes = {@Index(name = "idx_blog_title", columnList = "Title")})
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Blog_Id")
    private Long blogId;

    @Column(name = "Blog_Type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BlogType blogType;

    @Column(name = "Title", nullable = false,unique = true)
    private String title;

    @Column(name = "Slug", nullable = false, unique = true)
    private String slug;

    @CreationTimestamp
    @Column(name = "Date_Created", nullable = false)
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(name = "Date_Updated")
    private LocalDateTime dateUpdated;

    @Column(name = "Published_Status", nullable = false)
    private boolean publishedStatus;

    @Column(name = "Author_Id", nullable = false)
    private Long authorId;

    @OneToOne(mappedBy = "blogPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private BlogContent blogContent;

    // Getters and Setters
}
