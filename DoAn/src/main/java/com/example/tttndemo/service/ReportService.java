package com.example.tttndemo.service;

import com.example.tttndemo.repository.OrderRepository;
import com.example.tttndemo.repository.ReviewRepository;
import com.example.tttndemo.repository.UserRepository;
import com.example.tttndemo.utils.Items;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReportService(OrderRepository orderRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
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
}
