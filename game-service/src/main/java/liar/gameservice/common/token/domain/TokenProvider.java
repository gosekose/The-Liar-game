package liar.gameservice.common.token.domain;

public interface TokenProvider {

    String TOKEN_TYPE = "Bearer ";

    String getUserIdFromToken(String token);

    long getRemainingTimeFromToken(String token);

    boolean isMoreThanReissueTime(String token);

    boolean validateToken(String authToken);

    String removeType(String token);



}
