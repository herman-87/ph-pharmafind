package cm.fastrelays.common.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class KeycloakJwtAuthenticationConverter
    implements Converter<Jwt, AbstractAuthenticationToken> {

  private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter =
      new JwtGrantedAuthoritiesConverter();

  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    Collection<GrantedAuthority> authorities =
        Stream.concat(
                defaultGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractScopes(jwt).stream())
            .collect(Collectors.toSet());

    return new JwtAuthenticationToken(jwt, authorities, getPreferredUsername(jwt));
  }

  private String getPreferredUsername(Jwt jwt) {
    return jwt.getClaim("preferred_username");
  }

  private Collection<? extends GrantedAuthority> extractScopes(Jwt jwt) {
    String scopeClaim = jwt.getClaim("scope");

    if (scopeClaim == null || scopeClaim.trim().isEmpty()) {
      return Collections.emptyList();
    }

    return Arrays.stream(scopeClaim.split("\\s+"))
        .filter(scope -> !scope.isEmpty())
        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
        .collect(Collectors.toSet());
  }
}
