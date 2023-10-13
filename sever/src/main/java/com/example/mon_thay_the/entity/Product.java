package com.example.mon_thay_the.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name ="price", nullable = false)
    private Double price;

    @Column(name ="in_stock", nullable = false)
    private Integer inStock;

    @Column(name ="sold_quantity")
    private Integer soldQuantity = 0;


    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="dd/MM/yyyy")
    @JsonIgnore
    @Column(name="created_at", nullable = false)
    private Date createdAt;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled ;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Collection<Review> reviews;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "brand_id")
    private Brand brand;


    @OneToMany(mappedBy = "product")
    private Collection<PromotionDetail> promotionDetails;


    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ImageProduct> imageProducts;

    public Product(String name, String description, Double price, Integer inStock, Integer soldQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.inStock = inStock;
        this.soldQuantity = soldQuantity;
//        this.createdAt = createdAt;
//        this.category = category;
    }

    public Product() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Collection<PromotionDetail> getPromotionDetails() {
        return promotionDetails;
    }

    public void setPromotionDetails(Collection<PromotionDetail> promotionDetails) {
        this.promotionDetails = promotionDetails;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Collection<Review> reviews) {
        this.reviews = reviews;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Collection<ImageProduct> getImageProducts() {
        return imageProducts;
    }

    public void setImageProducts(List<ImageProduct> imageProducts) {
        this.imageProducts = imageProducts;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", inStock=" + inStock +
                ", soldQuantity=" + soldQuantity +
                ", enabled=" + enabled +
                '}';
    }

    @Transient
    public String getURI(){
        return "/products/" + this.getId();
    }
}

