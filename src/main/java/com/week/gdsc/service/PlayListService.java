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


    public PlayListDTO.Response createPlayList(PlayListDTO.RequestPlaylistName playListDTO) {

        // 플레이리스트 이름 미입력시 발생하는 오류
        if (playListDTO.getPlaylistName().isBlank()) {
            throw new BusinessLogicException(ErrorCode.PLAYLIST_NAME_NOT_FOUND);
        }

        Playlist playlist = Playlist.builder()
                .name(playListDTO.getPlaylistName())
                .build();
        Playlist save = playRepository.save(playlist);

        return PlayListDTO.Response.builder()
                .id(save.getId())
                .playListName(save.getName())
                .build();
    }

    @Transactional
    public void addMusicToPlaylist(Long musicNum, Long playListNum) {
        Playlist playlist = findByIdPlayList(playListNum);
        Music music = musicRepository.findById(musicNum).orElseThrow(() -> new BusinessLogicException(ErrorCode.MUSIC_NOT_FOUND));

        //추가
        music.setPlaylist(playlist);

        List<Music> musicList = playlist.getMusicList();
        musicList.add(music);
    }

    // 플레이리스트에 있는 음악 조회
    public PlayListDTO.ResponseMusicList showMusicList(Long playListNum, Pageable pageable) {
        Playlist playlist = findByIdPlayList(playListNum);

        // 플레이리스트의 음악 목록을 페이지네이션하여 조회
        Page<Music> musicPage = musicRepository.findByPlaylistId(playlist.getId(), pageable);

        List<MusicDTO> musicDTOList = MusicDTO.mapToMusicDTOS(musicPage.getContent());

        return PlayListDTO.ResponseMusicList.ResponseDTO(playlist, musicDTOList);
    }


    // 플레이리스트 이름 수정
    @Transactional
    public PlayListDTO.ResponseMusicList updatePlayListName(Long playListNum, String playListName) {
        Playlist playlist = findByIdPlayList(playListNum);
        playlist.updateName(playListName);

        //Music -> MusicDTO
        List<Music> musicList = playlist.getMusicList();
        List<MusicDTO> musicDTOList = MusicDTO.mapToMusicDTOS(musicList);

        return PlayListDTO.ResponseMusicList.ResponseDTO(playlist, musicDTOList);
    }

    //플레이리스트 에서 음악제거
    @Transactional
    public PlayListDTO.ResponseMusicList deleteMusicInPlayList(Long playListNum, List<Long> musics) {
        // 음악체크
        if (musics.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.MUSIC_NOT_FOUND_FOR_DELETE);
        }

        Playlist playlist = findByIdPlayList(playListNum);

        // 제거할 음악의 ID 리스트
        List<Music> removeMusicList = playlist.getMusicList().stream()
                .filter(music -> musics.contains(music.getId()))
                .collect(Collectors.toList());

        // 제거할 음악리스트를 분리해서 playList에서 제거
        removeMusicList.forEach(music -> {
            music.setPlaylist(null);
            playlist.getMusicList().remove(music);
        });

        //Music -> MusicDTO
        List<MusicDTO> musicDTOList = MusicDTO.mapToMusicDTOS(playlist.getMusicList());

        return PlayListDTO.ResponseMusicList.ResponseDTO(playlist, musicDTOList);
    }

    // 플레이리스트 삭제
    @Transactional
    public void deletePlayList(Long playListNum) {
        Playlist playlist = findByIdPlayList(playListNum);
        List<Music> musicList = new ArrayList<>(playlist.getMusicList());
        musicList.forEach(music -> music.setPlaylist(null));
        playRepository.delete(playlist);
    }


    // 중복코드 메서드
    private Playlist findByIdPlayList(Long playListNum) {
        return playRepository.findById(playListNum)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PLAYLIST_NOT_FOUND));
    }




}
