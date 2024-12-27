package uk.satyampi.BlogMs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.satyampi.BlogMs.dto.*;
import uk.satyampi.BlogMs.service.BlogService;

@RestController
@RequestMapping("/blog")
public class BlogController {
    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blogtitle")
    public ResponseEntity<?> getBlogBytitle(@RequestParam String title) {
        BlogDataDTO blogDataDTO = blogService.getBlogByTitle(title);
        return new ResponseEntity<>(new ResponseDTO("Successfully fetched blog " + title,HttpStatus.OK.toString(),null,blogDataDTO), HttpStatus.OK);
    }

    @PostMapping("/saveblog")
    public ResponseEntity<?> saveBlog(@RequestBody BlogDataDTO blogDataDTO) {
        String message  = blogService.saveBlog(blogDataDTO);
        return new ResponseEntity<>(new ResponseDTO(message,HttpStatus.OK.toString(),null,null), HttpStatus.OK);
    }

}
