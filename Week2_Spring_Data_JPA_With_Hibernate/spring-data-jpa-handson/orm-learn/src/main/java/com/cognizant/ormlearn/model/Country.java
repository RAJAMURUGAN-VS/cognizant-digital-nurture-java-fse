package com.cognizant.ormlearn.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Country — JPA persistence class mapped to the 'country' table.
 *
 * Hands-on 1 / Exercise 1:
 *   @Entity  — tells Spring Data JPA this is a managed persistent entity.
 *   @Table   — maps this class to the 'country' database table.
 *   @Id      — marks the primary key field.
 *   @Column  — maps each field to its database column by name.
 *
 * Table DDL (run in MySQL Workbench):
 *   CREATE TABLE country (
 *       co_code VARCHAR(2) PRIMARY KEY,
 *       co_name VARCHAR(50) NOT NULL
 *   );
 */
@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "co_code")
    private String code;

    @Column(name = "co_name")
    private String name;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    public Country() {
    }

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // ---------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ---------------------------------------------------------------
    // toString
    // ---------------------------------------------------------------

    @Override
    public String toString() {
        return "Country{code='" + code + "', name='" + name + "'}";
    }
}
