package com.week.gdsc.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MusicDTO {
    private Long id;
    private String artist;
    private String title;
    private String playTime;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteMusicListNum{
        private List<Long> deleteMusicNumList;
    }
}
