package com.week.gdsc.dto;

import com.week.gdsc.domain.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String token;

    @NotBlank(message = "아이디는 필수 입력 값 입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    private String password;
    private Long id;

    @Builder
    @Getter
    public static class UserVerifyResponseDto {
        private boolean isValid;
    }

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
