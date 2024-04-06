package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

    @Query(value = "select sum(amount) from expenses where year(date)=year(now()) and month(date)=month(now()) and user_id = ?1 group by user_id",nativeQuery = true)
    Double getMonthlyExpense(int userId);
    @Query(value = "SELECT e.id, title, amount, date, IF(description='' OR description IS NULL,'NA',description) as description, c.name AS category \n" +
                    "FROM EXPENSES e \n" +
                    "JOIN categories c ON c.id = e.category AND user_id= ?1 \n" +
                    "ORDER BY e.modified_date DESC;",nativeQuery = true)
    List<Map<String, Object>> getAllExpensesOfUser(int userId);

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

    @Query(value = "WITH TRANS AS \n" +
            "(SELECT id, title, amount, date, IF(description='' OR description IS NULL,'NA',description) as description, \n" +
            "modified_date, user_id, true AS isIncome FROM income \n" +
            "UNION ALL \n" +
            "SELECT id, title, amount, date, IF(description='' OR description IS NULL,'NA',description) as description, \n" +
            "modified_date, user_id, false AS isIncome FROM expenses) \n" +
            "SELECT id, title, amount, date, description, isIncome \n" +
            "FROM TRANS \n" +
            "WHERE user_id = ?1 \n" +
            "ORDER BY modified_date DESC;", nativeQuery = true)
    List<Map<String, Object>> getAllTransactionsOfUser(int userId);
}
