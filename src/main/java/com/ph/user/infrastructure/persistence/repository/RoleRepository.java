package com.ph.user.infrastructure.persistence.repository;

import com.ph.user.domain.user.Role;
import com.ph.user.domain.user.RoleName;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {

  Optional<Role> findByName(RoleName name);
}
