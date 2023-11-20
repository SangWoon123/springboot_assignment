package com.week.gdsc.dto.response;

import com.week.gdsc.domain.Playlist;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PlaylistResponse {

    @Getter
    @Builder
    public static class PlaylistNameResponse {
        private Long id;
        private String playlistName;
    }

    @Getter
    @Setter
    @Builder
    public static class MusicsResponse {
        private Long id;
        private String playName;
        private List<MusicResponse> musicResponseList;

        public static MusicsResponse ResponseDTO(Playlist playlist, List<MusicResponse> musicResponseList) {
            return PlaylistResponse.MusicsResponse.builder()
                    .id(playlist.getId())
                    .playName(playlist.getName())
                    .musicResponseList(musicResponseList)
                    .build();
        }
    }
}
