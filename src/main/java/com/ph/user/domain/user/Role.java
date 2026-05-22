package com.ph.user.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "c_id", nullable = false, updatable = false)
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(name = "c_name", nullable = false, unique = true, length = 50)
  private RoleName name;
}
