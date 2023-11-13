package com.example.tttndemo.entity;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column()
    private String description;

    @Column()
    private Boolean enabled = true;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Collection<Product> products;


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

    public Brand(Integer id) {
        this.id = id;
    }

    public Brand() {
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                ", products=" + products +
                '}';
    }
}
