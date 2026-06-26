package cm.fastrelays.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class UuidBaseEntity implements Serializable {

  private static final Clock CLOCK = Clock.system(ZoneId.of("Africa/Douala"));

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "c_id")
  protected UUID id;

  @Column(name = "c_created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "c_updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now(CLOCK);
    this.updatedAt = LocalDateTime.now(CLOCK);
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now(CLOCK);
  }
}
