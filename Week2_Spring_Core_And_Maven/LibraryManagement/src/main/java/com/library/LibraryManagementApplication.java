package com.library;

import com.library.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * LibraryManagementApplication — entry point for Exercises 1–8.
 *
 * Loads the Spring IoC container from applicationContext.xml and
 * exercises the BookService / BookRepository beans to verify:
 *
 *   Exercise 1 : Basic Spring context loads, beans are available.
 *   Exercise 2 : BookRepository is injected into BookService (setter).
 *   Exercise 3 : LoggingAspect prints execution times (@Around).
 *   Exercise 5 : IoC container wires dependencies via setter injection.
 *   Exercise 6 : Annotation-driven beans discovered via component scan.
 *   Exercise 7 : Constructor + setter injection both verified.
 *   Exercise 8 : @Before and @After advice prints method entry/exit.
 */
public class LibraryManagementApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(LibraryManagementApplication.class);

    public static void main(String[] args) {

        logger.info("=== Library Management System Starting ===");

        // Load Spring application context from classpath XML
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        logger.info("Spring ApplicationContext loaded successfully");

        // Retrieve the BookService bean (with BookRepository already injected)
        BookService bookService = context.getBean("bookService", BookService.class);

        // -----------------------------------------------------------------
        // Verify Exercise 1 & 2: Context loads and DI works
        // -----------------------------------------------------------------
        logger.info("--- Listing all books (initial state) ---");
        List<String> books = bookService.getAllBooks();
        books.forEach(b -> logger.info("  Book: {}", b));

        // -----------------------------------------------------------------
        // Verify Exercise 3 & 8: AOP advice fires on service method calls
        // (observe [BEFORE], [AFTER], [AROUND] log lines in output)
        // -----------------------------------------------------------------
        logger.info("--- Adding a new book ---");
        bookService.addBook("The Pragmatic Programmer");

        logger.info("--- Listing all books (after add) ---");
        bookService.getAllBooks().forEach(b -> logger.info("  Book: {}", b));

        logger.info("--- Removing a book ---");
        boolean removed = bookService.removeBook("Clean Code");
        logger.info("  Removed 'Clean Code': {}", removed);

        logger.info("--- Attempting to remove non-existent book ---");
        bookService.removeBook("Unknown Book");

        logger.info("--- Final book list ---");
        bookService.getAllBooks().forEach(b -> logger.info("  Book: {}", b));

        // -----------------------------------------------------------------
        // Verify Exercise 7: Constructor injection path
        // (switch to constructor-arg in applicationContext.xml to test)
        // -----------------------------------------------------------------
        logger.info("--- Demonstrating addBook with empty title (warn path) ---");
        bookService.addBook("");

        logger.info("=== Library Management System Finished ===");

        // Close context to release resources
        ((ClassPathXmlApplicationContext) context).close();
    }
}
