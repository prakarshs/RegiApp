package com.Registration.UserRegistration.Service;

import com.Registration.UserRegistration.Constants.AppConstants;
import com.Registration.UserRegistration.Constants.ErrorConstants;
import com.Registration.UserRegistration.Model.ResetPasswordRequest;
import com.Registration.UserRegistration.Entity.ResetTokenEntity;
import com.Registration.UserRegistration.Entity.UserEntity;
import com.Registration.UserRegistration.Entity.UserTokenEntity;
import com.Registration.UserRegistration.Errors.CustomError;
import com.Registration.UserRegistration.Events.PasswordResetEvent;
import com.Registration.UserRegistration.Events.UserActivationEvent;
import com.Registration.UserRegistration.Model.MailTemplateModel;
import com.Registration.UserRegistration.Model.UserRequest;
import com.Registration.UserRegistration.Repository.ResetPasswordRepository;
import com.Registration.UserRegistration.Repository.UserRepository;
import com.Registration.UserRegistration.Repository.UserTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@Log4j2
public class UserRegistrationServiceIMPL implements UserRegistrationService {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public UserEntity registerUser(UserRequest userRequest, HttpServletRequest request) {
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
        log.info("CALLING EVENT...");
        applicationEventPublisher.publishEvent(new UserActivationEvent(user, templateUrl(request)));

        return user;
    }

    @Override
    public void saveUserToken(UserEntity user, String verificationtoken) {
        log.info("CREATING USER TOKEN...");
        UserTokenEntity userToken = UserTokenEntity.builder()
                .verificationToken(verificationtoken)
                .user(user)
                .expirationTime(Date.from(Instant.now().plusSeconds(300)))
                .build();
        log.info("SAVING USER TOKEN...");
        userTokenRepository.save(userToken);

    }

    @Override
    public String activate(String token) {
        log.info("CHECKING FOR USER-TOKEN IN DB...");
        UserTokenEntity userToken = userTokenRepository.findByVerificationToken(token).orElseThrow(() -> new CustomError(ErrorConstants.TOKEN_DOESNT_EXIST, ErrorConstants.TRY_WITH_A_DIFFERENT_TOKEN));
        log.info("CHECKING EXPIRY OF TOKEN...");
        if(Date.from(Instant.now()).getTime() - userToken.getExpirationTime().getTime() >= 0){
            log.info("TOKEN HAS EXPIRED.");
            log.info("DELETING EXPIRED TOKEN FROM DB...");
            userTokenRepository.delete(userToken);
            return "THE VERIFICATION TOKEN ISNT VALID ANYMORE.";
        }
        else{
            UserEntity user = userToken.getUser();
            user.setState(AppConstants.ACTIVE);
            userRepository.save(user);
            return "USER WAS ACTIVATED!";
        }
    }

    @Value("${spring.mail.username}")
    private String fromMail;
    @Override
    public String sendMail(String email, MailTemplateModel mailTemplateModel) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        log.info("BUILDING EMAIL...");
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mailTemplateModel.getSubject());
        simpleMailMessage.setText(mailTemplateModel.getContent());
        simpleMailMessage.setTo(email);

        log.info("SENDING EMAIL...");
        javaMailSender.send(simpleMailMessage);
        return "Mail Sent Successfully!";
    }

    @Override
    public String resetPassword(ResetPasswordRequest passwordRequest, HttpServletRequest request) {
        log.info("CHECKING EMAIL IN DB...");
        UserEntity user = userRepository.findByEmail(passwordRequest.getEmail()).orElseThrow(()->new CustomError(ErrorConstants.EMAIL_ID_DOESNT_EXIST, ErrorConstants.TRY_WITH_A_DIFFERENT_EMAIL_ID));
        applicationEventPublisher.publishEvent(new PasswordResetEvent(user,templateUrl(request),passwordRequest.getNewPassword()));
        return "PASSWORD RESET HAS BEEN INITIATED";
    }

    @Override
    public String resetTokenSave(UserEntity user, String newPassword, String resetToken) {
        log.info("CREATING RESET ENTITY TO BE SAVED...");
        ResetTokenEntity resetTokenEntity = ResetTokenEntity.builder()
                .resetToken(resetToken)
                .user(user)
                .newPassword(passwordEncoder.encode(newPassword))
                .expirationTime(Date.from(Instant.now().plusSeconds(300)))
                .build();

        log.info("SAVING RESET ENTITY...");
        resetPasswordRepository.save(resetTokenEntity);

        return "SAVED RESET ENTITY.";
    }

    @Override
    public String changePassword(String token) {
        log.info("CHECKING FOR RESET-TOKEN IN DB...");
        ResetTokenEntity tokenEntity = resetPasswordRepository.findByResetToken(token).orElseThrow(()->new CustomError(ErrorConstants.TOKEN_DOESNT_EXIST,ErrorConstants.TRY_WITH_A_DIFFERENT_TOKEN));
        UserEntity user = tokenEntity.getUser();

        log.info("CHECKING EXPIRY...");
        if(Date.from(Instant.now()).getTime() - tokenEntity.getExpirationTime().getTime() >= 0)
        {
            log.info("RESET TOKEN EXPIRED.");
            resetPasswordRepository.delete(tokenEntity);
            return "RESET TOKEN IS EXPIRED.";
        }
        else{
            log.info("CHANGING PASSWORD...");
            user.setPassword(tokenEntity.getNewPassword());
            resetPasswordRepository.delete(tokenEntity);
            userRepository.save(user);
            return "PASSWORD HAS BEEN RESET WITH THE NEW PASSWORD.";
        }


    }

    private String templateUrl(HttpServletRequest request) {
        log.info("CREATING TEMPLATE URL");
        return "http://"
                +
                request.getServerName()
                +
                ":"
                +
                request.getServerPort()
                +
                "/"
                +
                "users";

    }
}
