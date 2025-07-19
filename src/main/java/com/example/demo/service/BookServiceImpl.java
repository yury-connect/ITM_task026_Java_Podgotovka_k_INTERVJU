package com.example.demo.service;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j

public class BookServiceImpl implements BookService {

    private final BookRepository repository;


    @Override
    public List<Book> findAll() {
        List<Book> books = repository.findAll();
        log.info("BookService.findAll(): Found {} books:", books.size());
        books.stream().forEach(book -> log.info("Book: {}", book));
        return books;
    }

    @Override
    public Book findById(UUID uuid) throws BookNotFoundException {
        Book book = repository.findById(uuid).orElseThrow(
                () -> new BookNotFoundException("Book with id " + uuid + " not found", HttpStatus.NOT_FOUND));
        log.info("BookService.findById(): Found {} book:", book);
        return book;
    }

    @Override
    public List<Book> findById(List<UUID> uuidList) {
        List<Book> books = new ArrayList<>();
        for (UUID id : uuidList) {
            try {
                books.add(findById(id));
            } catch (BookNotFoundException e) {
                log.error("BookService.findById(): Book with id {} not found", id);
            }
        }
        log.info("BookService.findById(): Found {} books:", books.size());
        books.stream().forEach(book -> log.info("Book: {}", book));
        return books;
    }

    @Override
    public Book save(Book book) {
        Book savedBook = repository.save(book);
        repository.flush();
        log.info("BookService.save: Saved book: {}", savedBook);
        return savedBook;
    }

    @Override
    public List<Book> save(List<Book> books) {
        List<Book> savingBooks = repository.saveAll(books);
        repository.flush();
        // Обновляем сущности из БД (если есть триггеры/генерация значений)

        List<Book> savedBooks =  repository.findAllById(
                savingBooks.stream()
                        .map(Book::getId)
                        .toList()
        );
        savedBooks.forEach(book -> log.info("BookService.save: Saved book: {}", book));
        return savedBooks;
    }

    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
        repository.flush();
        log.info("BookService.delete: Deleted book with id {}", uuid);
    }
}
