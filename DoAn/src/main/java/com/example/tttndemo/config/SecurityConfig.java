package com.example.tttndemo.config;

import com.example.tttndemo.authentication.MyUserDetails;
import com.example.tttndemo.entity.User;
import com.example.tttndemo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/profile/**","/address/**","/order/**","/profile"
                        ,"/change-info","/change-info/save","/change-password","/change-password/save").hasAnyAuthority("ADMIN", "USER")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .and().rememberMe()
                .and().logout().permitAll()
                .and().httpBasic().disable()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){

        return email ->{
            User user = userRepository.getUserByEmail(email);
            if(user == null){
                throw new UsernameNotFoundException("Could not find user with email :" + email);
            }
            return new MyUserDetails(user);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
