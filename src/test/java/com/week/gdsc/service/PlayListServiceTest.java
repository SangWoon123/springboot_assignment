package com.week.gdsc.service;

import com.week.gdsc.domain.Music;
import com.week.gdsc.domain.Playlist;
import com.week.gdsc.dto.PlayListDTO;
import com.week.gdsc.repository.PlayListRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
class PlayListServiceTest {

    @Autowired
    private PlayListService playListService;

    @Autowired
    private PlayListRepository playListRepository;

    @Test
    void showSongList() {

        // 테스트 데이터 생성
        Music music1=Music.builder().artist("가수1").title("제목1").playTime(300).build();
        Music music2=Music.builder().artist("가수2").title("제목2").playTime(300).build();
        List<Music> musics = Arrays.asList(music1,music2);

        Playlist playlist= Playlist.builder()
                .name("테스트 플리에리스트")
                .musicList(musics)
                        .build();

        Playlist save = playListRepository.save(playlist);

        // 플레이리스트 조회 및 음악 조회 로직 실행
        PlayListDTO.ResponseMusicList result=playListService.showMusicList(save.getId());

        // 결과 검증
        Assertions.assertEquals("테스트 플리에리스트",result.getPlayName());
        Assertions.assertEquals(music1.getTitle(),result.getMusicDTOList().get(0).getTitle());
    }

    @Test
    void testUpdatePlayListName(){
        //given
        Playlist playlist= Playlist.builder()
                .name("테스트 플리에리스트")
                .build();
        Playlist save = playListRepository.save(playlist);
        //when
        Playlist findPlayList=playListRepository.findById(save.getId()).orElseThrow(()->new IllegalArgumentException("플레이리스트가 존재하지 않습니다."));
        findPlayList.updateName("변경한 플레이리스트 이름");
        //then
        Assertions.assertEquals("변경한 플레이리스트 이름",findPlayList.getName());
    }

    @Test
    void testPlayListDelete(){
        //given
        // 테스트 데이터 생성
        Music music1=Music.builder().artist("가수1").title("제목1").playTime(300).build();
        Music music2=Music.builder().artist("가수2").title("제목2").playTime(300).build();
        List<Music> musics = Arrays.asList(music1,music2);

        Playlist playlist= Playlist.builder()
                .name("테스트 플리에리스트")
                .musicList(musics)
                .build();

        Playlist save = playListRepository.save(playlist);
        //when
        playListService.deletePlayList(save.getId());
        //then
        Playlist fetched = playListRepository.findById(save.getId()).orElse(null);
        Assertions.assertNull(fetched);
    }
}