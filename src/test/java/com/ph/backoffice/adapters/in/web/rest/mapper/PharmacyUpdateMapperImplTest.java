package com.ph.backoffice.adapters.in.web.rest.mapper;

import static org.mockito.Mockito.verify;

import com.ph.backoffice.domain.certifications.Pharmacy;
import com.ph.backoffice.domain.certifications.model.PharmacyUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PharmacyUpdateMapperImplTest {

  @Mock private PharmacyMapper pharmacyMapper;
  @Mock private PharmacyUpdateRequest request;
  @Mock private Pharmacy pharmacy;

  @InjectMocks private PharmacyUpdateMapperImpl mapper;

  @Test
  void shouldDelegateToPharmacyMapper() {
    mapper.update(request, pharmacy);

    verify(pharmacyMapper).updatePharmacy(request, pharmacy);
  }
}
