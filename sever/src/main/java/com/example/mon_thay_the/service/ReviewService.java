package com.example.mon_thay_the.service;

import com.example.mon_thay_the.entity.Product;
import com.example.mon_thay_the.entity.Review;
import com.example.mon_thay_the.entity.User;
import com.example.mon_thay_the.repository.ProductRepository;
import com.example.mon_thay_the.repository.ReviewRepository;
import com.example.mon_thay_the.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Review newReview(Integer userId, Integer productId, String comment, Integer vote){
        User user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();
        Review review = new Review(user, product, comment, vote);
        return reviewRepository.save(review);
    }

    public List<Review> getListReviewByProduct(Product product){

        return reviewRepository.findAllByProduct(product);
    }
}
