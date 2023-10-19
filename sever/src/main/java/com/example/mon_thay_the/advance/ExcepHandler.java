package com.example.mon_thay_the.advance;
import com.example.mon_thay_the.exception.DuplicateEmailException;
import com.example.mon_thay_the.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExcepHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerEmailUniqueException(Exception e, HttpServletRequest request){

        BaseResponse data = new BaseResponse(0,e.getMessage(),request.getMethod(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(data, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handlerEmailUniqueException(BindException e, HttpServletRequest request){

        String errorMessage = "";
        if(e.getBindingResult().hasErrors())
            errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        BaseResponse response = new BaseResponse(0,errorMessage,request.getMethod(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

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
