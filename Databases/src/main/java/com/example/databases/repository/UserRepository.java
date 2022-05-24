package com.example.databases.repository;


import com.example.databases.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //@Query("Select t from User t where t.email = ?1")

    @Query("Select user from User user")
    List<User> findAll();

    Optional<User> findByEmail(String email);

    User findOneById(int id);


    User findUserByEmail(String email);



}
