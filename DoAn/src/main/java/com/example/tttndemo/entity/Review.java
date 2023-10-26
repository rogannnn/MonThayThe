package com.example.tttndemo.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm") // Định dạng ngày giờ
    @Column(name="datetime_column")
    private LocalDateTime date;

    @Column(name="comment", length = 300, nullable = false)
    private String comment;

    @Column(name="vote")
    private Integer vote;

    public Review() {
    }

    public Review(User user, Product product, String comment, Integer vote) {
        this.date = LocalDateTime.now();
        this.user = user;
        this.product = product;
        this.comment = comment;
        this.vote = vote;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Review{" +
                "user=" + user.getId() +
                ", product=" + product.getId() +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                ", vote=" + vote +
                '}';
    }
}
