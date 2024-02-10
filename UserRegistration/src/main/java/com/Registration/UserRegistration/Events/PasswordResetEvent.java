package com.Registration.UserRegistration.Events;

import com.Registration.UserRegistration.Entity.UserEntity;
import lombok.Data;
import org.springframework.context.ApplicationEvent;
@Data
public class PasswordResetEvent extends ApplicationEvent {
    private String resetUrl;
    private UserEntity user;
    private String newPassword;
    public PasswordResetEvent(UserEntity user, String resetUrl, String newPassword) {
        super(user);
        this.user = user;
        this.resetUrl = resetUrl;
        this.newPassword = newPassword;
    }
}
