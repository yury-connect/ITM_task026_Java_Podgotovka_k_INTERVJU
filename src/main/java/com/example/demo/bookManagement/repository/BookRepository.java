package com.example.demo.bookManagement.repository;

import com.example.demo.bookManagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface BookRepository extends JpaRepository<Book, UUID> {
}
