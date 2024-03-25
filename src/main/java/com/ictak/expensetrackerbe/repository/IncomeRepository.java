package com.ictak.expensetrackerbe.repository;


import com.ictak.expensetrackerbe.dbmodels.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Integer> {
    @Query(value = "select sum(amount) from income where year(date)=year(now()) and month(date)=month(now()) and user_id = ?1 group by user_id",nativeQuery = true)
    Double getMonthlyIncome(int userId);
}
