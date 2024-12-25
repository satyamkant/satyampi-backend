package uk.satyampi.BlogMs.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.satyampi.BlogMs.dto.BlogDTO;
import uk.satyampi.BlogMs.entity.BlogPost;
import uk.satyampi.BlogMs.repository.BlogRepository;
import uk.satyampi.BlogMs.service.BlogService;

@Service
@Transactional
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public BlogDTO getBlogByTitle(String blogTitle){
        BlogPost blogPost = blogRepository.findByTitle(blogTitle).orElseThrow();

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.convertToDTO(blogPost);
        return blogDTO;
    }

    @Override
    public String saveBlog(BlogDTO blogDTO) {
        BlogPost blogPost = new BlogPost();
        blogPost.convertToEntity(blogDTO);

        blogRepository.save(blogPost);
        return "Blog Saved successfully";
    }

    @Override
    public String updateBlog(BlogDTO blogDTO) {
        BlogPost blogPost = blogRepository.findByTitle(blogDTO.getTitle()).orElseThrow();
        blogPost.convertToEntity(blogDTO);

        blogRepository.save(blogPost);
        return "Blog Updated successfully";
    }
}
