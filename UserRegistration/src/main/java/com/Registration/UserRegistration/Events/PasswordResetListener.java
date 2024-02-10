package com.Registration.UserRegistration.Events;

import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Model.MailTemplateModel;
import com.Registration.UserRegistration.Service.UserRegistrationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log4j2
@Component
public class PasswordResetListener implements ApplicationListener<PasswordResetEvent> {

    @Autowired
    private UserRegistrationService userRegistrationService;
    @Override
    public void onApplicationEvent(PasswordResetEvent event) {
        UserEntity user = event.getUser();
        String newPassword = event.getNewPassword();
        String resetToken = UUID.randomUUID().toString();

        System.out.println(userRegistrationService.resetTokenSave(user,newPassword, resetToken));

        String resetUrl = event.getResetUrl() + "/resetPassword?token=" + resetToken;

        MailTemplateModel mailTemplateModel = MailTemplateModel.builder()
                .subject("RegiApp Password Reset Request.")
                .content("You are receiving this email because a password reset request was initiated for your account.\n\n"
                + "To reset your password, please click the link below:\n\n"
                + resetUrl)
                .build();

        System.out.println(userRegistrationService.sendMail(user.getEmail(), mailTemplateModel));
        System.out.println("\n"+resetUrl);
    }
}
