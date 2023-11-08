package com.week.gdsc.dto;

import lombok.Getter;

@Getter
public class TokenDTO {
    private String accessToken;
    private String refreshToken;

    public TokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
