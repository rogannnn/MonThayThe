package com.example.tttndemo.repository;

import com.example.tttndemo.entity.OrderDetail;
import com.example.tttndemo.utils.ItemProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {


    @Query("SELECT o FROM OrderDetail o WHERE o.order.id = ?1")
    List<OrderDetail> getOrderDetailByOrder(Integer orderId);


    @Query("SELECT SUM(od.quantity)" +
            "FROM OrderDetail od " +
            "JOIN Order o ON od.order.id = o.id " +
            "WHERE o.date = :date_time AND o.orderStatus.id = 4")
    Integer getTotalProductSoldInDate(@Param("date_time")Date date_time);
    @Query("SELECT new com.example.tttndemo.utils.ItemProduct(od.product.id, od.quantity)" +
            "FROM OrderDetail od " +
            "JOIN Order o ON od.order.id = o.id " +
            "WHERE o.date = :date_time AND o.orderStatus.id = 4")
    List<ItemProduct> getListProductSoldInDay(@Param("date_time")Date date_time);
}
