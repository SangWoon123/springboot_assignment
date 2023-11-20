package com.week.gdsc.dto;

import com.week.gdsc.domain.Music;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MusicResponse {
    private Long id;
    private String artist;
    private String title;
    private String playTime;

    public static List<MusicResponse> mapToMusicDTOS(List<Music> musicList) {
        List<MusicResponse> musicResponseList = musicList.stream().map(
                music ->
                        MusicResponse.builder()
                                .id(music.getId())
                                .artist(music.getArtist())
                                .title(music.getTitle())
                                .playTime(music.formationTime())
                                .build()
        ).collect(Collectors.toList());
        return musicResponseList;
    }

}
