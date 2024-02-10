package com.Registration.UserRegistration.Repository;

import com.Registration.UserRegistration.Entity.UserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserTokenEntity, Long> {
   Optional<UserTokenEntity>  findByVerificationToken(String token);
}
