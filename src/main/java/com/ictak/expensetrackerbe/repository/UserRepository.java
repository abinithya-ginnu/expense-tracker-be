package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM user WHERE user_name=?1  and password=?2 ",nativeQuery = true)
    User userlogin(String userName, String password);
}
