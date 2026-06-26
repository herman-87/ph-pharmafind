package com.ph.backoffice.adapters.in.web.rest.mapper;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyUpdateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PharmacyUpdateMapperImpl implements PharmacyUpdateMapper {

  private final PharmacyMapper pharmacyMapper;

  @Override
  public void update(PharmacyUpdateRequest request, Pharmacy pharmacy) {
    pharmacyMapper.updatePharmacy(request, pharmacy);
  }
}
