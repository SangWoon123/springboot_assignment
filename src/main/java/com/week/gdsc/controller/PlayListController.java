package com.week.gdsc.controller;

import com.week.gdsc.domain.User;
import com.week.gdsc.dto.PlayListDTO;
import com.week.gdsc.service.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlayListController {

    private final PlayListService playListService;

    @PostMapping
    public ResponseEntity<?> createPlayList(@RequestBody PlayListDTO.RequestPlaylistName playListDTO, HttpServletRequest request) {
        PlayListDTO.Response createdPlaylist = playListService.createPlayList(playListDTO,request);
        return new ResponseEntity<>(createdPlaylist, HttpStatus.CREATED);
    }

    // REST API방식 @Pathvariable 사용
    // 플레이리스트에 음악추가
    @PostMapping("/{playlistId}/music") // 플레이리스트 번호 , 음악번호
    public ResponseEntity<String> addMusicToPlaylist(@PathVariable Long playlistId, @RequestBody PlayListDTO.AddMusicRequest addMusicRequest,HttpServletRequest request) {
        playListService.addMusicToPlaylist(addMusicRequest.getMusicNum(), playlistId,request);
        return new ResponseEntity<>("추가완료", HttpStatus.OK);
    }

    // 플레이리스트 음악조회
    @GetMapping("/{playlistId}")
    public ResponseEntity<PlayListDTO.ResponseMusicList> showSongList(@PathVariable Long playlistId, @PageableDefault(page = 0, size = 5) Pageable pageable,HttpServletRequest request) {
        // 플레이리스트 조회 및 음악 조회 로직 실행
        PlayListDTO.ResponseMusicList responseMusicList = playListService.showMusicList(playlistId, pageable,request);
        return new ResponseEntity<>(responseMusicList, HttpStatus.FOUND);
    }

    // 플레이리스트 이름 수정 -> Patch메서드 사용
    @PatchMapping("/{playlistId}")
    public ResponseEntity<PlayListDTO.ResponseMusicList> updatePlayListName(@PathVariable Long playlistId, @RequestBody PlayListDTO.RequestPlaylistName playlistName) {
        PlayListDTO.ResponseMusicList responseMusicList = playListService.updatePlayListName(playlistId, playlistName.getPlaylistName());
        return new ResponseEntity<>(responseMusicList, HttpStatus.OK);
    }

    //플레이리스트 에서 음악제거
    @DeleteMapping("/{playlistId}/music")
    public ResponseEntity<PlayListDTO.ResponseMusicList> deleteMusicInPlayList(@PathVariable Long playlistId, @RequestParam List<Long> musics,HttpServletRequest request) {
        PlayListDTO.ResponseMusicList result = playListService.deleteMusicInPlayList(playlistId, musics,request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //플레이리스트 삭제
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<String> deletePlayList(@PathVariable Long playlistId, HttpServletRequest request) {
        playListService.deletePlayList(playlistId,request);
        return new ResponseEntity<>("플레이리스트 삭제완료", HttpStatus.OK);
    }


}
