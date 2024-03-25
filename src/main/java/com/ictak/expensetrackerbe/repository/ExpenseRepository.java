package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

    @Query(value = "select sum(amount) from expenses where year(date)=year(now()) and month(date)=month(now()) and user_id = ?1 group by user_id",nativeQuery = true)
    Double getMonthlyExpense(int userId);

    @Query(value = "SELECT c.name, COALESCE(SUM(e.amount), 0) AS expense \n" +
                    "FROM categories c \n" +
                    "LEFT JOIN expenses e ON e.category = c.id AND e.user_id = ?1 \n" +
                    "AND year(date)=year(now()) AND month(date)=month(now()) \n" +
                    "GROUP BY c.id", nativeQuery = true)
    List<Map<String, Object>> getCategoricalExpense(int userId);
}
