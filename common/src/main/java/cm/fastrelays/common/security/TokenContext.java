package cm.fastrelays.common.security;

public class TokenContext {

  private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

  public static String getToken() {
    return tokenHolder.get();
  }

  public static void setToken(String token) {
    tokenHolder.set(token);
  }

  public static void clear() {
    tokenHolder.remove();
  }
}
