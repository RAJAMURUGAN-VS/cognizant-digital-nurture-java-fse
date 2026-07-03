package com.library.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * BookRepository — data access layer for Book entities.
 *
 * Exercise 1 : Plain class defined as a Spring bean in applicationContext.xml.
 * Exercise 6 : @Repository annotation enables component scanning discovery.
 *
 * @Repository also registers this bean as a candidate for Spring's
 * exception translation (DataAccessException hierarchy).
 */
@Repository
public class BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);

    // In-memory store for demonstration purposes
    private final List<String> books = new ArrayList<>();

    public BookRepository() {
        // Pre-populate with sample data
        books.add("Effective Java");
        books.add("Clean Code");
        books.add("Design Patterns");
        logger.info("BookRepository initialised with {} sample books", books.size());
    }

    /**
     * Returns all books currently in the repository.
     */
    public List<String> findAll() {
        logger.debug("BookRepository.findAll() called");
        return new ArrayList<>(books);
    }

    /**
     * Persists a new book title.
     */
    public void save(String title) {
        logger.debug("BookRepository.save() called with title='{}'", title);
        books.add(title);
        logger.info("Book '{}' saved to repository", title);
    }

    /**
     * Removes a book by title.
     */
    public boolean delete(String title) {
        boolean removed = books.remove(title);
        if (removed) {
            logger.info("Book '{}' deleted from repository", title);
        } else {
            logger.warn("Book '{}' not found in repository — nothing deleted", title);
        }
        return removed;
    }
}
