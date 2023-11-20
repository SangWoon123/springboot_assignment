package com.week.gdsc.config;

import com.week.gdsc.domain.RefreshToken;
import com.week.gdsc.domain.User;
import com.week.gdsc.dto.response.TokenResponse;
import com.week.gdsc.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class TokenProvider {

    private final String SECRET_KEY;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenProvider(@Value("${secret.key}") String SECRET_KEY, RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.SECRET_KEY=SECRET_KEY;
    }

    /*
            JWT 토큰생성
            header(타입,알고리즘) / payload(내용) / signature(서명)
         */
    public String createAccessToken(String username){
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .setSubject(username) // String 변경
                .setIssuer("access")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now()
                        .plus(1, ChronoUnit.DAYS)))
                .compact();
    }

    public String createRefreshToken(String username){
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .setSubject(username) // String 변경
                .setIssuer("refresh")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now()
                        .plus(15, ChronoUnit.DAYS)))
                .compact();
    }

    private boolean validateRefreshToken(String refreshToken){
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(refreshToken);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public TokenResponse createToken(User user){
        String accessToken = createAccessToken(user.getUsername());
        String refreshToken=createRefreshToken(user.getUsername());

        // DB에 리프레시토큰 저장
        RefreshToken newRefreshToken= RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(newRefreshToken);

        return new TokenResponse(accessToken,refreshToken);
    }

    /*
        Jwt 검증 Payload에 해당하는 username를 반환
     */
    public String validateAndGetUsername(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public TokenResponse createNewAccessToken(String refreshToken){
        if(!validateRefreshToken(refreshToken)){
            throw new RuntimeException("불가한 refreshToken 입니다.");
        }

        String username=validateAndGetUsername(refreshToken);
        String newAccessToken=createAccessToken(username);
        return new TokenResponse(newAccessToken,refreshToken);
    }

//    private String extractRefreshToken(String accessToken) {
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(secretKey)
//                    .build()
//                    .parseClaimsJws(accessToken)
//                    .getBody();
//            return (String) claims.get("refreshToken");
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new InvalidTokenException("Invalid access token");
//        }
//    }

}
