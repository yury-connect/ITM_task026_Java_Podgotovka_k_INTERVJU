package com.example.demo.controller.mvc;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public String showBooks(Model model) {
        model.addAttribute("books", bookService.findAllBooks());
        model.addAttribute("newBook", new Book());
        return "books";
    }

    @PostMapping("/books")
    public String addBook(@ModelAttribute Book newBook) {
        bookService.saveBook(newBook);
        return "redirect:/books";
    }
}