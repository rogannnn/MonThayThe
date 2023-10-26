package com.example.tttndemo.repository;

import com.example.tttndemo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getUserByEmail(String email);

    Long countById(Integer id);

    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ',u.email,' ', u.firstName, ' '," +
            " u.lastName) LIKE %?1%")
    Page<User> findAll(String keyWord, Pageable pageable);

    @Query("SELECT COUNT(u) FROM  User u WHERE WEEK(u.registrationDate) = WEEK(?1)")
    Long counUserInWeek(Date date);
}
