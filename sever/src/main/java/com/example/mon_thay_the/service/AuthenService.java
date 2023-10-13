package com.example.mon_thay_the.service;

import com.example.mon_thay_the.bean.Token;
import com.example.mon_thay_the.entity.Role;
import com.example.mon_thay_the.entity.User;
import com.example.mon_thay_the.principal.MyUserDetails;
import com.example.mon_thay_the.repository.RoleRepository;
import com.example.mon_thay_the.repository.UserRepository;
import com.example.mon_thay_the.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public User sigupUser(SignUpRequest signUpRequest){
        User newUser = new User(signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getFirstName(), signUpRequest.getLastName());
        Role userRole = roleRepository.findById(2).get();
        newUser.setRole(userRole);
        userRepository.save(newUser);
        return newUser;
    }

    }
