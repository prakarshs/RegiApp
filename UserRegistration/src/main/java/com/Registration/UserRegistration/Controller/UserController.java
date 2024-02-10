package com.Registration.UserRegistration.Controller;

import com.Registration.UserRegistration.Model.ResetPasswordRequest;
import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Model.UserRequest;
import com.Registration.UserRegistration.Service.UserRegistrationService;
import jakarta.servlet.http.HttpServletRequest;
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
    private ResponseEntity<UserEntity> register(@RequestBody UserRequest userRequest, HttpServletRequest request){
        return new ResponseEntity<>(userRegistrationService.registerUser(userRequest,request), HttpStatus.OK);
    }

    @GetMapping("/activate")
    private ResponseEntity<String> activate(@RequestParam(name = "token") String token){
        return new ResponseEntity<>(userRegistrationService.activate(token),HttpStatus.OK);
    }

    @PostMapping("/reset")
    private ResponseEntity<String> reset (@RequestBody ResetPasswordRequest passwordRequest, HttpServletRequest request){
        return new ResponseEntity<>(userRegistrationService.resetPassword(passwordRequest,request),HttpStatus.OK);
    }

    @GetMapping("/resetPassword")
    private ResponseEntity<String> resetPassword (@RequestParam(name = "token") String token){
        return new ResponseEntity<>(userRegistrationService.changePassword(token),HttpStatus.OK);
    }
}
