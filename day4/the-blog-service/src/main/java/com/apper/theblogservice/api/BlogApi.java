package com.apper.theblogservice.api;

import com.apper.theblogservice.exceptions.AccountNotFoundException;
import com.apper.theblogservice.exceptions.BlogNotFoundException;
import com.apper.theblogservice.model.Blog;
import com.apper.theblogservice.payload.*;
import com.apper.theblogservice.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogApi {

    private final BlogService blogService;

    public BlogApi(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateBlogResponse createBlog(@Valid @RequestBody CreateBlogRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessages.add(fieldError.getDefaultMessage());
            }

            throw new IllegalArgumentException(String.join(", ", errorMessages));
        }

        Blog createdBlog = blogService.createBlog(request.getTitle(), request.getBody(), request.getBloggerId());

        CreateBlogResponse response = new CreateBlogResponse();
        response.setBlogId(createdBlog.getBlogId());
        response.setBloggerId(createdBlog.getBloggerId());
        response.setDateCreated(createdBlog.getCreatedAt());
        response.setDateUpdated(createdBlog.getLastUpdate());

        return response;
    }

    @PutMapping("{id}")
    public UpdateBlogResponse updateBlog(@PathVariable String id, @Valid @RequestBody UpdateBlogRequest request) {
        blogService.updateBlog(id, request.getTitle(), request.getBody());
        Blog blog = blogService.getBlog(id);

        UpdateBlogResponse response = new UpdateBlogResponse();
        response.setBlogId(blog.getBlogId());
        response.setBloggerId(blog.getBloggerId());
        response.setDateCreated(blog.getCreatedAt());
        response.setDateUpdated(blog.getLastUpdate());

        return response;
    }

    @GetMapping("{id}")
    public BlogDetails getBlog(@PathVariable String id) {
        Blog blog = blogService.getBlog(id);

        BlogDetails blogDetails = new BlogDetails();
        blogDetails.setBloggerId(blog.getBloggerId());
        blogDetails.setTitle(blog.getTitle());
        blogDetails.setBody(blog.getBody());
        blogDetails.setDateCreated(blog.getCreatedAt());
        blogDetails.setDateUpdated(blog.getLastUpdate());

        return blogDetails;
    }

    @GetMapping
    public List<BlogDetails> getAllBlog() {
        List<BlogDetails> responseList = new ArrayList<>();

        for (Blog blog : blogService.getAllBlog()) {
            BlogDetails response = getBlog(blog.getBlogId());
            responseList.add(response);
        }

        return responseList;
    }

    @GetMapping("/blogger/{id}")
    public List<GetAllBlogsByBloggerResponse> getAllBlogsByBlogger(@PathVariable String id) {
        List<Blog> blogs = blogService.getAllBlogsBy(id);
        List<GetAllBlogsByBloggerResponse> responseList = new ArrayList<>();

        for (Blog blog : blogs) {
            GetAllBlogsByBloggerResponse response = new GetAllBlogsByBloggerResponse();
            response.setTitle(blog.getTitle());
            response.setBody(blog.getBody());
            response.setDateCreated(blog.getCreatedAt());
            response.setDateUpdated(blog.getLastUpdate());

            responseList.add(response);
        }

        return responseList;
    }

    // Exception handlers

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BlogNotFoundException.class)
    public String handleBlogNotFoundException(BlogNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorMessages = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessages.add(fieldError.getDefaultMessage());
        }

        return String.join(", ", errorMessages);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFoundException(AccountNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
