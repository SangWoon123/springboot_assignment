package com.week.gdsc.config;

import com.week.gdsc.domain.AuthUser;
import com.week.gdsc.domain.RefreshToken;
import com.week.gdsc.domain.User;
import com.week.gdsc.dto.TokenDTO;
import com.week.gdsc.exception.BusinessLogicException;
import com.week.gdsc.exception.ErrorCode;
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
                .setIssuer("Jwt login")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now()
                        .plus(1, ChronoUnit.DAYS)))
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

    public boolean validateToken(String token) {
        try {
            return !Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(parseBearer(token))
                    .getBody()
                    .getExpiration().before(new Date());

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessLogicException(ErrorCode.TOKEN_NOT_FOUND);
        }
    }

    public TokenDTO createToken(User user){
        String accessToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(user.getUsername()) // String 변경
                .setIssuer("access")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now()
                        .plus(15, ChronoUnit.MINUTES)))
                .compact();
        //분리

        String refreshToken=Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(user.getUsername()) // String 변경
                .setIssuer("refresh")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now()
                        .plus(15, ChronoUnit.DAYS)))
                .compact();

        // DB에 리프레시토큰 저장
        RefreshToken newRefreshToken= RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(newRefreshToken);

        return new TokenDTO(accessToken,refreshToken);
    }

    /*
        Jwt 검증 Payload에 해당하는 username를 반환
     */
    public String validateAndGetUsername(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(parseBearer(token))
                .getBody();

        return claims.getSubject();
    }

    public TokenDTO createNewAccessToken(String refreshToken){
        if(!validateRefreshToken(refreshToken)){
            throw new RuntimeException("불가한 refreshToken 입니다.");
        }

        String username=validateAndGetUsername(refreshToken);
        String newAccessToken=createAccessToken(username);
        return new TokenDTO(newAccessToken,refreshToken);
    }

    public AuthUser getAuthUser(String token){
        Claims getClaims=Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(parseBearer(token))
                .getBody();


        return AuthUser.builder()
                .username(getClaims.getSubject())
                .build();
    }



    /*
    헤더 파싱
     */
    private String parseBearer(String authorization){
        return authorization.replace("Bearer","").trim();
    }
}
