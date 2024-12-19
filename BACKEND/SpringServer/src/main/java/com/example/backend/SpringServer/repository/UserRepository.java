package com.example.backend.SpringServer.repository;

import com.example.backend.SpringServer.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT email FROM users WHERE email = :email", nativeQuery = true)
    List<String> checkUserEmail(@Param("email") String email);

    @Query(value = "SELECT password FROM users WHERE email = :email", nativeQuery = true)
    String checkUserPasswordByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    User GetUserDetailsByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO USERS(username, email, password, isic, school) VALUES (:username, :email, :password, :isic, :school)", nativeQuery = true)
    int registerNewUser(@Param("username") String username,
                        @Param("email") String email,
                        @Param("password") String password,
                        @Param("isic") String isic,
                        @Param("school") String school);

}
