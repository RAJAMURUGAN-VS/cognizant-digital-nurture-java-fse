package com.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * BookControllerTest — integration tests for the REST API.
 *
 * @SpringBootTest loads the full application context.
 * @AutoConfigureMockMvc wires MockMvc so we can fire HTTP requests
 *   without starting a real server.
 *
 * Run with: mvn test
 */
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Clear and re-seed before each test for isolation
        bookRepository.deleteAll();
        bookRepository.save(new Book("Effective Java",   "Joshua Bloch",    "978-0134685991", 45.99));
        bookRepository.save(new Book("Clean Code",       "Robert C. Martin","978-0132350884", 35.99));
        bookRepository.save(new Book("Design Patterns",  "Gang of Four",    "978-0201633610", 55.00));
    }

    // ---------------------------------------------------------------
    // GET /api/books
    // ---------------------------------------------------------------
    @Test
    void getAllBooks_returnsAllSeededBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title", is("Effective Java")));
    }

    // ---------------------------------------------------------------
    // GET /api/books/{id}
    // ---------------------------------------------------------------
    @Test
    void getBookById_existingId_returnsBook() throws Exception {
        Long id = bookRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));
    }

    @Test
    void getBookById_nonExistentId_returns404() throws Exception {
        mockMvc.perform(get("/api/books/99999"))
                .andExpect(status().isNotFound());
    }

    // ---------------------------------------------------------------
    // GET /api/books/search?title=
    // ---------------------------------------------------------------
    @Test
    void searchByTitle_matchingKeyword_returnsResults() throws Exception {
        mockMvc.perform(get("/api/books/search").param("title", "java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Effective Java")));
    }

    @Test
    void searchByTitle_noMatch_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/books/search").param("title", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ---------------------------------------------------------------
    // GET /api/books/author?name=
    // ---------------------------------------------------------------
    @Test
    void getByAuthor_returnsMatchingBooks() throws Exception {
        mockMvc.perform(get("/api/books/author").param("name", "Joshua Bloch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is("Joshua Bloch")));
    }

    // ---------------------------------------------------------------
    // GET /api/books/price?min=&max=
    // ---------------------------------------------------------------
    @Test
    void getByPriceRange_returnsBooksInRange() throws Exception {
        mockMvc.perform(get("/api/books/price").param("min", "30").param("max", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));  // Effective Java (45.99) + Clean Code (35.99)
    }

    // ---------------------------------------------------------------
    // POST /api/books
    // ---------------------------------------------------------------
    @Test
    void createBook_validBook_returns201() throws Exception {
        Book newBook = new Book("The Pragmatic Programmer", "David Thomas",
                "978-0135957059", 42.50);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("The Pragmatic Programmer")));
    }

    @Test
    void createBook_duplicateIsbn_returns409() throws Exception {
        // ISBN already exists in seeded data
        Book duplicate = new Book("Another Java Book", "Someone",
                "978-0134685991", 20.00);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isConflict());
    }

    // ---------------------------------------------------------------
    // PUT /api/books/{id}
    // ---------------------------------------------------------------
    @Test
    void updateBook_existingId_returnsUpdatedBook() throws Exception {
        Long id = bookRepository.findAll().get(0).getId();
        Book updated = new Book("Effective Java 3rd Ed", "Joshua Bloch",
                "978-0134685991", 49.99);

        mockMvc.perform(put("/api/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Effective Java 3rd Ed")))
                .andExpect(jsonPath("$.price", is(49.99)));
    }

    @Test
    void updateBook_nonExistentId_returns404() throws Exception {
        Book updated = new Book("Title", "Author", "000-0000000000", 10.00);

        mockMvc.perform(put("/api/books/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    // ---------------------------------------------------------------
    // DELETE /api/books/{id}
    // ---------------------------------------------------------------
    @Test
    void deleteBook_existingId_returns200() throws Exception {
        Long id = bookRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));
    }

    @Test
    void deleteBook_nonExistentId_returns404() throws Exception {
        mockMvc.perform(delete("/api/books/99999"))
                .andExpect(status().isNotFound());
    }
}
