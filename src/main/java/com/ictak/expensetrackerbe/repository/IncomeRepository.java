package com.ictak.expensetrackerbe.repository;


import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import com.ictak.expensetrackerbe.dbmodels.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeEntity, Integer> {

    @Query(value = "SELECT * FROM INCOMES where user_id=?1", nativeQuery = true)
    List<IncomeEntity> getIncomes(int userId);




}
