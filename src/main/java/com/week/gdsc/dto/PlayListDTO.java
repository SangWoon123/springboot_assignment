package com.week.gdsc.dto;

import com.week.gdsc.domain.Music;
import lombok.*;

import java.util.List;

public class PlayListDTO {
    @Getter
    public static class Create{
        private String name;
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
