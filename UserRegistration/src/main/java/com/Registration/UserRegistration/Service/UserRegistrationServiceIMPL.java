package com.Registration.UserRegistration.Service;

import com.Registration.UserRegistration.Constants.AppConstants;
import com.Registration.UserRegistration.Constants.ErrorConstants;
import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Errors.CustomError;
import com.Registration.UserRegistration.Model.UserRequest;
import com.Registration.UserRegistration.Repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserRegistrationServiceIMPL implements UserRegistrationService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(UserRequest userRequest) {
        UserEntity user;

        log.info("CREATING USER...");
        user = UserEntity.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .state(AppConstants.INACTIVE)
                .role(AppConstants.USER)
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();

        try {
            log.info("SAVING USER...");
            userRepository.save(user);
        } catch (Exception e) {
            log.info("ENCOUNTERED ERROR!");
            throw new CustomError(ErrorConstants.DUPLICATE_EMAIL, ErrorConstants.TRY_A_DIFFERENT_EMAIL);
        }

        return user;
    }
}
