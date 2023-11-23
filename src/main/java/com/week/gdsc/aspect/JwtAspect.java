//package com.week.gdsc.aspect;
//
//import com.week.gdsc.config.TokenProvider;
//import com.week.gdsc.exception.BusinessLogicException;
//import com.week.gdsc.exception.ErrorCode;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
///*
//    login을 하게되면 AccessToken을 발급받게 되는데, 이후 로그인시 헤더에 토큰을 넣고 요청이 들어오게된다.
//    AOP는 헤더에서 accesstoken이 존재하는지 확인하고 이를 다음 컨트롤러에 넘겨주는 역ㅕ
// */
//@Component
//@Aspect
//@RequiredArgsConstructor
//@Slf4j
//public class JwtAspect {
//    private final TokenProvider tokenProvider;
//    @Before("execution(* com.week.gdsc.controller.PlayListController.*(..))")
//    public void checkJwtToken(){
//        HttpServletRequest request =
//                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//
//        String token=parseBearerToken(request);
//        // 토큰이 저장도 되지않았는데 이전의 토큰의 정보를가져와서 안에있는 유저이름을 가지고 검증하여 진행새
//        //token의 유효성 검증 로직 필요
//        if(token!=null){
//            String username = tokenProvider.validateAndGetUsername(token);
//            request.setAttribute("username",username);
//            return;
//        }else{
//            throw new BusinessLogicException(ErrorCode.TOKEN_NOT_FOUND);
//        }
//    }
//
//    private String parseBearerToken(HttpServletRequest request){
//        String bearerToken=request.getHeader("Authorization");
//
//        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}
