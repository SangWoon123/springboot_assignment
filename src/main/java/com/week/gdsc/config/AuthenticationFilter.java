package com.week.gdsc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.week.gdsc.domain.User;
import com.week.gdsc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
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

    private static final String[] whitelist = {"/auth/signup"}; // 회원가입시 스킵
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final static String AUTH="인증된 사용자";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        User user = (User)request.getAttribute("user");

        if(user!=null){
            log.info("로그인한 사용자 정보:"+user.getUserId()+" "+user.getPassword());
        }

        // Whitelist에 있는 URL은 인증 필터를 적용하지 않음
        if (isWhitelistedPath(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request,response);

//        try {
//            log.info("인증 체크 필터 시작 {}", requestURI);
//            // 여기에서 인증 로직을 수행하고 필요에 따라 UserDTO 객체를 생성
//            UserDTO userDTO = objectMapper.readValue(request.getReader(), UserDTO.class);
//            UserDTO.UserVerifyResponseDto authenticateUser = userService.authenticateUser(userDTO);
//            // 인증 로직을 추가하고 유효한 사용자인지 확인
//            if(authenticateUser.isValid()){
//                log.info("사용자 인증 성공");
//                request.setAttribute(AUTH,new AuthUser(userDTO.getUserId()));
//            }else{
//                throw new IllegalAccessException();
//            }
//
//            // 필요한 경우 토큰을 생성하고 사용자에게 반환
//
//            // 인증에 성공하면 다음 필터로 이동
//            filterChain.doFilter(request, response);
//        } catch (Exception e) {
//            // 필터 내에서 예외 발생 시 예외 처리
//            log.info("로그인 실패");
//            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//            httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value());
//        }
    }

    private boolean isWhitelistedPath(String requestURI) {
        // Whitelist에 있는 URL은 인증 필터를 적용하지 않음
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
