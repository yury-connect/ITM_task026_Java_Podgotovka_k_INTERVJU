package com.example.demo.service;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface BookService {

    public List<Book> findAll();

    public Book findById(UUID uuid) throws BookNotFoundException;

    public List<Book> findById(List<UUID> uuidList);

    public Book save(Book book);

    public List<Book> save(List<Book> books);

    public void delete(UUID uuid);
}
