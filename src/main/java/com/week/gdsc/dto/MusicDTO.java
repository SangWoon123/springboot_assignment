package com.week.gdsc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MusicDTO {
    private Long id;
    private String artist;
    private String title;
    private int playTime;
    private PlayListDTO.ResponseMusicList playListDTO;


}
