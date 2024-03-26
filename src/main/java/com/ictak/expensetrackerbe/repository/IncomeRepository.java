package com.ictak.expensetrackerbe.repository;


import com.ictak.expensetrackerbe.dbmodels.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Integer> {
    @Query(value = "select sum(amount) from income where year(date)=year(now()) and month(date)=month(now()) and user_id = ?1 group by user_id",nativeQuery = true)
    Double getMonthlyIncome(int userId);

    @Query(value = "SELECT \n" +
            "    COALESCE(SUM(i.amount), 0) AS amount\n" +
            "FROM \n" +
            "    (\n" +
            "        SELECT 1 AS month_number\n" +
            "        UNION SELECT 2\n" +
            "        UNION SELECT 3\n" +
            "        UNION SELECT 4\n" +
            "        UNION SELECT 5\n" +
            "        UNION SELECT 6\n" +
            "        UNION SELECT 7\n" +
            "        UNION SELECT 8\n" +
            "        UNION SELECT 9\n" +
            "        UNION SELECT 10\n" +
            "        UNION SELECT 11\n" +
            "        UNION SELECT 12\n" +
            "    ) AS m\n" +
            "LEFT JOIN income i ON MONTH(i.date) = m.month_number AND YEAR(i.date) = ?2 AND user_id = ?1 \n" +
            "GROUP BY m.month_number\n" +
            "ORDER BY m.month_number", nativeQuery = true)
    List<Double> getIncome(int userId, int year);

    @Query(value = "WITH TRANS AS \n" +
            "(SELECT title, amount, modified_date, user_id, true AS isIncome FROM income\n" +
            "UNION ALL\n" +
            "SELECT title, amount, modified_date, user_id, false AS isIncome FROM expenses)\n" +
            "SELECT title, amount, isIncome\n" +
            "FROM TRANS\n" +
            "WHERE user_id = ?1\n" +
            "ORDER BY modified_date DESC\n" +
            "LIMIT 10", nativeQuery = true)
    List<Map<String, Object>> getRecentTransactions(int userId);
}
