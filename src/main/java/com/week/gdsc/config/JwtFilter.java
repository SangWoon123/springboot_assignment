package com.week.gdsc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.week.gdsc.dto.response.TokenResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private static final String[] WHITE_LIST = {"/auth/signup", "/auth/login"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Whitelist에 있는 URL은 인증 필터를 적용하지 않음
        if (isWhitelistedPath(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = parserBearerToken(request);
        if (token != null && !token.equalsIgnoreCase("null")) {
            log.info("{JwtFilter 실행중..} AccessToken 존재");
            try {
                String username = tokenProvider.validateAndGetUsername(token);
                request.setAttribute("username", username);
            } catch (ExpiredJwtException e) {
                logger.warn("토큰이 만료되었습니다. 리프레시 토큰을 사용하여 재발급합니다.", e);

                // 쿠키에서 리프레시 토큰 추출
                String refreshToken = extractRefreshTokenFromCookie(request);

                if (StringUtils.hasText(refreshToken)) {
                    TokenResponse newTokens = tokenProvider.createNewAccessToken(refreshToken);

                    // 새로운 액세스 토큰과 리프레시 토큰을 응답 헤더에 설정
//                    response.setHeader("Authorization", "Bearer " + newTokens.getAccessToken());
//                    response.setHeader("Set-Cookie", "refreshToken=" + newTokens.getRefreshToken() + "; Path=/; HttpOnly; SameSite=None; Secure");

                    // 새로운 액세스 토큰을 JSON으로 응답에 포함시킴
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(response.getWriter(), newTokens);
                    return;
                }
            }
        }else{
            log.info("token 상태: {}",token);
        }
        filterChain.doFilter(request, response);
    }



    private String parserBearerToken (HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private boolean isWhitelistedPath (String requestURI){
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("쿠키이름: "+cookie.getName());
                if (cookie.getName().equals("refreshToken")) {
                    log.info("쿠키값: "+cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
