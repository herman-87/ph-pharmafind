package com.ph.user.infrastructure.persistence.repository;

import com.ph.user.domain.user.AuthProvider;
import com.ph.user.domain.user.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
