package com.library.service;

import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BookService — business logic layer for library operations.
 *
 * Exercise 1  : Plain class, bean defined in applicationContext.xml.
 * Exercise 2  : BookRepository wired via setter injection (XML config).
 * Exercise 5  : Setter injection confirmed via IoC container.
 * Exercise 6  : @Service annotation makes it a component-scanned bean.
 * Exercise 7  : Demonstrates both constructor injection and setter injection.
 *               - Constructor injection: BookService(BookRepository)
 *               - Setter injection:      setBookRepository(BookRepository)
 */
@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepository;

    // ---------------------------------------------------------------
    // Exercise 7: Constructor Injection
    // ---------------------------------------------------------------
    /**
     * Constructor injection — Spring calls this when configured via
     * &lt;constructor-arg ref="bookRepository"/&gt; in applicationContext.xml.
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        logger.info("BookService created via constructor injection");
    }

    /**
     * No-arg constructor required when using pure setter injection
     * (i.e., when the XML uses &lt;property&gt; without &lt;constructor-arg&gt;).
     */
    public BookService() {
        logger.info("BookService created via no-arg constructor (setter injection will follow)");
    }

    // ---------------------------------------------------------------
    // Exercise 2, 5, 7: Setter Injection
    // ---------------------------------------------------------------
    /**
     * Spring calls this setter when configured via
     * &lt;property name="bookRepository" ref="bookRepository"/&gt;.
     *
     * @Autowired also works for annotation-driven wiring (Exercise 6).
     */
    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        logger.info("BookRepository injected into BookService via setter");
    }

    // ---------------------------------------------------------------
    // Business Methods
    // ---------------------------------------------------------------

    /**
     * Returns all books from the repository.
     */
    public List<String> getAllBooks() {
        logger.info("BookService.getAllBooks() called");
        return bookRepository.findAll();
    }

    /**
     * Adds a new book to the library.
     */
    public void addBook(String title) {
        logger.info("BookService.addBook() called with title='{}'", title);
        if (title == null || title.trim().isEmpty()) {
            logger.warn("addBook() called with null/empty title — ignoring");
            return;
        }
        bookRepository.save(title);
    }

    /**
     * Removes a book from the library by title.
     */
    public boolean removeBook(String title) {
        logger.info("BookService.removeBook() called with title='{}'", title);
        return bookRepository.delete(title);
    }
}
