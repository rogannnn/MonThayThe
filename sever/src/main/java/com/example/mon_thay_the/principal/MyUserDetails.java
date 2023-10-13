package com.example.mon_thay_the.principal;



import com.example.mon_thay_the.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =new SimpleGrantedAuthority(user.getRole().getName());
        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();

    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId(){
        return user.getId();
    }

    public String getFirstName(){
        return user.getFirstName();
    }

    public String getFullName(){
        return user.getLastName() + " " + user.getFirstName();
    }

    @Override
    public String toString() {
        return "MyUserDetails{" +
                "user=" + user +
                '}';
    }


}
