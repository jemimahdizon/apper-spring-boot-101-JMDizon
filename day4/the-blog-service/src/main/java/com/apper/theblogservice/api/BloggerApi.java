package com.apper.theblogservice.api;

import com.apper.theblogservice.exceptions.AccountNotFoundException;
import com.apper.theblogservice.exceptions.BloggerNotFoundException;
import com.apper.theblogservice.exceptions.EmailAlreadyRegisteredException;
import com.apper.theblogservice.model.Blogger;
import com.apper.theblogservice.payload.BloggerDetails;
import com.apper.theblogservice.payload.CreateBloggerRequest;
import com.apper.theblogservice.payload.CreateBloggerResponse;
import com.apper.theblogservice.service.BloggerService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/blogger")
@Validated
public class BloggerApi {

    private final BloggerService bloggerService;

    public BloggerApi(BloggerService bloggerService) {
        this.bloggerService = bloggerService;
    }

    @PostMapping
    public ResponseEntity<CreateBloggerResponse> createBlogger(@Valid @RequestBody CreateBloggerRequest request) {
        Blogger createdBlogger = bloggerService.createBlogger(request);

        CreateBloggerResponse response = new CreateBloggerResponse();
        response.setId(String.valueOf(createdBlogger.getId()));
        response.setDateRegistration(createdBlogger.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloggerDetails> getBlogger(@PathVariable UUID id) {
        Blogger blogger = bloggerService.getBlogger(id);

        if (blogger == null) {
            throw new BloggerNotFoundException("Blogger not found with ID: " + id);
        }

        BloggerDetails bloggerDetails = new BloggerDetails();
        bloggerDetails.setId(blogger.getId());
        bloggerDetails.setName(blogger.getName());
        bloggerDetails.setEmail(blogger.getEmail());
        bloggerDetails.setDateRegistration(blogger.getCreatedAt());

        return ResponseEntity.ok(bloggerDetails);
    }

    @GetMapping
    public ResponseEntity<List<BloggerDetails>> getAllBloggers() {
        List<Blogger> bloggers = bloggerService.getAllBloggers();

        List<BloggerDetails> bloggerDetailsList = new ArrayList<>();
        for (Blogger blogger : bloggers) {
            BloggerDetails bloggerDetails = new BloggerDetails();
            bloggerDetails.setId(blogger.getId());
            bloggerDetails.setName(blogger.getName());
            bloggerDetails.setEmail(blogger.getEmail());
            bloggerDetails.setDateRegistration(blogger.getCreatedAt());
            bloggerDetailsList.add(bloggerDetails);
        }

        return ResponseEntity.ok(bloggerDetailsList);
    }

    @ExceptionHandler({BloggerNotFoundException.class, EmailAlreadyRegisteredException.class, AccountNotFoundException.class})
    public ResponseEntity<String> handleAccountException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }

        String errorMessage = String.join(", ", errorMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            errorMessages.add(constraintViolation.getMessage());
        }

        String errorMessage = String.join(", ", errorMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
