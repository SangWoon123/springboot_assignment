package com.week.gdsc.dto;

import lombok.*;

import java.util.List;

public class PlayListDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddMusicRequest{
        private Long musicNum;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestPlaylistName {
        private String playlistName;
    }

    @Getter
    @Builder
    public static class Response{
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
    }
}
