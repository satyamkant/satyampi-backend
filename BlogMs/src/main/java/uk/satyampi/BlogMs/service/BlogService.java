package uk.satyampi.BlogMs.service;

import uk.satyampi.BlogMs.dto.BlogDataDTO;
import uk.satyampi.BlogMs.enums.BlogType;

import java.util.List;

public interface BlogService {
    BlogDataDTO getBlogByTitle(String blogTitle);

    String saveBlog(BlogDataDTO blogDataDTO);

    String updateBlog(BlogDataDTO blogDataDTO);

    List<BlogDataDTO> getAllBlogsByUser(Long id);

    List<BlogDataDTO> getAllBlogsOfType(BlogType type);
}
