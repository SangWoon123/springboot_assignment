package com.week.gdsc.dto;

import com.week.gdsc.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String username;

    private String refreshToken;

    private String accessToken;

    public static UserResponse toUserDTO(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SignUpResponse{
        private Long id;
        private String username;
    }
}
