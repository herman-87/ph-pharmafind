package com.ph.user.domain.user;

import cm.fastrelays.common.domain.UuidBaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "t_user")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends UuidBaseEntity implements UserDetails {

  @Column(name = "c_username", nullable = false, unique = true, length = 120)
  private String username;

  @Column(name = "c_email", nullable = false, unique = true, length = 190)
  private String email;

  @Column(name = "c_password", length = 255)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "c_provider", nullable = false, length = 30)
  @Builder.Default
  private AuthProvider provider = AuthProvider.LOCAL;

  @Column(name = "c_provider_id", length = 190)
  private String providerId;

  @Builder.Default
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "t_user_role",
      joinColumns = @JoinColumn(name = "c_user_id"),
      inverseJoinColumns = @JoinColumn(name = "c_role_id"))
  private Set<Role> roles = new HashSet<>();

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "t_user_scope", joinColumns = @JoinColumn(name = "c_user_id"))
  @Column(name = "c_scope")
  private Set<String> scopes = new HashSet<>();

  @Builder.Default
  @Column(name = "c_enabled", nullable = false)
  private boolean enabled = true;

  @Builder.Default
  @Column(name = "c_account_non_expired", nullable = false)
  private boolean accountNonExpired = true;

  @Builder.Default
  @Column(name = "c_account_non_locked", nullable = false)
  private boolean accountNonLocked = true;

  @Builder.Default
  @Column(name = "c_credentials_non_expired", nullable = false)
  private boolean credentialsNonExpired = true;

  @Builder.Default
  @Column(name = "c_email_verified", nullable = false)
  private boolean emailVerified = false;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).toList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
