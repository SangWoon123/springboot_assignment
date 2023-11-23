package com.week.gdsc.aspect;

import com.week.gdsc.config.TokenProvider;
import com.week.gdsc.domain.AuthUser;
import com.week.gdsc.exception.BusinessLogicException;
import com.week.gdsc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/*
    login을 하게되면 AccessToken을 발급받게 되는데, 이후 로그인시 헤더에 토큰을 넣고 요청이 들어오게된다.
    AOP는 헤더에서 accesstoken이 존재하는지 확인하고 이를 다음 컨트롤러에 넘겨주는 역ㅕ
 */
@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class JwtAspect {
    private final TokenProvider tokenProvider;
    @Before("@annotation(JwtAop)")
    public void checkJwtToken(){
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if(request!=null){
            String token=request.getHeader("Authorization");
            String username = tokenProvider.validateAndGetUsername(token);
            AuthUser authUser=AuthUser.builder()
                            .username(username)
                            .build();
            request.setAttribute("authUser",authUser);
            return;
        }else{
            throw new BusinessLogicException(ErrorCode.TOKEN_NOT_FOUND);
        }
    }
}
