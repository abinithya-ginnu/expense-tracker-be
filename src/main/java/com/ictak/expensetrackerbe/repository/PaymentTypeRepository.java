package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.PaymentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, Integer> {

    @Query(value = "SELECT * FROM PAYMENTTYPE",nativeQuery = true)
    List<PaymentTypeEntity> getPaymentTypes();
}
