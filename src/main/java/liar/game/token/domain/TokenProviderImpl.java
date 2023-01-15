package liar.game.token.domain;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import liar.game.member.domain.Authority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProviderImpl implements InitializingBean, TokenProvider{

    public static final String TOKEN_TYPE = "Bearer ";
    private static final String AUTHORITIES_KEY = "auth";
    private Key key;

    private final String secretKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;
    private final long reissueRefreshTime;

    public TokenProviderImpl(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-expiration-time}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-expiration-time}") long refreshTokenExpirationTime,
            @Value("${jwt.reissue-refresh-time}") long reissueRefreshTime) {
        this.secretKey = secretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.reissueRefreshTime = reissueRefreshTime;
    }



    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, refreshTokenExpirationTime);
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, accessTokenExpirationTime);
    }

    public String createAccessToken(String email, List<Authority> roles) {
        return createTokenFormLogin(email, roles, accessTokenExpirationTime);
    }

    public String createRefreshToken(String email, List<Authority> roles) {
        return createTokenFormLogin(email, roles, refreshTokenExpirationTime);
    }

    private String createTokenFormLogin(String email, List<Authority> authorities, long tokenTime) {

        List<String> roles = getRoles(authorities);

        Claims claims = Jwts.claims().setSubject(email);
        claims.put(AUTHORITIES_KEY, roles);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private String createToken(Authentication authentication, long tokenTime) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiryDate)
                .compact();
    }


    public String getUserEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }


    public long getRemainingTimeFromToken(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.getTime() - (new Date()).getTime();
    }

    public boolean isMoreThanReissueTime(String token) {
        return getRemainingTimeFromToken(token) >= reissueRefreshTime;
    }


    public Claims getClaims(String token) {

        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public Authentication getAuthentication(String token) {

        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String removeType(String token) {

        if (token == null || token.length() < TOKEN_TYPE.length()) {
            return null;
        }

        return token.substring(TOKEN_TYPE.length());
    }

    private List<String> getRoles (List<Authority> authorities) {

        List<String> roles = new ArrayList<>();
        authorities.forEach(role -> roles.add(role.getAuthorities().getAuthoritiesName()));
        return roles;
    }


    public Long getRemainTime(String token) {

        Claims claims = getClaims(token);

        log.info("claims = {}", claims);
        log.info("getExpiration = {}", claims.getExpiration());

        if (claims == null || claims.getExpiration() == null) {
            return null;
        }

        return claims.getExpiration().getTime();
    }

}
