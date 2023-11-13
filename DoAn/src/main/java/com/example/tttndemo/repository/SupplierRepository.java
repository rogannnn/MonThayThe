package com.example.tttndemo.repository;

import com.example.tttndemo.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findByEmail(String email);

    Supplier findByName(String name);
}
