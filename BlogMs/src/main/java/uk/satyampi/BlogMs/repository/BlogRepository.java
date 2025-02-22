package uk.satyampi.BlogMs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.satyampi.BlogMs.entity.BlogPost;
import uk.satyampi.BlogMs.enums.BlogType;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<BlogPost,Long> {
    List<BlogPost> findAll();
    Optional<BlogPost> findByTitle(String title);
    Optional<List<BlogPost>> findAllByAuthorId(Long authorId);
    Optional<List<BlogPost>> findAllByBlogType(BlogType blogType);

}
