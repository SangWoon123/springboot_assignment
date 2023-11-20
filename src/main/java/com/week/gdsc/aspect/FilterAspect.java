package com.week.gdsc.aspect;

import com.week.gdsc.aspect.exception.BusinessLogicException;
import com.week.gdsc.aspect.exception.ErrorCode;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
@Aspect
@Component
public class FilterAspect {
    @Before("@annotation(TokenCheck)")
    public void TokenCheck(){
        ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request= requestAttributes.getRequest();

        String token =parserBearerToken(request);

        if(StringUtils.isEmpty(token)){
            throw new BusinessLogicException(ErrorCode.TOKEN_NOT_FOUND);
        }
    }

    private String parserBearerToken (HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}

