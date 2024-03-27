package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT * FROM users WHERE email=?1  and password=?2 ",nativeQuery = true)
    UserEntity login(String email, String password);

    @Query(value = "SELECT * FROM users WHERE email=?1", nativeQuery = true)
    UserEntity emailExists(String email);



}
