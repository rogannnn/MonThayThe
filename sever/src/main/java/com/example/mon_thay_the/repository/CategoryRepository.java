package com.example.mon_thay_the.repository;


import com.example.mon_thay_the.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Long countById(Integer id);

    Category findByName(String name);

    @Query("UPDATE Category c set c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer categoryId, boolean enabled);

    @Query("SELECT c FROM Category c WHERE CONCAT(c.id, ' ',c.name) LIKE %?1%")
    Page<Category> findAll(String keyword, Pageable pageable);


    Category getCategoriesByName(String name);

}
