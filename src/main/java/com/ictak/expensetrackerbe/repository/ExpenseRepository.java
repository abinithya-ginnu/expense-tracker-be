package com.ictak.expensetrackerbe.repository;
import com.ictak.expensetrackerbe.dbmodels.PaymentTypeEntity;
import org.springframework.stereotype.Repository;
import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    @Query(value = "SELECT * FROM EXPENSES where user_id=?1",nativeQuery = true)
    List<ExpenseEntity> getExpenses(int userId);

}
