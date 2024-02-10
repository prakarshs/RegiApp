package com.Registration.UserRegistration.Events;

import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Service.UserRegistrationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@Log4j2
public class UserActivationListener implements ApplicationListener<UserActivationEvent> {
    @Autowired
    private UserRegistrationService userRegistrationService;
    @Override
    public void onApplicationEvent(UserActivationEvent event) {
        UserEntity user = event.getUser();
        String verificationtoken = UUID.randomUUID().toString();

        userRegistrationService.saveUserToken(user,verificationtoken);
        log.info("CREATING URL TO SEND...");
        System.out.println("\n" + event.getActivationUrl() + "/activate?token=" + verificationtoken + "\n");

    }
}
