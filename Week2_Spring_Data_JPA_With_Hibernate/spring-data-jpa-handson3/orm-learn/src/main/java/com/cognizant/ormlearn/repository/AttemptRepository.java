package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.quiz.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * AttemptRepository — HQL for fetching full quiz attempt details.
 *
 * Hands-on 3: The HQL joins the following tables in order:
 *   user → attempt → attempt_question → question → attempt_option → options
 *
 * 'LEFT JOIN FETCH' is used wherever one-to-many or many-to-many associations
 * exist so that all nested data is populated in a SINGLE SQL query.
 *
 * HQL uses entity class names and field names (NOT table/column names):
 *   Attempt         → at_id, at_us_id, at_attempted_on
 *   AttemptQuestion → aq_id, aq_at_id, aq_qu_id
 *   AttemptOption   → ao_id, ao_aq_id, ao_op_id, ao_selected
 */
@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Integer> {

    /**
     * Fetch full attempt detail for a given user and attempt id.
     *
     * Join order:
     *   a.user                              — ManyToOne (user)
     *   a.attemptQuestions aq               — OneToMany FETCH (attempt_question)
     *   aq.question q                       — ManyToOne (question)
     *   q.options                           — OneToMany FETCH (options)
     *   aq.attemptOptions ao                — OneToMany FETCH (attempt_option)
     *   ao.option                           — ManyToOne (options)
     *
     * WHERE clause filters by user id and attempt id.
     *
     * @param userId    the id of the user who made the attempt
     * @param attemptId the id of the specific attempt
     * @return the Attempt with all nested data populated
     */
    @Query(value =
        "SELECT DISTINCT a FROM Attempt a " +
        "JOIN FETCH a.user u " +
        "LEFT JOIN FETCH a.attemptQuestions aq " +
        "LEFT JOIN FETCH aq.question q " +
        "LEFT JOIN FETCH q.options " +
        "LEFT JOIN FETCH aq.attemptOptions ao " +
        "LEFT JOIN FETCH ao.option " +
        "WHERE u.id = :userId AND a.id = :attemptId")
    Attempt getAttempt(@Param("userId") int userId, @Param("attemptId") int attemptId);
}
