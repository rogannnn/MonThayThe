package com.example.mon_thay_the.repository;

import com.example.mon_thay_the.entity.CartItem;
import com.example.mon_thay_the.entity.Product;
import com.example.mon_thay_the.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findCartItemByUser(User user);
    @Query("SELECT c FROM CartItem c WHERE c.user.id = ?1")
    List<CartItem> findCartItemByUserId(Integer userId);

    CartItem findByUserAndProduct(User user, Product product);

    Long countByUser(User user);

    @Query("UPDATE CartItem c SET c.quantity = ?1 where c.product.id = ?2 AND c.user.id = ?3")
    @Modifying
    void updateQuantity(Integer quantity, Integer productId, Integer userId);

    @Query("DELETE FROM CartItem  c WHERE c.user.id = ?2 AND c.product.id = ?1")
    @Modifying
    void deleteCartItemByUserAndUser(Integer productId, Integer userId);
}
