package com.ph.backoffice.domain.certifications.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class OpeningHour {

  private final DayOfWeek dayOfWeek;
  private final LocalTime openTime;
  private final LocalTime closeTime;
  private final boolean isOpen;

  public OpeningHour(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, boolean isOpen) {
    this.dayOfWeek = dayOfWeek;
    this.isOpen = isOpen;
    if (isOpen) {
      this.openTime = openTime;
      this.closeTime = closeTime;
    } else {
      this.openTime = null;
      this.closeTime = null;
    }
  }
}
