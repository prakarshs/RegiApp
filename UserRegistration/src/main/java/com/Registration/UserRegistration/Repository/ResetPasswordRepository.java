package com.Registration.UserRegistration.Repository;

import com.Registration.UserRegistration.Entity.ResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetTokenEntity,Long> {
   Optional<ResetTokenEntity> findByResetToken(String token);
}
