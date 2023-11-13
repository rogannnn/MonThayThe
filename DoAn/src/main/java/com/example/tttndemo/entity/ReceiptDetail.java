package com.example.tttndemo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Entity
@Table(name = "receipt_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Float unitPrice;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

}
