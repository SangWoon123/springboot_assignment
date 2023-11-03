package com.week.gdsc.controller;

import com.week.gdsc.dto.MusicDTO;
import com.week.gdsc.dto.PlayListDTO;
import com.week.gdsc.service.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/play")
@RequiredArgsConstructor
public class PlayListController {

    private final PlayListService playListService;

    @PostMapping
    public ResponseEntity<?> createPlayList(@RequestBody PlayListDTO.Create playListDTO){
        PlayListDTO.Response createdPlaylist=playListService.createPlayList(playListDTO);
        return new ResponseEntity<>(createdPlaylist, HttpStatus.CREATED);
    }

    // REST API방식 @Pathvariable 사용
    // 플레이리스트에 음악추가
    @PostMapping("/add/{playListNum}/{musicNum}") // 플레이리스트 번호 , 음악번호
    public ResponseEntity<?> addSongToPlaylist(@PathVariable Long musicNum,@PathVariable Long playListNum){
        playListService.addMusicToPlaylist(musicNum,playListNum);
        return new ResponseEntity<>("추가완료",HttpStatus.OK);
    }

    // 플레이리스트 음악조회
    @GetMapping("/{playListNum}")
    public ResponseEntity<?> showSongList(@PathVariable Long playListNum, @PageableDefault(page = 0,size = 5)Pageable pageable){
//        // 페이지 번호와 페이지 크기를 설정하여 Pageable 객체 생성
//        Pageable pageable = PageRequest.of(page, size);

        // 플레이리스트 조회 및 음악 조회 로직 실행
        PlayListDTO.ResponseMusicList responseMusicList = playListService.showMusicList(playListNum, pageable);

        return new ResponseEntity<>(responseMusicList, HttpStatus.FOUND);
    }

    // 플레이리스트 이름 수정 -> Patch메서드 사용
    @PatchMapping("/{playListNum}/{newName}")
    public ResponseEntity<?> updatePlayListName(@PathVariable Long playListNum,@PathVariable String newName){
        PlayListDTO.ResponseMusicList responseMusicList = playListService.updatePlayListName(playListNum, newName);
        return new ResponseEntity<>(responseMusicList,HttpStatus.OK);
    }

    //플레이리스트 에서 음악제거
    @PostMapping("/delete/{playListNum}")
    public ResponseEntity<?> deleteMusicInPlayList(@PathVariable Long playListNum, @RequestBody MusicDTO.DeleteMusicListNum deleteMusicListNum){
        PlayListDTO.ResponseMusicList result=playListService.deleteMusicInPlayList(playListNum,deleteMusicListNum);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //플레이리스트 삭제
    @DeleteMapping("/{playListNum}")
    public ResponseEntity<?> deletePlayList(@PathVariable Long playListNum){
        playListService.deletePlayList(playListNum);
        return new ResponseEntity<>("플레이리스트 삭제완료",HttpStatus.OK);
    }


}
