package cm.fastrelays.common.security;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

public class TokenRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      @NonNull HttpRequest request,
      @NonNull byte[] body,
      @NonNull ClientHttpRequestExecution execution)
      throws IOException {
    String token = TokenContext.getToken();
    if (token != null) {
      request.getHeaders().setBearerAuth(token);
    }
    return execution.execute(request, body);
  }
}
