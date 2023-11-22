package com.week.gdsc.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PlaylistRequest {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class AddMusicRequest{
        @NotNull(message = "추가하려는 음악을 선택해 주세요.")
        private Long musicNum;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class UpdatePlaylistNameRequest {
        @NotBlank(message = "재생목록 이름을 입력하여야 합니다.")
        private String playlistName;
    }


}
