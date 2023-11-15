package com.week.gdsc.dto;

import com.week.gdsc.domain.Music;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MusicDTO {
    private Long id;
    private String artist;
    private String title;
    private String playTime;

    public static List<MusicDTO> mapToMusicDTOS(List<Music> musicList) {
        List<MusicDTO> musicDTOList = musicList.stream().map(
                music ->
                        MusicDTO.builder()
                                .id(music.getId())
                                .artist(music.getArtist())
                                .title(music.getTitle())
                                .playTime(music.formationTime())
                                .build()
        ).collect(Collectors.toList());
        return musicDTOList;
    }

}
