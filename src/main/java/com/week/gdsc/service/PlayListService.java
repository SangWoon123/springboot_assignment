package com.week.gdsc.service;

import com.week.gdsc.domain.AuthUser;
import com.week.gdsc.domain.Music;
import com.week.gdsc.domain.Playlist;
import com.week.gdsc.domain.User;
import com.week.gdsc.dto.MusicDTO;
import com.week.gdsc.dto.PlaylistRequest;
import com.week.gdsc.dto.PlaylistResponse;
import com.week.gdsc.exception.BusinessLogicException;
import com.week.gdsc.exception.ErrorCode;
import com.week.gdsc.repository.MusicRepository;
import com.week.gdsc.repository.PlayListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayListService {
    private final PlayListRepository playRepository;
    private final MusicRepository musicRepository;
    private final UserService userService;

    public PlaylistResponse.PlaylistNameResponse createPlayList(PlaylistRequest.UpdatePlaylistNameRequest playListDTO, AuthUser authUser) {

        User user=userService.findByUsername(authUser.getUsername());

        // 플레이리스트 이름 미입력시 발생하는 오류
        if (playListDTO.getPlaylistName().isBlank()) {
            throw new BusinessLogicException(ErrorCode.PLAYLIST_NAME_NOT_FOUND);
        }

        Playlist playlist = Playlist.builder()
                .name(playListDTO.getPlaylistName())
                .user(user)
                .build();

        user.updatePlaylist(playlist);

        Playlist save = playRepository.save(playlist);

        return PlaylistResponse.PlaylistNameResponse.builder()
                .id(save.getId())
                .playListName(save.getName())
                .build();
    }

    public void addMusicToPlaylist(Long musicNum, Long playListNum,HttpServletRequest request) {
        User user=getUserFromServlet(request);
        Playlist playlist = findByIdPlayList(playListNum);
        // 권한체크
        checkAccessPermission(playlist,user);

        Music music = musicRepository.findById(musicNum).orElseThrow(() -> new BusinessLogicException(ErrorCode.MUSIC_NOT_FOUND));

        //추가
        music.setPlaylist(playlist);

        List<Music> musicList = playlist.getMusicList();
        musicList.add(music);
    }

    // 플레이리스트에 있는 음악 조회
    @Transactional(readOnly=true)
    public PlaylistResponse.MusicsResponse showMusicList(Long playListNum, Pageable pageable, HttpServletRequest request) {
        User user=getUserFromServlet(request);
        Playlist playlist = findByIdPlayList(playListNum);
        // 권한체크
        checkAccessPermission(playlist,user);

        // 플레이리스트의 음악 목록을 페이지네이션하여 조회
        Page<Music> musicPage = musicRepository.findByPlaylistId(playlist.getId(), pageable);

        List<MusicDTO> musicDTOList = MusicDTO.mapToMusicDTOS(musicPage.getContent());

        return PlaylistResponse.MusicsResponse.ResponseDTO(playlist, musicDTOList);
    }


    // 플레이리스트 이름 수정
    public PlaylistResponse.MusicsResponse updatePlayListName(Long playListNum, String playListName) {
        Playlist playlist = findByIdPlayList(playListNum);
        playlist.updateName(playListName);

        //Music -> MusicDTO
        List<Music> musicList = playlist.getMusicList();
        List<MusicDTO> musicDTOList = MusicDTO.mapToMusicDTOS(musicList);

        return PlaylistResponse.MusicsResponse.ResponseDTO(playlist, musicDTOList);
    }

    //플레이리스트 에서 음악제거
    public PlaylistResponse.MusicsResponse deleteMusicInPlayList(Long playListNum, List<Long> musics, HttpServletRequest request) {


        // 음악체크
        if (musics.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.MUSIC_NOT_FOUND_FOR_DELETE);
        }

        User user=getUserFromServlet(request);
        Playlist playlist = findByIdPlayList(playListNum);

        // 권한체크
        checkAccessPermission(playlist,user);

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

        return PlaylistResponse.MusicsResponse.ResponseDTO(playlist, musicDTOList);
    }

    // 플레이리스트 삭제
    public void deletePlayList(Long playListNum,HttpServletRequest request) {
        User user=getUserFromServlet(request);
        Playlist playlist = findByIdPlayList(playListNum);
        // 권한체크
        checkAccessPermission(playlist,user);

        List<Music> musicList = new ArrayList<>(playlist.getMusicList());
        musicList.forEach(music -> music.setPlaylist(null));
        playRepository.delete(playlist);
    }


    // 중복코드 메서드
    private Playlist findByIdPlayList(Long playListNum) {
        return playRepository.findById(playListNum)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    private User getUserFromServlet(HttpServletRequest request){
        String username=(String)request.getAttribute("username");
        return  userService.findByUsername(username);
    }


    private void checkAccessPermission(Playlist playlist, User user) {
        if (playlist.getUser() != user) {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }
    }




}
