package uk.satyampi.BlogMs.service;

import uk.satyampi.BlogMs.dto.BlogDTO;

public interface BlogService {
    BlogDTO getBlogByTitle(String blogTitle);

    String saveBlog(BlogDTO blogDTO);

    String updateBlog(BlogDTO blogDTO);
}
