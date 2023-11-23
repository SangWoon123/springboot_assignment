package com.week.gdsc.config;

import com.week.gdsc.aspect.JwtAuth;
import com.week.gdsc.domain.AuthUser;
import com.week.gdsc.exception.BusinessLogicException;
import com.week.gdsc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtAuth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        // 헤더 값 체크
        if (httpServletRequest != null) {

            String token = httpServletRequest.getHeader("Authorization");

            log.info("resolver toke {}",token);

            if (token != null && !token.trim().equals("")) {
                // 토큰 있을 경우 검증
                if (tokenProvider.validateToken(token)) {
                    // 검증 후 AuthenticationUser 리턴
                    return tokenProvider.getAuthUser(token);
                }
            }

//            // 토큰은 없지만 필수가 아닌 경우 체크
//            JwtAuth annotation = parameter.getParameterAnnotation(JwtAuth.class);
//            if (annotation != null) {
//                // 필수가 아닌 경우 기본 객체 리턴
//                return new AuthUser();
//            }
            // 토큰 값이 없으면 에러
            throw new BusinessLogicException(ErrorCode.TOKEN_NOT_FOUND);
        }

        return null;
    }
}
