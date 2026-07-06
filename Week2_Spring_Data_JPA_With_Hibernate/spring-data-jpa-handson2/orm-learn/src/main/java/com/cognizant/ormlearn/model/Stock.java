package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Stock — JPA entity mapped to the 'stock' table.
 *
 * Hands-on 2: Query Methods on stock data.
 *
 * Table DDL:
 *   CREATE TABLE stock (
 *       st_id     INT NOT NULL AUTO_INCREMENT,
 *       st_code   VARCHAR(10),
 *       st_date   DATE,
 *       st_open   NUMERIC(10,2),
 *       st_close  NUMERIC(10,2),
 *       st_volume NUMERIC,
 *       PRIMARY KEY (st_id)
 *   );
 */
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "st_id")
    private int id;

    @Column(name = "st_code")
    private String code;

    @Column(name = "st_date")
    private LocalDate date;

    @Column(name = "st_open")
    private BigDecimal open;

    @Column(name = "st_close")
    private BigDecimal close;

    @Column(name = "st_volume")
    private long volume;

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    public Stock() {}

    // ---------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------

    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }

    public String getCode()              { return code; }
    public void setCode(String code)     { this.code = code; }

    public LocalDate getDate()           { return date; }
    public void setDate(LocalDate date)  { this.date = date; }

    public BigDecimal getOpen()          { return open; }
    public void setOpen(BigDecimal open) { this.open = open; }

    public BigDecimal getClose()             { return close; }
    public void setClose(BigDecimal close)   { this.close = close; }

    public long getVolume()              { return volume; }
    public void setVolume(long volume)   { this.volume = volume; }

    @Override
    public String toString() {
        return String.format("Stock{code='%s', date=%s, open=%s, close=%s, volume=%d}",
                code, date, open, close, volume);
    }
}
