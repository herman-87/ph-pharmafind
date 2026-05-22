package cm.fastrelays.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Converter
public class PasswordCryptoConverter implements AttributeConverter<String, String> {

  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public String convertToDatabaseColumn(String rawPassword) {
    if (rawPassword == null || rawPassword.isBlank()) {
      throw new IllegalArgumentException("Password cannot be null or blank");
    }

    if (rawPassword.startsWith("$2a$")
        || rawPassword.startsWith("$2b$")
        || rawPassword.startsWith("$2y$")) {
      return rawPassword;
    }

    return passwordEncoder.encode(rawPassword);
  }

  @Override
  public String convertToEntityAttribute(String encodedPassword) {
    return encodedPassword;
  }
}
