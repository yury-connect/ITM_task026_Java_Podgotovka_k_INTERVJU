package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Book {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @NotBlank(message = "Book title cannot be empty")
    @Size(max = 255, message = "Title length must not exceed 255 characters")
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be at least 1")  // Для целых чисел лучше @Min
    @Max(value = Integer.MAX_VALUE, message = "Price must not exceed " + Integer.MAX_VALUE)
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private Integer price;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private BookStatus status = BookStatus.AVAILABLE;


    // Состояние книги
    public enum BookStatus {
        AVAILABLE, RESERVED, SOLD_OUT
    }
}
