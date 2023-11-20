package com.week.gdsc.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserRequest {
    @NotBlank(message = "아이디는 필수 입력 값 입니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    private String password;
}
