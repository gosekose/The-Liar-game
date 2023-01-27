package liar.gateway.domain;

public interface TokenProvider {

    String TOKEN_TYPE = "Bearer ";

    long getRemainingTimeFromToken(String token);

    boolean validateToken(String jwt, String userId);
}
