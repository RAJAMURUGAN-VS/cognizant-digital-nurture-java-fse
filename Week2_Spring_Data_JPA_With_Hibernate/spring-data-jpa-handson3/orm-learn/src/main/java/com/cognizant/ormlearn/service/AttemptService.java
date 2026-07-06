package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.quiz.Attempt;
import com.cognizant.ormlearn.repository.AttemptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AttemptService — service layer for quiz attempt operations.
 * Hands-on 3.
 */
@Service
public class AttemptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttemptService.class);

    @Autowired
    private AttemptRepository attemptRepository;

    /**
     * Fetch full attempt detail including user, questions, options and selections.
     * The HQL in AttemptRepository uses LEFT JOIN FETCH to load all nested data
     * in a single SQL query.
     *
     * @param userId    id of the user who made the attempt
     * @param attemptId id of the specific attempt
     * @return fully populated Attempt entity
     */
    @Transactional(readOnly = true)
    public Attempt getAttempt(int userId, int attemptId) {
        LOGGER.info("Start getAttempt(userId={}, attemptId={})", userId, attemptId);
        Attempt attempt = attemptRepository.getAttempt(userId, attemptId);
        LOGGER.debug("Attempt fetched: {}", attempt);
        return attempt;
    }
}
