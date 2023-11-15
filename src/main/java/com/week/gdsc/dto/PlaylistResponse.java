package com.week.gdsc.dto;

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
        private String playListName;
    }

    @Getter
    @Setter
    @Builder
    public static class MusicsResponse {
        private Long id;
        private String playName;
        private List<MusicDTO> musicDTOList;

        public static MusicsResponse ResponseDTO(Playlist playlist, List<MusicDTO> musicDTOList) {
            return PlaylistResponse.MusicsResponse.builder()
                    .id(playlist.getId())
                    .playName(playlist.getName())
                    .musicDTOList(musicDTOList)
                    .build();
        }
    }
}
