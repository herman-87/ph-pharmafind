package com.ph.backoffice.domain.certifications;

import cm.fastrelays.common.domain.UuidBaseEntity;
import com.ph.backoffice.domain.certifications.model.OpeningHour;
import com.ph.backoffice.domain.certifications.model.OpeningHourListConverter;
import com.ph.backoffice.domain.certifications.model.PharmacyData;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Map;
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

  @Column(name = "c_name")
  private String name;

  @Column(name = "c_email")
  private String email;

  @Column(name = "c_phone")
  private String phone;

  @Column(name = "c_city")
  private String city;

  @Column(name = "c_quarter")
  private String quarter;

  @Column(name = "c_address")
  private String address;

  @Column(name = "c_gps_coordinates")
  private String gpsCoordinates;

  @Column(name = "c_registration_number")
  private String registrationNumber;

  @Column(name = "c_tax_id")
  private String taxId;

  @Column(name = "c_is_24h")
  private Boolean is24h;

  @Column(name = "c_delivery_available")
  private Boolean deliveryAvailable;

  @Column(name = "c_delivery_radius_km")
  private Integer deliveryRadiusKm;

  @Convert(converter = OpeningHourListConverter.class)
  @Column(name = "c_opening_hours")
  private List<OpeningHour> openingHours;

  @Column(name = "c_payment_methods")
  private Map<String, String> paymentMethods;

  @Column(name = "c_social_links")
  private Map<String, String> socialLinks;

  @Column(name = "c_certified")
  private boolean certified;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_owner_id")
  private Owner owner;

  public static Pharmacy create(PharmacyData data, Owner owner) {
    return Pharmacy.builder()
        .name(data.name())
        .email(data.email())
        .phone(data.phone())
        .city(data.city())
        .quarter(data.quarter())
        .address(data.address())
        .gpsCoordinates(data.gpsCoordinates())
        .registrationNumber(data.registrationNumber())
        .taxId(data.taxId())
        .is24h(data.is24h())
        .deliveryAvailable(data.deliveryAvailable())
        .deliveryRadiusKm(data.deliveryRadiusKm())
        .openingHours(data.openingHours())
        .paymentMethods(data.paymentMethods())
        .socialLinks(data.socialLinks())
        .owner(owner)
        .build();
  }
}
