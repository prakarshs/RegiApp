package com.Registration.UserRegistration.Service;

import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Model.MailTemplateModel;
import com.Registration.UserRegistration.Model.UserRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserRegistrationService {
    UserEntity registerUser(UserRequest userRequest, HttpServletRequest request);

    void saveUserToken(UserEntity user, String verificationtoken);

    String activate(String token);

    String sendMail(String email, MailTemplateModel mailTemplateModel);
}
