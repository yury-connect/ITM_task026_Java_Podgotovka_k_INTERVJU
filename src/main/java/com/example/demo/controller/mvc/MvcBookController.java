package com.example.demo.controller.mvc;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/mvc/books")
public class MvcBookController {

    private final BookService bookService;

    public MvcBookController(BookService bookService) {
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
    public String addBook(@Valid @ModelAttribute("newBook") Book newBook,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "books"; // останемся на странице с отображением ошибок
        }
        bookService.save(newBook);
        redirectAttributes.addFlashAttribute("message", "Book added successfully!");
        return "redirect:/mvc/books";
    }

    @GetMapping("/{id}")
    public String showBookDetails(@PathVariable UUID id, Model model) throws BookNotFoundException {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "book-details";
    }

    @PostMapping("/batch")
    public String addBooks(@ModelAttribute List<Book> books, RedirectAttributes redirectAttributes) {
        List<Book> savedBooks = bookService.save(books);
        redirectAttributes.addFlashAttribute("message",
                "Successfully added " + savedBooks.size() + " books");
        return "redirect:/mvc/books";
    }

    @GetMapping("/search")
    public String findBooksByIds(@RequestParam List<UUID> ids, Model model) {
        List<Book> books = bookService.findById(ids);
        model.addAttribute("books", books);
        return "book-list";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Book deleted successfully!");
        return "redirect:/mvc/books";
    }
}