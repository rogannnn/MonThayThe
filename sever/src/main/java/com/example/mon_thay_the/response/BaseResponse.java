package com.example.mon_thay_the.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseResponse {

    private Integer result;
    private String msg;
    private String method;
    private Integer code;

}
