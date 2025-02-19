package uk.satyampi.BlogMs.service;

import org.springframework.dao.DataIntegrityViolationException;
import uk.satyampi.BlogMs.dto.BlogDataDTO;
import uk.satyampi.BlogMs.enums.BlogType;

import java.util.List;

public interface BlogService {
    BlogDataDTO getBlogByTitle(String blogTitle);

    String saveBlog(BlogDataDTO blogDataDTO) throws DataIntegrityViolationException;

    String updateBlog(BlogDataDTO blogDataDTO);

    List<BlogDataDTO> getAllBlogsByUser(Long id);

    List<BlogDataDTO> getAllBLogs();

    List<BlogDataDTO> getAllBlogsOfType(BlogType type);
}
