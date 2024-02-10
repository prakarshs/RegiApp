package com.Registration.UserRegistration.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USER_DETAILS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "USER_NAME")
    private String fullName;
    @Column(name = "USER_STATE")
    private String state;
    @Column(name = "USER_EMAIL", unique = true)
    private String email;
    @Column(name = "USER_PASSWORD")
    private String password;
    @Column(name = "USER_ROLE")
    private String role;
}
