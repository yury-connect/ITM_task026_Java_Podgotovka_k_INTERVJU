package com.example.demo.controller.api;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/books")
public class ApiBookController {

    private final BookService bookService;

    public ApiBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String showBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("newBook", new Book());
        return "books";
    }

    @PostMapping
    public String addBook(@ModelAttribute Book newBook, RedirectAttributes redirectAttributes) {
        Book savedBook = bookService.save(newBook);
        redirectAttributes.addFlashAttribute("message", "Book added successfully!");
        return "redirect:/books";
    }

    // пока только этот петод переписал
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable UUID id) {
        try {
            Book book = bookService.findById(id);
            return ResponseEntity.ok(book);
        } catch (BookNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/batch")
    public String addBooks(@ModelAttribute List<Book> books, RedirectAttributes redirectAttributes) {
        List<Book> savedBooks = bookService.save(books);
        redirectAttributes.addFlashAttribute("message",
                "Successfully added " + savedBooks.size() + " books");
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String findBooksByIds(@RequestParam List<UUID> ids, Model model) {
        List<Book> books = bookService.findById(ids);
        model.addAttribute("books", books);
        return "book-list";
    }
}