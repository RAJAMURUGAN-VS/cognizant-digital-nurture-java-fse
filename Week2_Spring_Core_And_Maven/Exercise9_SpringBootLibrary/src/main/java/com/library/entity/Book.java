package com.library.entity;

import javax.persistence.*;

/**
 * Book — JPA entity mapped to the BOOKS table.
 *
 * @Entity  : marks this class as a JPA-managed persistent entity.
 * @Table   : maps to the "books" table (optional; defaults to class name).
 * @Id     : marks the primary key field.
 * @GeneratedValue : auto-increments the ID using the database identity column.
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    @Column(name = "price")
    private Double price;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    public Book() {
    }

    public Book(String title, String author, String isbn, Double price) {
        this.title  = title;
        this.author = author;
        this.isbn   = isbn;
        this.price  = price;
    }

    // ---------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author
                + "', isbn='" + isbn + "', price=" + price + "}";
    }
}
