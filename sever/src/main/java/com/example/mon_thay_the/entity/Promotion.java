package com.example.mon_thay_the.entity;


import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;

@Table(name = "promotions")
@Entity
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name="start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name="finish_date", nullable = false)
    private Date finishDate;


    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "promotion")
    private Collection<PromotionDetail> promotionDetails;

    public Promotion(Date startDate, Date finishDate) {
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public Promotion() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<PromotionDetail> getPromotionDetails() {
        return promotionDetails;
    }

    public void setPromotionDetails(Collection<PromotionDetail> promotionDetails) {
        this.promotionDetails = promotionDetails;
    }
}
