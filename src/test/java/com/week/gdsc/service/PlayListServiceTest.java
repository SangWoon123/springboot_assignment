package com.week.gdsc.service;

import com.week.gdsc.domain.Music;
import com.week.gdsc.domain.Playlist;
import com.week.gdsc.dto.PlayListDTO;
import com.week.gdsc.repository.MusicRepository;
import com.week.gdsc.repository.PlayListRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Autowired
    private MusicRepository musicRepository;
    @Test
    void showSongList() {

        // 테스트 데이터 생성
        Music music1=Music.builder().artist("가수1").title("제목1").playTime(300).build();
        Music music2=Music.builder().artist("가수2").title("제목2").playTime(300).build();
        List<Music> musics = Arrays.asList(music1,music2);

        musicRepository.saveAll(musics);

        Playlist playlist= Playlist.builder()
                .name("테스트 플리에리스트")
                .musicList(musics)
                        .build();

        Playlist save = playListRepository.save(playlist);

        musics.stream().forEach(music -> music.setPlaylist(playlist));

        // 페이지 번호와 페이지 크기를 설정하여 Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, 2);

        // 플레이리스트 조회 및 음악 조회 로직 실행
        PlayListDTO.ResponseMusicList result = playListService.showMusicList(save.getId(), pageable);

        // 결과 검증
        Assertions.assertEquals("테스트 플리에리스트",result.getPlayName());
        Assertions.assertEquals(2,result.getMusicDTOList().size());
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
    void testDeletePlayMusic(){
        //given
        // 테스트 데이터 생성
        Music music1=Music.builder().artist("가수1").title("제목1").playTime(300).build();
        Music music2=Music.builder().artist("가수2").title("제목2").playTime(300).build();
        List<Music> musicList = Arrays.asList(music1,music2);

        List<Music> musics = musicRepository.saveAll(musicList);

        Playlist playlist= Playlist.builder()
                .name("테스트 플리에리스트")
                .musicList(musics)
                .build();

        Playlist save = playListRepository.save(playlist);

        // music에 어떤 플레이리스트에 있는지 설정
        musics.stream().forEach(music -> music.setPlaylist(playlist));


        //when 음악 삭제 실행
        playListService.deleteMusicInPlayList(save.getId(), Arrays.asList(music1.getId()));

        //then
        Playlist result = playListRepository.findById(playlist.getId()).orElseThrow(() -> new IllegalArgumentException("플레이리스트가 존재하지 않습니다."));
        Assertions.assertEquals(1, result.getMusicList().size());
        Assertions.assertEquals(music2.getTitle(), result.getMusicList().get(0).getTitle());
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