//package com.week.gdsc.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.week.gdsc.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterRegistration;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Bean
//    public FilterRegistrationBean jwtAuthenticationFilter(TokenProvider provider,UserService userService) {
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter(new JwtFilter(provider,userService));
//        filterRegistrationBean.setOrder(1);
//        filterRegistrationBean.addUrlPatterns("/auth/login");
//        return filterRegistrationBean;
//    }
//
////    @Bean
////    public FilterRegistrationBean authUser(ObjectMapper objectMapper,UserService userService) {
////        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
////        filterRegistrationBean.setFilter(new AuthenticationFilter(objectMapper,userService));
////        filterRegistrationBean.setOrder(2);
////    filterRegistrationBean.addUrlPatterns("/auth/**     ");
////        return filterRegistrationBean;
////    }
//}
