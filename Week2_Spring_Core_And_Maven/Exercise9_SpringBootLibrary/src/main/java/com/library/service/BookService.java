package com.library.service;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * BookService — business logic layer for library operations.
 *
 * @Service marks this as a Spring-managed service component.
 * @Transactional ensures each method runs in a database transaction.
 */
@Service
@Transactional
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    // Constructor injection — preferred over field injection
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ---------------------------------------------------------------
    // Create
    // ---------------------------------------------------------------

    /**
     * Persists a new book. Throws if ISBN already exists.
     */
    public Book addBook(Book book) {
        logger.info("Adding book: title='{}', author='{}'", book.getTitle(), book.getAuthor());
        if (book.getIsbn() != null) {
            bookRepository.findByIsbn(book.getIsbn()).ifPresent(existing -> {
                throw new IllegalArgumentException(
                        "A book with ISBN '" + book.getIsbn() + "' already exists");
            });
        }
        Book saved = bookRepository.save(book);
        logger.info("Book saved with id={}", saved.getId());
        return saved;
    }

    // ---------------------------------------------------------------
    // Read
    // ---------------------------------------------------------------

    /**
     * Returns all books in the library.
     */
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        logger.debug("Fetching all books");
        return bookRepository.findAll();
    }

    /**
     * Returns a single book by ID, or empty if not found.
     */
    @Transactional(readOnly = true)
    public Optional<Book> getBookById(Long id) {
        logger.debug("Fetching book with id={}", id);
        return bookRepository.findById(id);
    }

    /**
     * Returns books whose title contains the given keyword (case-insensitive).
     */
    @Transactional(readOnly = true)
    public List<Book> searchByTitle(String keyword) {
        logger.info("Searching books by title keyword='{}'", keyword);
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    /**
     * Returns all books by a given author.
     */
    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthor(String author) {
        logger.info("Fetching books by author='{}'", author);
        return bookRepository.findByAuthor(author);
    }

    /**
     * Returns books priced within [min, max].
     */
    @Transactional(readOnly = true)
    public List<Book> getBooksByPriceRange(Double min, Double max) {
        logger.info("Fetching books in price range [{}, {}]", min, max);
        return bookRepository.findByPriceRange(min, max);
    }

    // ---------------------------------------------------------------
    // Update
    // ---------------------------------------------------------------

    /**
     * Updates an existing book. Returns the updated book, or empty if not found.
     */
    public Optional<Book> updateBook(Long id, Book updatedDetails) {
        logger.info("Updating book id={}", id);
        return bookRepository.findById(id).map(existing -> {
            existing.setTitle(updatedDetails.getTitle());
            existing.setAuthor(updatedDetails.getAuthor());
            existing.setIsbn(updatedDetails.getIsbn());
            existing.setPrice(updatedDetails.getPrice());
            Book saved = bookRepository.save(existing);
            logger.info("Book id={} updated successfully", id);
            return saved;
        });
    }

    // ---------------------------------------------------------------
    // Delete
    // ---------------------------------------------------------------

    /**
     * Deletes a book by ID. Returns true if deleted, false if not found.
     */
    public boolean deleteBook(Long id) {
        logger.info("Deleting book id={}", id);
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            logger.info("Book id={} deleted", id);
            return true;
        }
        logger.warn("Delete failed — book id={} not found", id);
        return false;
    }
}
