package com.week.gdsc.controller;


import com.week.gdsc.config.TokenProvider;
import com.week.gdsc.domain.User;
import com.week.gdsc.dto.UserDTO;
import com.week.gdsc.service.PasswordEncoder;
import com.week.gdsc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO){
        User user= User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encrypt(userDTO.getUsername(),userDTO.getPassword()))
                .build();
        UserDTO registeredUser=userService.createUser(user);
        return ResponseEntity.ok().body(registeredUser);
    }

    // 로그인
    // 로그인시 토큰생성하여 반환!
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO){
        User user=userService.getByCredentials(userDTO.getUsername(), userDTO.getPassword(),passwordEncoder);
        if(user!=null){
            final String token=tokenProvider.create(user);
            final UserDTO responseUserDTO=UserDTO.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        }
        UserDTO.UserVerifyResponseDto.UserVerifyResponseDtoBuilder valid = UserDTO.UserVerifyResponseDto.builder()
                .isValid(false);

        return ResponseEntity.badRequest().body(valid);
    }

    @GetMapping("/user")
    public ResponseEntity<?> updateUserPassword(@RequestHeader(value = "Authorization") String authorizationHeader){
        return ResponseEntity.ok().body(userService.findUser(authorizationHeader));
    }
}
