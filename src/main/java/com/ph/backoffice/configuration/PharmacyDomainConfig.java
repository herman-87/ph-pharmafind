package com.ph.backoffice.configuration;

import com.ph.backoffice.domain.certifications.port.in.feat.CreatePharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.CreateCertificationRequest;
import com.ph.backoffice.domain.certifications.port.in.feat.DeletePharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.GetPharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.ListPharmacies;
import com.ph.backoffice.domain.certifications.port.in.feat.UpdatePharmacy;
import com.ph.backoffice.domain.certifications.port.in.feat.VerifyPharmacy;
import com.ph.backoffice.domain.certifications.port.in.impl.CreatePharmacyImpl;
import com.ph.backoffice.domain.certifications.port.in.impl.CreateCertificationRequestImpl;
import com.ph.backoffice.domain.certifications.port.in.impl.DeletePharmacyImpl;
import com.ph.backoffice.domain.certifications.port.in.impl.GetPharmacyImpl;
import com.ph.backoffice.domain.certifications.port.in.impl.ListPharmaciesImpl;
import com.ph.backoffice.domain.certifications.port.in.impl.UpdatePharmacyImpl;
import com.ph.backoffice.domain.certifications.port.in.impl.VerifyPharmacyImpl;
import com.ph.backoffice.domain.certifications.port.out.feat.CertificationRequestRepository;
import com.ph.backoffice.domain.certifications.port.out.feat.OwnerRepository;
import com.ph.backoffice.domain.certifications.port.out.feat.PharmacyRepository;
import com.ph.backoffice.domain.certifications.service.CertificationDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PharmacyDomainConfig {

  @Bean
  public CertificationDomainService pharmacyDomainService(PharmacyRepository pharmacyRepository) {
    return new CertificationDomainService(pharmacyRepository);
  }

  @Bean
  public CreatePharmacy createPharmacy(
      PharmacyRepository pharmacyRepository,
      OwnerRepository ownerRepository,
      CertificationDomainService pharmacyDomainService) {
    return new CreatePharmacyImpl(pharmacyRepository, ownerRepository, pharmacyDomainService);
  }

  @Bean
  public ListPharmacies listPharmacies(PharmacyRepository pharmacyRepository) {
    return new ListPharmaciesImpl(pharmacyRepository);
  }

  @Bean
  public GetPharmacy getPharmacy(PharmacyRepository pharmacyRepository) {
    return new GetPharmacyImpl(pharmacyRepository);
  }

  @Bean
  public UpdatePharmacy updatePharmacy(PharmacyRepository pharmacyRepository) {
    return new UpdatePharmacyImpl(pharmacyRepository);
  }

  @Bean
  public DeletePharmacy deletePharmacy(PharmacyRepository pharmacyRepository) {
    return new DeletePharmacyImpl(pharmacyRepository);
  }

  @Bean
  public VerifyPharmacy verifyPharmacy(PharmacyRepository pharmacyRepository) {
    return new VerifyPharmacyImpl(pharmacyRepository);
  }

  @Bean
  public CertificationDomainService certificationDomainService(PharmacyRepository pharmacyRepository) {
    return new CertificationDomainService(pharmacyRepository);
  }

  @Bean
  public CreateCertificationRequest createCertificationRequest(
      CertificationRequestRepository certificationRequestRepository,
      CertificationDomainService certificationDomainService) {
    return new CreateCertificationRequestImpl(certificationRequestRepository, certificationDomainService);
  }
}
