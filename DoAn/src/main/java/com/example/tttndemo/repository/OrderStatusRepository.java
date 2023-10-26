package com.example.tttndemo.repository;

import com.example.tttndemo.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    @Query("SELECT os FROM OrderStatus os where  os.id = ?1")
    public OrderStatus getOrderStatusById(int id);

    Long countById(Integer id);

    @Query("SELECT os FROM OrderStatus os ORDER BY  os.id")
    List<OrderStatus> getAll();
}
