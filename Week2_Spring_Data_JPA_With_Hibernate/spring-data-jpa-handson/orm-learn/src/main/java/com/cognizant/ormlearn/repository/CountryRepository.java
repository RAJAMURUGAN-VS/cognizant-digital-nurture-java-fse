package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CountryRepository — Spring Data JPA repository for Country entities.
 *
 * Hands-on 1:
 *   Extending JpaRepository<Country, String> provides all CRUD methods
 *   automatically — no implementation needed:
 *     findAll(), findById(), save(), deleteById(), count(), etc.
 *
 * Hands-on 5 (search by partial name):
 *   Spring Data derives the SQL query from the method name:
 *     findByNameContaining("ind") → SELECT * FROM country WHERE co_name LIKE '%ind%'
 *
 * @Repository registers this as a Spring bean and enables exception translation.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    /**
     * Hands-on 5: Find countries whose name contains the given keyword.
     * Case-sensitive match using LIKE '%keyword%'.
     *
     * Spring Data JPA generates the query from the method name automatically.
     */
    List<Country> findByNameContaining(String keyword);

    /**
     * Find countries whose name contains the keyword, ignoring case.
     * Useful for user-facing search where case shouldn't matter.
     */
    List<Country> findByNameContainingIgnoreCase(String keyword);
}
