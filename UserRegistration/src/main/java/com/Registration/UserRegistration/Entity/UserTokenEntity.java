package com.Registration.UserRegistration.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USER_TOKEN_DETAILS")
public class UserTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTokenId;
    @Column(name = "USER_TOKEN")
    private String verificationToken;
    @Column(name = "EXPIRATION_TIME")
    private Date expirationTime;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER" ,nullable = false, foreignKey = @ForeignKey(name = "FK_USER_TOKEN"))
    private UserEntity user;

}

