package com.ph.pharmafind.domain.pharmacy;

import cm.fastrelays.common.domain.UuidBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "t_pharmacy")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Pharmacy extends UuidBaseEntity {

  @Column(name = "c_name", nullable = false, length = 200)
  private String name;

  @Column(name = "c_email", nullable = false, unique = true, length = 190)
  private String email;

  @Column(name = "c_phone", nullable = false, length = 20)
  private String phone;

  @Column(name = "c_city", nullable = false, length = 100)
  private String city;

  @Column(name = "c_quarter", nullable = false, length = 100)
  private String quarter;

  @Column(name = "c_address", nullable = false, length = 300)
  private String address;

  @Column(name = "c_gps_coordinates", nullable = false, length = 100)
  private String gpsCoordinates;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_owner_id", nullable = false)
  private Owner owner;

  public static Pharmacy create(
      String name,
      String email,
      String phone,
      String city,
      String quarter,
      String address,
      String gpsCoordinates,
      Owner owner) {
    return Pharmacy.builder()
        .name(name)
        .email(email)
        .phone(phone)
        .city(city)
        .quarter(quarter)
        .address(address)
        .gpsCoordinates(gpsCoordinates)
        .owner(owner)
        .build();
  }
}