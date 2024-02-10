package com.Registration.UserRegistration.Events;

import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Model.MailTemplateModel;
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
        String urlSend = event.getActivationUrl() + "/activate?token=" + verificationtoken;

        log.info("CALLING MAIL SERVICE...");
        MailTemplateModel mailTemplateModel = MailTemplateModel.builder()
                .subject("RegiApp User Activation")
                .content("Thank you " + user.getFullName() +  "! for registering with RegiApp." +"\n \nTo complete your registration and verify your email address, please click on the link below:\n \n" + urlSend )
                .build();
       System.out.println(userRegistrationService.sendMail(user.getEmail(),mailTemplateModel));
       System.out.println(urlSend);


    }
}
