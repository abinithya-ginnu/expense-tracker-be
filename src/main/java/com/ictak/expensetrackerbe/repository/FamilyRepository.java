package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.FamilyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface FamilyRepository extends JpaRepository<FamilyEntity, Integer> {
    @Query(value = "SELECT id FROM family WHERE code=?1", nativeQuery = true)
    Integer codeExists(String code);

    @Query(value = "SELECT u.id as userId, email, name, code, is_admin as isAdmin \n" +
            "FROM users u\n" +
            "JOIN family f ON f.id = u.family_id \n" +
            "WHERE f.id = ?1", nativeQuery = true)
    List<Map<String, Object>> getFamilyDetails(int familyId);
}
