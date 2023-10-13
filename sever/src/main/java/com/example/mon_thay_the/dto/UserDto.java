package com.example.mon_thay_the.dto;

import com.example.mon_thay_the.entity.Role;
import com.example.mon_thay_the.entity.User;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private Date registrationDate;
    private Boolean enabled;
    private Role role;

    public UserDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.registrationDate = user.getRegistrationDate();
        this.enabled = user.getEnabled();
        this.role = user.getRole();
    }
}
