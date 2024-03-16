package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.FamilyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FamilyRepository extends JpaRepository<FamilyEntity, Integer> {
    @Query(value = "SELECT id FROM family WHERE code=?1", nativeQuery = true)
    Integer codeExists(String code);
}
