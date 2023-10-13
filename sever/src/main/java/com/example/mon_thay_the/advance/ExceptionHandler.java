package com.example.mon_thay_the.advance;
import com.example.mon_thay_the.exception.DuplicateEmailException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerEmailUniqueException(Exception e){

            return ResponseEntity.badRequest().body(e.getMessage());

    }


//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ExceptionResponse> handlerInvalidArgument(MethodArgumentNotValidException e, HttpServletRequest request){
//        Map<String,String> errorMap = new HashMap<>();
//        e.getBindingResult().getFieldErrors().forEach(error -> {
//            errorMap.put(error.getField(),error.getDefaultMessage());
//        });
//        ExceptionResponse response = new ExceptionResponse(0, "Bad Request Exception", request.getMethod(), new Date().getTime(),
//                HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(),errorMap);
//        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//    }
}
