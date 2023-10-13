package com.example.mon_thay_the.response;

import com.example.mon_thay_the.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends BaseResponse{

    private UserDto data;

    public UserResponse(Integer result, String msg, String method, Integer code, UserDto data){
        super(result, msg, method, code);
        this.data = data;
    }
}
