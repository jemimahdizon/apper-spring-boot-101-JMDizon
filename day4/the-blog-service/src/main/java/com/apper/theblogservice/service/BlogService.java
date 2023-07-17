package com.apper.theblogservice.service;

import com.apper.theblogservice.exceptions.AccountNotFoundException;
import com.apper.theblogservice.exceptions.IllegalArgumentException;
import com.apper.theblogservice.exceptions.BlogNotFoundException;
import com.apper.theblogservice.model.Blog;
import com.apper.theblogservice.repository.BlogRepository;
import com.apper.theblogservice.repository.BloggerRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final BloggerRepository bloggerRepository;

    public BlogService(BlogRepository blogRepository, BloggerRepository bloggerRepository) {
        this.blogRepository = blogRepository;
        this.bloggerRepository = bloggerRepository;
    }

    public Blog createBlog(String title, String body, String bloggerId) {
        if (StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("Title must not be empty.");
        }

        if (StringUtils.isBlank(body)) {
            throw new IllegalArgumentException("Body must not be empty.");
        }

        if (StringUtils.isBlank(bloggerId)) {
            throw new IllegalArgumentException("Blogger ID must not be empty.");
        }

        if (!bloggerRepository.existsById(UUID.fromString(bloggerId))) {
            throw new AccountNotFoundException("Blogger ID: " + bloggerId + " does not exist.");
        }

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setBody(body);
        blog.setBloggerId(bloggerId);

        return blogRepository.save(blog);
    }


    public Blog getBlog(String id) {
        if (blogRepository.existsById(id)) {
            Optional<Blog> blogResult = blogRepository.findById(id);
            return blogResult.get();
        }
        throw new BlogNotFoundException("Blog id: " + id + " not found!");
    }

    public List<Blog> getAllBlog() {
        return (List<Blog>) blogRepository.findAll();
    }

    public List<Blog> getAllBlogsBy(String id) {
        if (!bloggerRepository.existsById(UUID.fromString(id))) {
            throw new AccountNotFoundException("Account id: " + id + " not found!");
        }
        return blogRepository.findAllByBloggerId(id);
    }

    public void updateBlog(String id, String title, String body) {
        if (!blogRepository.existsById(id)) {
            throw new BlogNotFoundException("Blog id: " + id + " not found!");
        }

        Optional<Blog> blogResult = blogRepository.findById(id);
        Blog blog = blogResult.get();
        blog.setTitle(title);
        blog.setBody(body);
        blog.setLastUpdate();
        blogRepository.save(blog);
    }

    // Exception handlers

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFoundException(AccountNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BlogNotFoundException.class)
    public String handleBlogNotFoundException(BlogNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
