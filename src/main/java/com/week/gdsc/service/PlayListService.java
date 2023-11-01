package com.week.gdsc.service;

import com.week.gdsc.domain.Music;
import com.week.gdsc.domain.Playlist;
import com.week.gdsc.dto.MusicDTO;
import com.week.gdsc.dto.PlayListDTO;
import com.week.gdsc.repository.MusicRepository;
import com.week.gdsc.repository.PlayListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayListService {
    private final PlayListRepository playRepository;
    private final MusicRepository musicRepository;


    public PlayListDTO.Response createPlayList(PlayListDTO.Create playListDTO){
        Playlist playlist= Playlist.builder()
                .name(playListDTO.getName())
                .build();
        Playlist save = playRepository.save(playlist);

        return PlayListDTO.Response.builder()
                .id(save.getId())
                .playListName(save.getName())
                .build();
    }

    @Transactional
    public void addMusicToPlaylist(Long musicNum,Long playListNum){
        Playlist playlist = findByIdPlayList(playListNum);
        Music music = musicRepository.findById(musicNum).orElseThrow(() -> new IllegalArgumentException("해당음악이 존재하지 않습니다."));
        List<Music> musicList = playlist.getMusicList();
        musicList.add(music);
    }

    // 플레이리스트에 있는 음악 조회
    public PlayListDTO.ResponseMusicList showMusicList(Long playListNum){
        Playlist playlist = findByIdPlayList(playListNum);

        List<Music> musicList = playlist.getMusicList();
        List<MusicDTO> musicDTOList = musicList.stream().map(
                music ->
                        MusicDTO.builder()
                                .id(music.getId())
                                .artist(music.getArtist())
                                .title(music.getTitle())
                                .playTime(music.getPlayTime())
                                .build()
        ).collect(Collectors.toList());

        return PlayListDTO.ResponseMusicList.builder()
                .id(playlist.getId())
                .playName(playlist.getName())
                .musicDTOList(musicDTOList)
                .build();
    }

    // 플레이리스트 이름 수정
    public PlayListDTO.ResponseMusicList updatePlayListName(Long playListNum,String newName){
        Playlist playlist = findByIdPlayList(playListNum);
        playlist.updateName(newName);

        List<Music> musicList = playlist.getMusicList();
        List<MusicDTO> musicDTOList = musicList.stream().map(
                music ->
                        MusicDTO.builder()
                                .id(music.getId())
                                .artist(music.getArtist())
                                .title(music.getTitle())
                                .playTime(music.getPlayTime())
                                .build()
        ).collect(Collectors.toList());

        return PlayListDTO.ResponseMusicList.builder()
                .id(playlist.getId())
                .playName(playlist.getName())
                .musicDTOList(musicDTOList)
                .build();
    }

    //플레이리스트 에서 음악제거
    @Transactional
    public PlayListDTO.ResponseMusicList deleteMusicInPlayList(Long playListNum,List<Long> musicId){
        Playlist playlist=findByIdPlayList(playListNum);
        playlist.getMusicList().removeIf(music -> musicId.contains(music.getId()));

        List<Music> musicList = playlist.getMusicList();
        List<MusicDTO> musicDTOList = musicList.stream().map(
                music ->
                        MusicDTO.builder()
                                .id(music.getId())
                                .artist(music.getArtist())
                                .title(music.getTitle())
                                .playTime(music.getPlayTime())
                                .build()
        ).collect(Collectors.toList());

        return PlayListDTO.ResponseMusicList.builder()
                .id(playlist.getId())
                .playName(playlist.getName())
                .musicDTOList(musicDTOList)
                .build();
    }

    // 플레이리스트 삭제
    public void deletePlayList(Long playListNum){
        Playlist playlist=findByIdPlayList(playListNum);
        playRepository.delete(playlist);
    }

    // 중복코드 메서드
    private Playlist findByIdPlayList(Long playListNum){
        return playRepository.findById(playListNum).orElseThrow(() -> new IllegalArgumentException("플레이리스트가 존재하지 않습니다."));
    }

}
