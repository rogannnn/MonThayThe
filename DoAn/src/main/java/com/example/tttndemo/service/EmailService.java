package com.example.tttndemo.service;

import com.example.tttndemo.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Async
    public void sendEmailOTP(User user, int otp) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("hoanghoainam2752001@gmail.com", "My Shop support");
        helper.setTo(user.getEmail());

        String subject = "Your OTP to change password - Expire in 5 minutes!";

        String content = "<p>Hello " + user.getFirstName() + "</p>"
                + "<p> For security reason, you are required to use the following OTP to change your password"
                + "<p><b><h1>" + otp + "</h1></b></p>"
                + "<br>"
                + "<p>Note: This OTP will be expire in 5 minutes";

        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);

    }


}
