package com.apper.theblogservice.service;

import com.apper.theblogservice.exceptions.AccountNotFoundException;
import com.apper.theblogservice.exceptions.EmailAlreadyRegisteredException;
import com.apper.theblogservice.model.Blogger;
import com.apper.theblogservice.payload.CreateBloggerRequest;
import com.apper.theblogservice.repository.BloggerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BloggerService {
    private final BloggerRepository bloggerRepository;

    public BloggerService(BloggerRepository bloggerRepository) {
        this.bloggerRepository = bloggerRepository;
    }

    public Blogger createBlogger(CreateBloggerRequest request) {
        if (bloggerRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyRegisteredException("Email is already registered: " + request.getEmail());
        }

        Blogger blogger = new Blogger();
        blogger.setId(UUID.randomUUID());
        blogger.setEmail(request.getEmail());
        blogger.setName(request.getName());
        blogger.setPassword(request.getPassword());
        blogger.setCreatedAt(LocalDateTime.now());
        blogger.setLastUpdate(LocalDateTime.now());

        return bloggerRepository.save(blogger);
    }

    public Blogger getBlogger(UUID id) {
        return bloggerRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Blogger not found with ID: " + id));
    }

    public List<Blogger> getAllBloggers() {
        return bloggerRepository.findAll();
    }
}
