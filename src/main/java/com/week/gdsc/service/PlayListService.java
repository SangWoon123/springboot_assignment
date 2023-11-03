package com.week.gdsc.service;

import com.week.gdsc.domain.Music;
import com.week.gdsc.domain.Playlist;
import com.week.gdsc.dto.MusicDTO;
import com.week.gdsc.dto.PlayListDTO;
import com.week.gdsc.exception.BusinessLogicException;
import com.week.gdsc.exception.ErrorCode;
import com.week.gdsc.repository.MusicRepository;
import com.week.gdsc.repository.PlayListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
        Music music = musicRepository.findById(musicNum).orElseThrow(() -> new BusinessLogicException(ErrorCode.MUSIC_NOT_FOUND));

        //추가
        music.setPlaylist(playlist);

        List<Music> musicList = playlist.getMusicList();
        musicList.add(music);
    }

    // 플레이리스트에 있는 음악 조회
    public PlayListDTO.ResponseMusicList showMusicList(Long playListNum, Pageable pageable){
        Playlist playlist = findByIdPlayList(playListNum);

        // 플레이리스트의 음악 목록을 페이지네이션하여 조회
        Page<Music> musicPage = musicRepository.findByPlaylistId(playlist.getId(), pageable);
        //System.out.println("토탈 엘리먼트:"+musicPage.getTotalElements()+"토탈 페이지:"+musicPage.getTotalPages());

        List<MusicDTO> musicDTOList = mapToMusicDTOS(musicPage.getContent());

        return ResponseDTO(playlist, musicDTOList);
    }


    // 플레이리스트 이름 수정
    public PlayListDTO.ResponseMusicList updatePlayListName(Long playListNum,String newName){
        Playlist playlist = findByIdPlayList(playListNum);
        playlist.updateName(newName);

        //Music -> MusicDTO
        List<Music> musicList = playlist.getMusicList();
        List<MusicDTO> musicDTOList = mapToMusicDTOS(musicList);

        return ResponseDTO(playlist, musicDTOList);
    }

    //플레이리스트 에서 음악제거
    @Transactional
    public PlayListDTO.ResponseMusicList deleteMusicInPlayList(Long playListNum,MusicDTO.DeleteMusicListNum deleteMusicListNum){
        Playlist playlist=findByIdPlayList(playListNum);
//        List<Music> musicList = playlist.getMusicList();

        // 제거할 음악의 ID 리스트
        List<Long> removeMusicIds = deleteMusicListNum.getDeleteMusicNumList();

        List<Music> removeMusicList = playlist.getMusicList().stream()
                .filter(music -> removeMusicIds.contains(music.getId()))
                .collect(Collectors.toList());

        // 제거할 음악리스트를 분리해서 playList에서 제거
        removeMusicList.forEach(music -> {
            music.setPlaylist(null);
            playlist.getMusicList().remove(music);
        });

        //Music -> MusicDTO
        List<MusicDTO> musicDTOList = mapToMusicDTOS(playlist.getMusicList());


        // 음악체크
        if(musicDTOList.isEmpty()){
            throw new BusinessLogicException(ErrorCode.MUSIC_NOT_FOUND_FOR_DELETE);
        }

        return ResponseDTO(playlist, musicDTOList);
    }

    // 플레이리스트 삭제
    @Transactional
    public void deletePlayList(Long playListNum){
        Playlist playlist = findByIdPlayList(playListNum);
        List<Music> musicList = new ArrayList<>(playlist.getMusicList());
        musicList.forEach(music -> music.setPlaylist(null));
        playRepository.delete(playlist);
    }


    // 중복코드 메서드
    private Playlist findByIdPlayList(Long playListNum){
        return playRepository.findById(playListNum).
                orElseThrow(()-> new BusinessLogicException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    private static PlayListDTO.ResponseMusicList ResponseDTO(Playlist playlist, List<MusicDTO> musicDTOList) {
        return PlayListDTO.ResponseMusicList.builder()
                .id(playlist.getId())
                .playName(playlist.getName())
                .musicDTOList(musicDTOList)
                .build();
    }

    private static List<MusicDTO> mapToMusicDTOS(List<Music> musicList) {
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
