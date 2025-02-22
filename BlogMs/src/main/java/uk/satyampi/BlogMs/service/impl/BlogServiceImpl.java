package uk.satyampi.BlogMs.service.impl;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.satyampi.BlogMs.dto.BlogDataDTO;
import uk.satyampi.BlogMs.entity.BlogContent;
import uk.satyampi.BlogMs.entity.BlogImage;
import uk.satyampi.BlogMs.entity.BlogPost;
import uk.satyampi.BlogMs.enums.BlogType;
import uk.satyampi.BlogMs.repository.BlogRepository;
import uk.satyampi.BlogMs.service.BlogService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BlogServiceImpl implements BlogService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final BlogRepository blogRepository;

    private BlogDataDTO getBlogDataDTO(BlogPost a) {
        BlogDataDTO blogDataDTO = new BlogDataDTO();

        blogDataDTO.setBlogId(a.getBlogId());
        blogDataDTO.setTitle(a.getTitle());
        blogDataDTO.setBlogType(a.getBlogType());
        blogDataDTO.setSlug(a.getSlug());
        blogDataDTO.setAuthorId(a.getAuthorId());
        blogDataDTO.setDateCreated(a.getDateCreated());
        blogDataDTO.setDateUpdated(a.getDateUpdated());
        blogDataDTO.setContent(a.getBlogContent().getContent());
        blogDataDTO.setImageUrls(a.getBlogContent().getBlogImages().stream().map(BlogImage::getImagePath).toList());
        blogDataDTO.setDescription(a.getDescription());
        return blogDataDTO;
    }

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public BlogDataDTO getBlogByTitle(String blogTitle){
        BlogPost blogPost = blogRepository.findByTitle(blogTitle).orElseThrow(()->new NoSuchElementException("Blog Not Found"));
        return getBlogDataDTO(blogPost);
    }

    @Override
    public String saveBlog(BlogDataDTO blogDataDTO) throws DataIntegrityViolationException {
        BlogPost blogPost = new BlogPost();

        blogPost.setTitle(blogDataDTO.getTitle());
        blogPost.setBlogType(blogDataDTO.getBlogType());
        blogPost.setSlug(blogDataDTO.getSlug());
        blogPost.setAuthorId(blogDataDTO.getAuthorId());
        blogPost.setPublishedStatus(false);
        blogPost.setDescription(blogDataDTO.getDescription());

        BlogContent blogContent = new BlogContent();
        blogContent.setContent(blogDataDTO.getContent());
        blogContent.setBlogPost(blogPost);

        if (blogDataDTO.getImageUrls() != null) {
            blogContent.setBlogImages(blogDataDTO.getImageUrls().stream().map(imageUrl -> {
                BlogImage blogImage = new BlogImage();
                blogImage.setImagePath(imageUrl);
                blogImage.setBlogContent(blogContent);
                return blogImage;
            }).toList());
        }

        blogPost.setBlogContent(blogContent);

        blogRepository.save(blogPost);
        return "Blog Saved successfully";
    }

    @Override
    public String updateBlog(BlogDataDTO blogDataDTO) {
        BlogPost blogPost = blogRepository.findById(blogDataDTO.getBlogId())
                .orElseThrow(() -> new NoSuchElementException("No Blog Found to update"));

        blogPost.setBlogType(blogDataDTO.getBlogType());
        blogPost.setDescription(blogDataDTO.getDescription());

        BlogContent blogContent = blogPost.getBlogContent();
        blogContent.setContent(blogDataDTO.getContent());

        if (blogDataDTO.getImageUrls() != null) {
            blogContent.getBlogImages().clear();
            List<BlogImage> blogImages = blogDataDTO.getImageUrls().stream().map(imageUrl -> {
                BlogImage blogImage = new BlogImage();
                blogImage.setImagePath(imageUrl);
                blogImage.setBlogContent(blogContent);
                return blogImage;
            }).toList();
            blogContent.getBlogImages().addAll(blogImages);
        }


        blogPost.setBlogContent(blogContent);
        blogRepository.save(blogPost);
        return "Blog Updated successfully";
    }

    @Override
    public List<BlogDataDTO> getAllBlogsByUser(Long id){
        List<BlogPost> blogPostList = blogRepository.findAllByAuthorId(id).orElseThrow(()->new NoSuchElementException("No Blog Found for User: "+ id.toString()));
        return blogPostList.stream().map(this::getBlogDataDTO).toList();
    }

    @Override
    public List<BlogDataDTO> getAllBLogs(){
        List<BlogPost> blogPostList = blogRepository.findAll();
        return blogPostList.stream().map(this::getBlogDataDTO).toList();
    }


    @Override
    public List<BlogDataDTO> getAllBlogsOfType(BlogType type){
        List<BlogPost> blogPostList = blogRepository.findAllByBlogType(type).orElseThrow(()->new NoSuchElementException("No Blog Found for Type: "+ type.toString()));
        return blogPostList.stream().map(this::getBlogDataDTO).toList();
    }
}
