package com.example.tttndemo.repository;




import com.example.tttndemo.entity.Product;
import com.example.tttndemo.utils.TopSellingProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
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
    Product getProductByName(String name);

    @Procedure("GetTopSellingProductsAllTime")
    List<Product> getTopSellingProducts(int limit);

    @Query("SELECT p FROM Product p WHERE p.enabled = true ")
    List<Product> findAllProductAvaiable();
    @Query("SELECT p FROM Product p WHERE p.enabled = true ")
    Page<Product> findAllProductPageAvaiable(Pageable pageable);

    @Query("SELECT COUNT(p) from Product p WHERE p.category.id = ?1")
    Long countProductByCategory(Integer categoryId);

    @Query("SELECT COUNT(p) from Product p WHERE p.brand.id = ?1")
    Long countProductByBrand(Integer brandId);

    @Query("SELECT p FROM Product p WHERE CONCAT(p.name,' ',p.category.name, ' ',p.brand.name) LIKE %?1% AND p.enabled = true ")
    Page<Product> findAllByKeyword(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 AND p.enabled = true ")
    Page<Product> findAllByCategory(Integer CategoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.brand.id = ?1 AND p.enabled = true ")
    Page<Product> findAllByBrand(Integer brandId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 AND p.brand.id = ?2 AND p.enabled = true ")
    Page<Product> findAllByCategoryAndBrand(Integer CategoryId, Integer brandId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 AND p.brand.id = ?2 AND p.price BETWEEN ?3 AND ?4")
    Page<Product> findAllByCategoryAndBrandAndPrice(Integer categoryId, Integer brandId, Integer minPrice
            , Integer maxPrice, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.category.id = ?1  AND p.price BETWEEN ?2 AND ?3")
    Page<Product> findAllByCategoryAndPrice(Integer categoryId, Integer minPrice, Integer maxPrice, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.brand.id = ?1  AND p.price BETWEEN ?2 AND ?3")
    Page<Product> findAllByBrandAndPrice(Integer brandId, Integer minPrice, Integer maxPrice, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2")
    Page<Product> findAllByPrice(Integer minPrice, Integer maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.enabled = true ORDER BY p.id DESC")
    List<Product> findAllProductAvaiableDESC();

    @Query("SELECT new com.example.tttndemo.utils.TopSellingProduct(p.id, p.name, SUM(od.quantity) AS totalSoldQuantity)" +
            "FROM Product p " +
            "JOIN OrderDetail od ON p.id = od.product.id " +
            "JOIN Order o ON od.order.id = o.id " +
            "WHERE o.date BETWEEN :startDate AND :endDate AND o.orderStatus.id = 4 " +
            "GROUP BY p.id " +
            "ORDER BY totalSoldQuantity DESC")
    List<TopSellingProduct> getTopSellingProducts(@Param("startDate") Date startDate,
                                                  @Param("endDate")  Date endDate,
                                                  Pageable pageable);
}
