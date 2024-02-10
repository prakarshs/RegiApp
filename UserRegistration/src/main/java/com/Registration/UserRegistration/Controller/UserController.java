package com.Registration.UserRegistration.Controller;

import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Model.UserRequest;
import com.Registration.UserRegistration.Service.UserRegistrationService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @GetMapping("/test")
    private ResponseEntity<String> test(){
        return new ResponseEntity<>("HEY IT WORKSSSSS",HttpStatus.OK);
    }
    @PostMapping("/register")
    private ResponseEntity<UserEntity> register(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(userRegistrationService.registerUser(userRequest), HttpStatus.OK);
    }
}
