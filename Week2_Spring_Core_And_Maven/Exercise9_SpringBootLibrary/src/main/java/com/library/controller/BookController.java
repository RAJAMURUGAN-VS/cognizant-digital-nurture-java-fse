package com.library.controller;

import com.library.entity.Book;
import com.library.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BookController — REST API for CRUD operations on Book entities.
 *
 * Base URL: /api/books
 *
 * Endpoints:
 *   GET    /api/books                        — list all books
 *   GET    /api/books/{id}                   — get book by ID
 *   GET    /api/books/search?title=keyword   — search by title keyword
 *   GET    /api/books/author?name=author     — get books by author
 *   GET    /api/books/price?min=x&max=y      — get books by price range
 *   POST   /api/books                        — create a new book
 *   PUT    /api/books/{id}                   — update an existing book
 *   DELETE /api/books/{id}                   — delete a book
 *
 * Test with curl or Postman:
 *   curl http://localhost:8080/api/books
 *   curl -X POST http://localhost:8080/api/books \
 *        -H "Content-Type: application/json" \
 *        -d '{"title":"Clean Code","author":"Robert Martin","isbn":"978-0132350884","price":35.99}'
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ---------------------------------------------------------------
    // GET /api/books — retrieve all books
    // ---------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.info("GET /api/books");
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // ---------------------------------------------------------------
    // GET /api/books/{id} — retrieve a single book by ID
    // ---------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.info("GET /api/books/{}", id);
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Book id={} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // ---------------------------------------------------------------
    // GET /api/books/search?title=keyword — search by title
    // ---------------------------------------------------------------
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchByTitle(@RequestParam String title) {
        logger.info("GET /api/books/search?title={}", title);
        List<Book> results = bookService.searchByTitle(title);
        return ResponseEntity.ok(results);
    }

    // ---------------------------------------------------------------
    // GET /api/books/author?name=author — get books by author
    // ---------------------------------------------------------------
    @GetMapping("/author")
    public ResponseEntity<List<Book>> getByAuthor(@RequestParam String name) {
        logger.info("GET /api/books/author?name={}", name);
        return ResponseEntity.ok(bookService.getBooksByAuthor(name));
    }

    // ---------------------------------------------------------------
    // GET /api/books/price?min=x&max=y — get books in price range
    // ---------------------------------------------------------------
    @GetMapping("/price")
    public ResponseEntity<List<Book>> getByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        logger.info("GET /api/books/price?min={}&max={}", min, max);
        return ResponseEntity.ok(bookService.getBooksByPriceRange(min, max));
    }

    // ---------------------------------------------------------------
    // POST /api/books — create a new book
    // ---------------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        logger.info("POST /api/books — title='{}'", book.getTitle());
        try {
            Book created = bookService.addBook(book);
            // 201 Created with the new book in the response body
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            logger.warn("Create failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // PUT /api/books/{id} — update an existing book
    // ---------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book) {
        logger.info("PUT /api/books/{}", id);
        return bookService.updateBook(id, book)
                .map(updated -> ResponseEntity.ok((Object) updated))
                .orElseGet(() -> {
                    logger.warn("Update failed — book id={} not found", id);
                    return ResponseEntity.notFound().<Object>build();
                });
    }

    // ---------------------------------------------------------------
    // DELETE /api/books/{id} — delete a book
    // ---------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        logger.info("DELETE /api/books/{}", id);
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            return ResponseEntity.ok("Book id=" + id + " deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book id=" + id + " not found");
    }
}
