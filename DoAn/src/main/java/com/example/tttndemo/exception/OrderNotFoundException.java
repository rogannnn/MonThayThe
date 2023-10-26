package com.example.tttndemo.exception;

import org.springframework.data.jpa.repository.JpaRepository;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
