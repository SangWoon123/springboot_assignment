package com.week.gdsc.config;

import com.week.gdsc.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class TokenProvider {

    private final String SECRET_KEY="FksjdifejsfAmdsjfn9sksfnsaAfmdkfgmfrlatkddnswkdsksdlqslekdnfldkvmsepanjfgkfRkrnrmfWkddldi";

    /*
        JWT 토큰생성
        header(타입,알고리즘) / payload(내용) / signature(서명)
     */
    public String create(User user){
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .setSubject(user.getUserId()) // String 변경
                .setIssuer("Jwt login")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now()
                        .plus(1, ChronoUnit.DAYS)))
                .compact();
    }

    /*
        Jwt 검증 Payload에 해당하는 userId를 반환
     */
    public String validateAndGetUserId(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return (claims.getSubject());
    }
}
