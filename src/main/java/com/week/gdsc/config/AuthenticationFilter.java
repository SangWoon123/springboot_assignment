package com.week.gdsc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.week.gdsc.domain.User;
import com.week.gdsc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String[] whitelist = {"/auth/signup","/auth/login"};
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Whitelist에 있는 URL은 인증 필터를 적용하지 않음
        if (isWhitelistedPath(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = parseBearerToken(request);
        if (token != null ) {
            String userId = tokenProvider.validateAndGetUserId(token);
            User user = userService.byUserID(userId);
            if (user != null) {
                request.setAttribute("user", user);
                log.info("로그인한 사용자 정보: " + user.getUserId() + " " + user.getPassword());
            } else {
                // 유저를 찾을 수 없는 경우 (예: 삭제된 계정)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid user");
                return;
            }
        } else {
            // JWT가 없거나 유효하지 않은 경우
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isWhitelistedPath(String requestURI) {
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
