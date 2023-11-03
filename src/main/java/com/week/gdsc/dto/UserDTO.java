package com.week.gdsc.dto;

import com.week.gdsc.domain.User;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String token;
    private String userId;
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
                .userId(user.getUserId())
                .password(user.getPassword())
                .build();
    }
}
