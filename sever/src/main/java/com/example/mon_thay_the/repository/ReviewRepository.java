package com.example.mon_thay_the.repository;

import com.example.mon_thay_the.entity.Product;
import com.example.mon_thay_the.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByProduct(Product product);

    @Query("SELECT COUNT(r) FROM Review r WHERE WEEK(r.date) = WEEK(?1)")
    Long countTotalReviewByWeek(Date date);

}
