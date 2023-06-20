package com.gcash.service.book;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book")
public class BookApi {

    private final BookService bookService;

    // Constructor injection of BookService
    public BookApi(BookService bookService) {
        this.bookService = bookService;
    }

    // POST endpoint to create a new book
    @PostMapping
    public Book createBook(@RequestBody CreateBook createBook) {
        // Delegate the book creation to the BookService
        return bookService.createBook(createBook.getTitle());
    }
}
