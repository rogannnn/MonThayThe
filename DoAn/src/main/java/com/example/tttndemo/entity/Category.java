package com.example.tttndemo.entity;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 300)
    private String images;

    @Column()
    private String description;

    @Column()
    private Boolean enabled = true;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Collection<Product> products;


    public Category(String name) {
        this.name = name;
    }

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {

    }

    public Category(String name, String images, String description) {
        this.name = name;
        this.images = images;
        this.description = description;
    }

    public Category(String name, String images, String description, Boolean enabled) {
        this.name = name;
        this.images = images;
        this.description = description;
        this.enabled = enabled;
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

    public String getImages() {
        return images;
    }

    public void setImages(String image) {
        this.images = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Product> getProducts() {
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + images + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
