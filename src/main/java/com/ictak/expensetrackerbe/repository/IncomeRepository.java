package com.ictak.expensetrackerbe.repository;


import com.ictak.expensetrackerbe.dbmodels.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Integer> {

}
