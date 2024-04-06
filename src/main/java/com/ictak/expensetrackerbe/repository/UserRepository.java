package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query(value = "SELECT * FROM users WHERE email=?1  and password=?2 ",nativeQuery = true)
    UserEntity login(String email, String password);

    @Query(value = "SELECT * FROM users WHERE email=?1", nativeQuery = true)
    UserEntity emailExists(String email);

    @Query(value = "SELECT * FROM users WHERE id=?1", nativeQuery = true)
    UserEntity getByUserId(int userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE users \n" +
            "SET name = ?1, \n" +
            "password = ?2, \n" +
            "email = ?3 \n" +
            "WHERE id = ?4", nativeQuery = true)
    void updateProfile(String name, String password, String email, int userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE users \n" +
            "SET password = ?1 \n" +
            "WHERE email = ?2", nativeQuery = true)
    void resetPassword(String password, String email);
}
