package com.week.gdsc.controller;


import com.week.gdsc.config.TokenProvider;
import com.week.gdsc.domain.User;
import com.week.gdsc.dto.TokenDTO;
import com.week.gdsc.dto.UserDTO;
import com.week.gdsc.service.PasswordEncoder;
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
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO){
        UserDTO registeredUser=userService.createUser(userDTO);
        return ResponseEntity.ok().body(registeredUser);
    }

    // 로그인
    // 로그인시 토큰생성하여 반환!
    @PostMapping("/login")
    public ResponseEntity<?> signInUser(@RequestBody UserDTO userDTO){
        UserDTO responseUserDTO = userService.signInUser(userDTO);
        return ResponseEntity.ok().body(responseUserDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<?> test( HttpServletRequest request){
        return ResponseEntity.ok().body(request.getAttribute("username"));
    }
}
