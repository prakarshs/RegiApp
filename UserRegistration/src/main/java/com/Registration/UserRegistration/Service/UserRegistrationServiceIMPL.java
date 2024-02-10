package com.Registration.UserRegistration.Service;

import com.Registration.UserRegistration.Constants.AppConstants;
import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Model.UserRequest;
import com.Registration.UserRegistration.Repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserRegistrationServiceIMPL implements UserRegistrationService{



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserEntity registerUser(UserRequest userRequest) {
        log.info("CREATING USER...");
        UserEntity user = UserEntity.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .state(AppConstants.INACTIVE)
                .role(AppConstants.USER)
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
        log.info("SAVING USER...");
        userRepository.save(user);
        return user;
    }
}
