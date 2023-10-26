package com.example.tttndemo.repository;




import com.example.tttndemo.entity.Product;
import com.example.tttndemo.utils.TopSellingProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


//    @Query("SELECT p FROM Product p WHERE CONCAT(p.name,' ',p.price, ' '," +
//            "p.category.name) LIKE %?1% and p.enabled = true")

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% "
            + "OR p.description LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);

    @Query("SELECT COUNT(p.id) from Product p WHERE p.id = :id")
    public Long countById(Integer id);
    @Query("SELECT p FROM Product p WHERE CONCAT(p.name,' ',p.category.name, ' ',p.brand.name) LIKE %?1% AND p.enabled = true ")
    Page<Product> findAllByKeyword(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 AND p.enabled = true ")
    Page<Product> findAllByCategory(Integer CategoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.brand.id = ?1 AND p.enabled = true ")
    Page<Product> findAllByBrand(Integer brandId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 AND p.brand.id = ?2 AND p.enabled = true ")
    Page<Product> findAllByCategoryAndBrand(Integer CategoryId, Integer brandId, Pageable pageable);

    Product getProductByName(String name);

    @Procedure("GetTopSellingProductsAllTime")
    List<Product> getTopSellingProducts(int limit);

    @Query("SELECT p FROM Product p WHERE p.enabled = true ")
    List<Product> findAllProductAvaiable();
    @Query("SELECT p FROM Product p WHERE p.enabled = true ")
    Page<Product> findAllProductAvaiable1(Pageable pageable);

    @Query("SELECT COUNT(p) from Product p WHERE p.category.id = ?1")
    Long countProductByCategory(Integer categoryId);

    @Query("SELECT COUNT(p) from Product p WHERE p.brand.id = ?1")
    Long countProductByBrand(Integer brandId);


}
