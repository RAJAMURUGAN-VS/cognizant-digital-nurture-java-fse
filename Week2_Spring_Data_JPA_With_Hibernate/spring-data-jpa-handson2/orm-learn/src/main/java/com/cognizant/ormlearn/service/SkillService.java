package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Skill;
import com.cognizant.ormlearn.repository.SkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SkillService — service layer for Skill operations.
 * Hands-on 6.
 */
@Service
public class SkillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillService.class);

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Fetch skill by id.
     */
    @Transactional
    public Skill get(int id) {
        LOGGER.info("Start get(id={})", id);
        return skillRepository.findById(id).get();
    }

    /**
     * Persist or update a skill.
     */
    @Transactional
    public void save(Skill skill) {
        LOGGER.info("Start save");
        skillRepository.save(skill);
        LOGGER.info("End save, skill={}", skill);
    }
}
