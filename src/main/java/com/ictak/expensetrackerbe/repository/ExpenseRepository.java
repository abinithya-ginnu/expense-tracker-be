package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {


}
