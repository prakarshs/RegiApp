package com.Registration.UserRegistration.Events;

import com.Registration.UserRegistration.Entity.UserEntity;
import lombok.Data;
import org.springframework.context.ApplicationEvent;
@Data
public class UserActivationEvent extends ApplicationEvent {
    private UserEntity user;
    private String activationUrl;

    public UserActivationEvent(UserEntity user, String activationUrl) {
        super(user);
        this.activationUrl = activationUrl;
        this.user = user;
    }
}
