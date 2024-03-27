package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

    @Query(value = "select sum(amount) from expenses where year(date)=year(now()) and month(date)=month(now()) and user_id = ?1 group by user_id",nativeQuery = true)
    Double getMonthlyExpense(int userId);
    @Query(value = "SELECT * FROM EXPENSES where user_id=?1",nativeQuery = true)
    List<ExpenseEntity> getExpenses(int userId);

    @Query(value = "SELECT c.name, COALESCE(SUM(e.amount), 0) AS expense \n" +
                    "FROM categories c \n" +
                    "LEFT JOIN expenses e ON e.category = c.id AND e.user_id = ?1 \n" +
                    "AND year(date)=year(now()) AND month(date)=month(now()) \n" +
                    "GROUP BY c.id", nativeQuery = true)
    List<Map<String, Object>> getCategoricalExpense(int userId);

    @Query(value = "SELECT \n" +
            "    COALESCE(SUM(e.amount), 0) AS amount\n" +
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
            "LEFT JOIN expenses e ON MONTH(e.date) = m.month_number AND YEAR(e.date) = ?2 AND user_id = ?1 \n" +
            "GROUP BY m.month_number\n" +
            "ORDER BY m.month_number", nativeQuery = true)
    List<Double> getExpenses(int userId, int year);
}
