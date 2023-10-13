package com.example.mon_thay_the.exception;

public class DuplicateEmailException extends Exception{

    public DuplicateEmailException(String message) {
        super(message);
    }
}
