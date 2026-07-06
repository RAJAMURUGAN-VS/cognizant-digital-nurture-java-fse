package com.cognizant.springlearn.service;

import com.cognizant.springlearn.dao.CountryDao;
import com.cognizant.springlearn.model.Country;
import com.cognizant.springlearn.service.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CountryService — service layer for all Country REST operations.
 */
@Service
public class CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    @Autowired
    private CountryDao countryDao;

    @Transactional(readOnly = true)
    public List<Country> getAllCountries() {
        LOGGER.info("START getAllCountries");
        List<Country> list = countryDao.getAllCountries();
        LOGGER.info("END getAllCountries — returning {}", list.size());
        return list;
    }

    @Transactional(readOnly = true)
    public Country getCountry(String code) throws CountryNotFoundException {
        LOGGER.info("START getCountry({})", code);
        Country c = countryDao.getCountry(code);
        LOGGER.info("END getCountry");
        return c;
    }

    @Transactional
    public Country addCountry(Country country) {
        LOGGER.info("START addCountry: {}", country);
        countryDao.addCountry(country);
        LOGGER.info("END addCountry");
        return country;
    }

    @Transactional
    public Country updateCountry(Country country) throws CountryNotFoundException {
        LOGGER.info("START updateCountry: {}", country);
        countryDao.updateCountry(country);
        LOGGER.info("END updateCountry");
        return country;
    }

    @Transactional
    public void deleteCountry(String code) throws CountryNotFoundException {
        LOGGER.info("START deleteCountry({})", code);
        countryDao.deleteCountry(code);
        LOGGER.info("END deleteCountry");
    }
}
