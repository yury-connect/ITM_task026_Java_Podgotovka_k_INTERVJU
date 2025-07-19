package com.example.demo.bookManagement.controller.api;

import com.example.demo.bookManagement.exception.BookNotFoundException;
import com.example.demo.bookManagement.model.Book;
import com.example.demo.bookManagement.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class ApiBookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable UUID id) {
        try {
            Book book = bookService.findById(id);
            return ResponseEntity.ok(book);
        } catch (BookNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookService.save(book);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBook.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable UUID id,
            @RequestBody Book bookDetails) {
        try {
            Book updatedBook = bookService.save(bookDetails); // или реализуйте update в сервисе
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Book>> createBooks(@RequestBody List<Book> books) {
        List<Book> savedBooks = bookService.save(books);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByIds(@RequestParam List<UUID> ids) {
        List<Book> books = bookService.findById(ids);
        return ResponseEntity.ok(books);
    }
}
