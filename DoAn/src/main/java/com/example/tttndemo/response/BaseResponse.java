package com.example.tttndemo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse  implements Serializable {
    private Integer result;
    private String msg;
    private String method;
    private Integer code;
}
