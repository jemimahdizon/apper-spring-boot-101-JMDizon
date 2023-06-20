package com.gcash.service.book;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private List<Book> books = new ArrayList<>();

    private final IdGenerator idGenerator;

    public BookService(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Book createBook(String title) {
        Book book = new Book();
        book.setId(idGenerator.nextId());
        book.setTitle(title);

        books.add(book);

        return book;
    }
}