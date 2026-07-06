package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

/**
 * StockRepository — Spring Data JPA repository for Stock.
 *
 * Hands-on 2: Query Methods on the stock table.
 *
 * All four scenarios use derived query methods.
 * Spring Data JPA translates the method name into JPQL/SQL at startup.
 *
 * Scenarios:
 *   1. Facebook stocks in Sep 2019  → findByCodeAndDateBetween
 *   2. Google close > 1250          → findByCodeAndCloseGreaterThan
 *   3. Top 3 by highest volume      → findTop3ByOrderByVolumeDesc
 *   4. Netflix lowest close (top 3) → findTop3ByCodeOrderByCloseAsc
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    /**
     * Hands-on 2a: Get all stocks for a given code between two dates (inclusive).
     *
     * Usage: findByCodeAndDateBetween("FB", LocalDate.of(2019,9,1), LocalDate.of(2019,9,30))
     *
     * Generated SQL:
     *   SELECT * FROM stock
     *   WHERE st_code = ? AND st_date BETWEEN ? AND ?
     */
    List<Stock> findByCodeAndDateBetween(String code, LocalDate startDate, LocalDate endDate);

    /**
     * Hands-on 2b: Google stocks where close price > threshold.
     *
     * Usage: findByCodeAndCloseGreaterThan("GOOGL", new BigDecimal("1250"))
     *
     * Generated SQL:
     *   SELECT * FROM stock
     *   WHERE st_code = ? AND st_close > ?
     */
    List<Stock> findByCodeAndCloseGreaterThan(String code, BigDecimal threshold);

    /**
     * Hands-on 2c: Top 3 stocks with the highest volume across all codes.
     *
     * 'Top3' limits results to 3 rows.
     * 'OrderByVolumeDesc' sorts by st_volume descending.
     *
     * Generated SQL:
     *   SELECT * FROM stock ORDER BY st_volume DESC LIMIT 3
     */
    List<Stock> findTop3ByOrderByVolumeDesc();

    /**
     * Hands-on 2d: Top 3 Netflix stocks with the lowest close price.
     *
     * 'Top3' limits results to 3 rows.
     * 'ByCode' filters by stock code.
     * 'OrderByCloseAsc' sorts by st_close ascending (lowest first).
     *
     * Generated SQL:
     *   SELECT * FROM stock WHERE st_code = ? ORDER BY st_close ASC LIMIT 3
     */
    List<Stock> findTop3ByCodeOrderByCloseAsc(String code);
}
