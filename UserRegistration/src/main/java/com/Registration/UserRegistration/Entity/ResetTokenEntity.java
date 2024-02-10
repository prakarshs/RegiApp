package com.Registration.UserRegistration.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "RESET_PASSWORD_TOKEN")
public class ResetTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resetId;

    @Column(name = "RESET_TOKEN")
    private String resetToken;

    @Column(name = "NEW_PASSWORD")
    private String newPassword;

    @Column(name = "EXPIRATION_TIME")
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RESET_USER", nullable = false, foreignKey = @ForeignKey(name = "FK_RESET_TOKEN"))
    private UserEntity user;
}
