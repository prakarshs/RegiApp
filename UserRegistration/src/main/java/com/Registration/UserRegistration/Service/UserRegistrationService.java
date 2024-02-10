package com.Registration.UserRegistration.Service;

import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Model.UserRequest;

public interface UserRegistrationService {
    UserEntity registerUser(UserRequest userRequest);
}
