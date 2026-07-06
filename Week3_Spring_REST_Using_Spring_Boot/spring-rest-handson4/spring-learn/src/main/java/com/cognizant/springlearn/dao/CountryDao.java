package com.cognizant.springlearn.dao;

import com.cognizant.springlearn.model.Country;
import com.cognizant.springlearn.service.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * CountryDao — DAO for Country CRUD operations backed by country.xml.
 */
@Repository
public class CountryDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryDao.class);
    private static List<Country> COUNTRY_LIST;

    public CountryDao() {
        LOGGER.info("START CountryDao constructor");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("country.xml");
        @SuppressWarnings("unchecked")
        List<Country> list = ctx.getBean("countryList", List.class);
        COUNTRY_LIST = new ArrayList<>(list);
        ((ClassPathXmlApplicationContext) ctx).close();
        LOGGER.debug("Loaded {} countries", COUNTRY_LIST.size());
        LOGGER.info("END CountryDao constructor");
    }

    public List<Country> getAllCountries() {
        return new ArrayList<>(COUNTRY_LIST);
    }

    public Country getCountry(String code) throws CountryNotFoundException {
        return COUNTRY_LIST.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new CountryNotFoundException(code));
    }

    public void addCountry(Country country) {
        LOGGER.info("addCountry: {}", country);
        COUNTRY_LIST.add(country);
    }

    public void updateCountry(Country country) throws CountryNotFoundException {
        LOGGER.info("updateCountry: {}", country);
        for (int i = 0; i < COUNTRY_LIST.size(); i++) {
            if (COUNTRY_LIST.get(i).getCode().equalsIgnoreCase(country.getCode())) {
                COUNTRY_LIST.set(i, country);
                return;
            }
        }
        throw new CountryNotFoundException(country.getCode());
    }

    public void deleteCountry(String code) throws CountryNotFoundException {
        LOGGER.info("deleteCountry: {}", code);
        boolean removed = COUNTRY_LIST.removeIf(c -> c.getCode().equalsIgnoreCase(code));
        if (!removed) throw new CountryNotFoundException(code);
    }
}
