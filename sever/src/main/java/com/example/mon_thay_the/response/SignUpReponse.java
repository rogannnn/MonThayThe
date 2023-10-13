package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpReponse extends BaseResponse{
    private UserDto data;

    private String accessToken;

    private String refreshToken;


    public SignUpReponse(Integer result, String msg, String method, Integer code, UserDto data, String accessToken, String refreshToken){
        super(result, msg, method, code);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.data = data;
    }
}
