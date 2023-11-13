package com.example.tttndemo.service;

import com.example.tttndemo.repository.*;
import com.example.tttndemo.utils.ItemProduct;
import com.example.tttndemo.utils.Items;
import com.example.tttndemo.utils.RevenueItem;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReceiptDetailRepository receiptDetailRepository;



    public ReportService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ReviewRepository reviewRepository, UserRepository userRepository, ReceiptDetailRepository receiptDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.receiptDetailRepository = receiptDetailRepository;
    }


    public long countOrderByWeek(Date date) {
        return orderRepository.countOrderByWeek(date);
    }

    public Long countTotalOrderByWeek(Date date) {
        return orderRepository.countTotalOrderByWeek(date);
    }

    public Long countUserByWeek(Date date) {
        return userRepository.counUserInWeek(date);
    }

    public Long countReviewByWeek(Date date) {
        return reviewRepository.countTotalReviewByWeek(date);
    }

    public List<Items> reportEarn() {
        List<Items> list = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            Date d = subDays(new Date(), i);
            Items myItem = new Items();
            myItem.setName(convertDayToString(d));
            myItem.setValue((long) orderRepository.totalEarnByDate(d));
            list.add(myItem);
        }
        return list;
    }

    public Date subDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    private String convertDayToString(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }

    public List<RevenueItem> reportRevenue(Date startDate, Date finishDate) {
        List<RevenueItem> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (Date currentDate = startDate; !currentDate.after(finishDate); currentDate = addDays(currentDate, 1)) {
            RevenueItem myItem = new RevenueItem();
            myItem.setDate(dateFormat.format(currentDate));
            myItem.setTotalRevenue(orderRepository.totalEarnByDate(currentDate));
            if(myItem.getTotalRevenue() != 0)
            {
                List<ItemProduct> productList = orderDetailRepository.getListProductSoldInDay(currentDate);

                long total = 0l;
                for(ItemProduct item : productList){
                    total += item.getQuantity() * receiptDetailRepository.findAverageUnitPriceByProductId(item.getProductId());
                }
                myItem.setTotalFund(total);
                myItem.setTotalSold(orderDetailRepository.getTotalProductSoldInDate(currentDate));
                myItem.setTotalProfit(myItem.getTotalRevenue() - myItem.getTotalFund());
                list.add(myItem);
            }
        }
        return list;
    }

    private Date addDays(Date date, int days) {
        long timeInMillis = date.getTime() + days * 24 * 60 * 60 * 1000L;
        return new Date(timeInMillis);
    }
}
