package com.example.mon_thay_the.config;

import com.example.mon_thay_the.entity.User;
import com.example.mon_thay_the.principal.MyUserDetails;
import com.example.mon_thay_the.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;


    @Bean
    public UserDetailsService userDetailsService( ){

        return email ->{
            User user = userRepository.getUserByEmail(email);
            if(user == null){
                throw new UsernameNotFoundException("Could not find user with email :" + email);
            }
            return new MyUserDetails(user);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authoProvider = new DaoAuthenticationProvider();
        authoProvider.setUserDetailsService(userDetailsService());

        authoProvider.setPasswordEncoder(passwordEncoder());
        return authoProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}