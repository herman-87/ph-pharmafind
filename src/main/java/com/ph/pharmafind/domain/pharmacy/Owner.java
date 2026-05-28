package com.ph.pharmafind.domain.pharmacy;

import cm.fastrelays.common.domain.UuidBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "t_owner")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Owner extends UuidBaseEntity {

  @Column(name = "c_username", nullable = false, unique = true, length = 120)
  private String username;

  @Column(name = "c_user_id", nullable = false, unique = true)
  private UUID userId;

  public static Owner create(String username, UUID userId) {
    return Owner.builder()
        .username(username)
        .userId(userId)
        .build();
  }
}