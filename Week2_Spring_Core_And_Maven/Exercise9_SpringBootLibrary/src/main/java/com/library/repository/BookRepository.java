package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * BookRepository — Spring Data JPA repository for Book entities.
 *
 * Extending JpaRepository gives us CRUD operations for free:
 *   save(), findById(), findAll(), deleteById(), count(), etc.
 *
 * Spring Data JPA generates the implementation at runtime —
 * no need to write any SQL or DAO boilerplate.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Derived query method — Spring Data generates the SQL from the method name:
     * SELECT * FROM books WHERE author = ?
     */
    List<Book> findByAuthor(String author);

    /**
     * Case-insensitive title search using derived query.
     * SELECT * FROM books WHERE LOWER(title) LIKE LOWER('%keyword%')
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Custom JPQL query — find a book by its ISBN.
     */
    @Query("SELECT b FROM Book b WHERE b.isbn = :isbn")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);

    /**
     * Custom JPQL query — find books within a price range.
     */
    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :min AND :max ORDER BY b.price")
    List<Book> findByPriceRange(@Param("min") Double min, @Param("max") Double max);
}
