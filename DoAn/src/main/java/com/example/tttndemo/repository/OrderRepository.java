package com.example.tttndemo.repository;


import com.example.tttndemo.entity.Order;
import com.example.tttndemo.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer > {
    @Query("SELECT o FROM Order o WHERE o.id = ?1")
    Order findByOrderId(Integer id);
    @Query("SELECT o FROM Order o WHERE o.user.id = ?1 ORDER BY o.id desc")
    List<Order> findByUserId(Integer userId);

    @Query("Select o from OrderDetail o WHERE  o.order.id = ?1")
    public List<OrderDetail> getOrderDetail(Integer id);
    @Query("SELECT o FROM Order o WHERE o.user.id = ?1 AND o.orderStatus.id = ?2 ORDER BY o.id desc")
    List<Order> findByOrderStatus(Integer userId, Integer orderStatusId);

    @Query("SELECT COUNT(o) FROM Order o WHERE WEEK(o.date) = WEEK(?1) AND o.orderStatus.id = 1")
    Long countOrderByWeek(Date date);

    @Query("SELECT COUNT(o) FROM Order o WHERE WEEK(o.date) = WEEK(?1)")
    Long countTotalOrderByWeek(Date date);

    @Query("SELECT COALESCE(sum(o.totalPrice),0) FROM Order o WHERE o.date = ?1 AND o.orderStatus.id = 4")
    Long totalEarnByDate(Date d);

    @Query("SELECT o FROM Order o WHERE o.date >= ?1")
    Page<Order> findAdminSinceDate(Date startDate, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.date < ?1")
    Page<Order> findAdminBeforDate(Date endDate, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderStatus.id = ?1")
    Page<Order> findAdminByStatus(Integer orderStatus, Pageable pageable);

}