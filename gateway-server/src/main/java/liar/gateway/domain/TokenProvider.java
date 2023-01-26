package liar.gateway.domain;

public interface TokenProvider {

    String TOKEN_TYPE = "Bearer ";


    String getUserEmailFromToken(String token);

    long getRemainingTimeFromToken(String token);

    boolean isMoreThanReissueTime(String token);

    boolean validateToken(String authToken);

    String removeType(String token);



}
