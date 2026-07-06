package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CountryRepository — Spring Data JPA repository for Country.
 *
 * Hands-on 1: Query Methods on the country table.
 *
 * Spring Data JPA derives SQL from method names automatically.
 * No @Query annotation or SQL needed — just the right method name.
 *
 * Query method naming conventions used here:
 *   findBy<Field>Containing      → WHERE co_name LIKE '%value%'
 *   findBy<Field>ContainingOrderBy<Field>Asc → ... ORDER BY co_name ASC
 *   findBy<Field>StartingWith    → WHERE co_name LIKE 'value%'
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    /**
     * Hands-on 1a: Search countries whose name contains the keyword.
     * E.g. "ou" → Bouvet Island, Djibouti, Luxembourg, South Sudan ...
     *
     * Generated SQL: SELECT * FROM country WHERE co_name LIKE '%ou%'
     */
    List<Country> findByNameContaining(String keyword);

    /**
     * Hands-on 1b: Same search but results sorted ascending by name.
     * Adding 'OrderByNameAsc' to the method name adds ORDER BY co_name ASC.
     *
     * Generated SQL: SELECT * FROM country WHERE co_name LIKE '%ou%' ORDER BY co_name ASC
     */
    List<Country> findByNameContainingOrderByNameAsc(String keyword);

    /**
     * Hands-on 1c: Countries whose name starts with the given letter/text.
     * E.g. 'Z' → Zambia, Zimbabwe
     *
     * Generated SQL: SELECT * FROM country WHERE co_name LIKE 'Z%'
     */
    List<Country> findByNameStartingWith(String prefix);
}
