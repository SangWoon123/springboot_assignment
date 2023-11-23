package com.week.gdsc.controller;

//import com.week.gdsc.aspect.TokenCheck;
import com.week.gdsc.aspect.JwtAuth;
import com.week.gdsc.domain.AuthUser;
import com.week.gdsc.dto.PlaylistRequest;
import com.week.gdsc.dto.PlaylistResponse;
import com.week.gdsc.service.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlayListController {

    private final PlayListService playListService;


    //@JwtAuth
    @PostMapping
    public ResponseEntity<PlaylistResponse.PlaylistNameResponse> createPlayList(@RequestBody @Valid PlaylistRequest.UpdatePlaylistNameRequest playListDTO,@JwtAuth AuthUser authUser) {
        if(authUser.getUsername()==null){
            System.out.println("zzzzzz");
        }
        PlaylistResponse.PlaylistNameResponse createdPlaylist = playListService.createPlayList(playListDTO,authUser);
        return new ResponseEntity<>(createdPlaylist, HttpStatus.CREATED);
    }

    // REST API방식 @Pathvariable 사용
    // 플레이리스트에 음악추가
    @JwtAuth
    @PostMapping("/{playlistId}/music") // 플레이리스트 번호 , 음악번호
    public ResponseEntity<String> addMusicToPlaylist(@PathVariable Long playlistId, @RequestBody @Valid PlaylistRequest.AddMusicRequest addMusicRequest, HttpServletRequest request) {
        playListService.addMusicToPlaylist(addMusicRequest.getMusicNum(), playlistId,request);
        return new ResponseEntity<>("추가완료", HttpStatus.OK);
    }

    // 플레이리스트 음악조회
    @JwtAuth
    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse.MusicsResponse> showSongList(@PathVariable Long playlistId, @PageableDefault(page = 0, size = 5) Pageable pageable, HttpServletRequest request) {
        // 플레이리스트 조회 및 음악 조회 로직 실행
        PlaylistResponse.MusicsResponse responseMusicList = playListService.showMusicList(playlistId, pageable,request);
        return new ResponseEntity<>(responseMusicList, HttpStatus.FOUND);
    }

    // 플레이리스트 이름 수정 -> Patch메서드 사용
    @JwtAuth
    @PatchMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse.MusicsResponse> updatePlayListName(@PathVariable Long playlistId, @RequestBody @Valid PlaylistRequest.UpdatePlaylistNameRequest playlistName) {
        PlaylistResponse.MusicsResponse responseMusicList = playListService.updatePlayListName(playlistId, playlistName.getPlaylistName());
        return new ResponseEntity<>(responseMusicList, HttpStatus.OK);
    }

    //플레이리스트 에서 음악제거
    @JwtAuth
    @DeleteMapping("/{playlistId}/music")
    public ResponseEntity<PlaylistResponse.MusicsResponse> deleteMusicInPlayList(@PathVariable Long playlistId, @RequestParam List<Long> musics, HttpServletRequest request) {
        PlaylistResponse.MusicsResponse result = playListService.deleteMusicInPlayList(playlistId, musics,request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //플레이리스트 삭제
    @JwtAuth
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<String> deletePlayList(@PathVariable Long playlistId, HttpServletRequest request) {
        playListService.deletePlayList(playlistId,request);
        return new ResponseEntity<>("플레이리스트 삭제완료", HttpStatus.OK);
    }


}
