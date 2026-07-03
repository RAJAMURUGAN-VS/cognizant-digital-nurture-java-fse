package com.library.config;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DataInitializer — seeds the H2 in-memory database with sample books on startup.
 *
 * CommandLineRunner runs after the Spring context is fully loaded.
 * This gives us testable data without needing a separate SQL script.
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner seedDatabase(BookRepository bookRepository) {
        return args -> {
            logger.info("Seeding database with sample books...");

            bookRepository.save(new Book("Effective Java",
                    "Joshua Bloch", "978-0134685991", 45.99));

            bookRepository.save(new Book("Clean Code",
                    "Robert C. Martin", "978-0132350884", 35.99));

            bookRepository.save(new Book("Design Patterns",
                    "Gang of Four", "978-0201633610", 55.00));

            bookRepository.save(new Book("The Pragmatic Programmer",
                    "David Thomas", "978-0135957059", 42.50));

            bookRepository.save(new Book("Spring in Action",
                    "Craig Walls", "978-1617294945", 49.99));

            bookRepository.save(new Book("Java: The Complete Reference",
                    "Herbert Schildt", "978-1260440232", 60.00));

            long count = bookRepository.count();
            logger.info("Database seeded successfully — {} books available", count);
            logger.info("API ready at http://localhost:8080/api/books");
            logger.info("H2 Console at http://localhost:8080/h2-console  (JDBC URL: jdbc:h2:mem:librarydb)");
        };
    }
}
