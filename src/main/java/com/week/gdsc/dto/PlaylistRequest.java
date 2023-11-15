package com.week.gdsc.dto;

import com.week.gdsc.domain.Playlist;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class Play {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class AddMusicRequest{
        @NotBlank(message = "추가하려는 음악을 선택해 주세요.")
        private Long musicNum;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class RequestPlaylistName {
        @NotBlank(message = "재생목록 이름을 입력하여야 합니다.")
        private String playlistName;
    }

    @Getter
    @Builder
    public static class ResponsePlaylistName {
        private Long id;
        private String playListName;
    }

    @Getter
    @Setter
    @Builder
    public static class ResponseMusicList{
        private Long id;
        private String playName;
        private List<MusicDTO> musicDTOList;

        public static ResponseMusicList ResponseDTO(Playlist playlist, List<MusicDTO> musicDTOList) {
            return Play.ResponseMusicList.builder()
                    .id(playlist.getId())
                    .playName(playlist.getName())
                    .musicDTOList(musicDTOList)
                    .build();
        }
    }
}
