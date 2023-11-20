package com.week.gdsc.controller;


import com.week.gdsc.dto.UserResponse;
import com.week.gdsc.dto.request.UserRequest;
import com.week.gdsc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest){
        UserResponse.SignUpResponse registeredUser=userService.createUser(userRequest);
        return ResponseEntity.ok().body(registeredUser);
    }

    // 로그인
    // 로그인시 토큰생성하여 반환!
    @PostMapping("/login")
    public ResponseEntity<?> signInUser(@RequestBody UserRequest userRequest){
        UserResponse responseUserResponse = userService.signInUser(userRequest);
        return ResponseEntity.ok().body(responseUserResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<?> test( HttpServletRequest request){
        return ResponseEntity.ok().body(request.getAttribute("username"));
    }
}
