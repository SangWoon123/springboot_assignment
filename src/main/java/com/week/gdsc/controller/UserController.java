package com.week.gdsc.controller;


import com.week.gdsc.config.TokenProvider;
import com.week.gdsc.domain.User;
import com.week.gdsc.dto.UserDTO;
import com.week.gdsc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final TokenProvider tokenProvider;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){

        User user=User.builder()
                .userId(userDTO.getUserId())
                .password(userDTO.getPassword())
                .build();

        User registeredUser=userService.create(user);
        UserDTO responseUserDTO=UserDTO.builder()
                .id(registeredUser.getId())
                .userId(registeredUser.getUserId())
                .build();

        return ResponseEntity.ok().body(responseUserDTO);
    }

    // 로그인
    // 로그인시 토큰생성하여 반환!
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO){
        User user=userService.getByCredentials(userDTO.getUserId(), userDTO.getPassword());
        if(user!=null){
            final String token=tokenProvider.create(user);
            final UserDTO responseUserDTO=UserDTO.builder()
                    .userId(user.getUserId())
                    .id(user.getId())
                    .token(token)
                    .build();

            System.out.println(responseUserDTO.getToken());
            return ResponseEntity.ok().body(responseUserDTO);
        }
        UserDTO.UserVerifyResponseDto.UserVerifyResponseDtoBuilder valid = UserDTO.UserVerifyResponseDto.builder()
                .isValid(false);

        return ResponseEntity.badRequest().body(valid);
    }
}