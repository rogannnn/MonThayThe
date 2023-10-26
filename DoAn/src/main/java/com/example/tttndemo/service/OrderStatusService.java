package com.example.tttndemo.service;

import com.example.tttndemo.entity.Category;
import com.example.tttndemo.entity.Order;
import com.example.tttndemo.entity.OrderStatus;
import com.example.tttndemo.exception.CategoryNotFoundException;
import com.example.tttndemo.exception.OrderNotFoundException;
import com.example.tttndemo.exception.OrderStatusNotFoundException;
import com.example.tttndemo.repository.OrderStatusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderStatusService {
    public static final int ORDERSTATUS_PER_PAGE = 10;

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    public List<OrderStatus> listOrderStatus() {
        return orderStatusRepository.getAll();
    }

    public OrderStatus saveOrderStatus(OrderStatus orderStatus){
        return orderStatusRepository.save(orderStatus);
    }


    public Page<OrderStatus> listByPage(Integer pageNumber, String sortDir, String sortField){

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber-1, ORDERSTATUS_PER_PAGE,sort);
        return orderStatusRepository.findAll(pageable);
    }

    public void deleteOrderStatus(Integer id) throws OrderNotFoundException {
        Long countById = orderStatusRepository.countById(id);
        if(countById == null || countById == 0 ){
            throw new OrderNotFoundException("Could not found order status with id :" + id);
        }
        orderStatusRepository.deleteById(id);
    }

    public OrderStatus getOrderStatusById(Integer id) throws OrderStatusNotFoundException {
        OrderStatus orderStatus = orderStatusRepository.findById(id).get();
        if(orderStatus == null ){
            throw new OrderStatusNotFoundException("Could not found order status with id :" + id);
        }
        return orderStatus;
    }


}
