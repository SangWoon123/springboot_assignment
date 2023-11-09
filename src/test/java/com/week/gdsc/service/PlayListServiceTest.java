package com.week.gdsc.service;

import com.week.gdsc.domain.Music;
import com.week.gdsc.domain.Playlist;
import com.week.gdsc.domain.User;
import com.week.gdsc.dto.PlayListDTO;
import com.week.gdsc.exception.BusinessLogicException;
import com.week.gdsc.repository.MusicRepository;
import com.week.gdsc.repository.PlayListRepository;
import com.week.gdsc.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before("")
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


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

        List<Playlist> playlists=new ArrayList<>();
        playlists.add(playlist);

        User user=User.builder().playlist(playlists).username("rlatkddns").password("1234").build();
        user.updatePlaylist(playlist);
        userRepository.save(user);

        Playlist save = playListRepository.save(playlist);

        musics.stream().forEach(music -> music.setPlaylist(playlist));

        // 공용 목 데이터 생성 및 사용자 이름 저장
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute("username", "rlatkddns");
        mockRequest.applyAttributes();

        // 페이지 번호와 페이지 크기를 설정하여 Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, 2);

        // 플레이리스트 조회 및 음악 조회 로직 실행
        PlayListDTO.ResponseMusicList result = playListService.showMusicList(save.getId(), pageable,mockRequest.getRequest());

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
        Music music3=Music.builder().artist("가수3").title("제목2").playTime(300).build();
        List<Music> musicList = Arrays.asList(music1,music2,music3);

        List<Music> musics = musicRepository.saveAll(musicList);

        Playlist playlist= Playlist.builder()
                .name("테스트 플리에리스트")
                .musicList(musics)
                .build();

        Playlist save = playListRepository.save(playlist);

        List<Playlist> playlists=new ArrayList<>();
        playlists.add(playlist);
        User user=User.builder().playlist(playlists).username("rlatkddns").password("1234").build();
        user.updatePlaylist(playlist);
        userRepository.save(user);

        // 공용 목 데이터 생성 및 사용자 이름 저장
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute("username", "rlatkddns");
        mockRequest.applyAttributes();

        // music에 어떤 플레이리스트에 있는지 설정
        musics.stream().forEach(music -> music.setPlaylist(playlist));


        List<Long> deleteMusicList=new ArrayList<>();
        deleteMusicList.add(music1.getId());
        deleteMusicList.add(music3.getId());

        //when 음악 삭제 실행
        playListService.deleteMusicInPlayList(save.getId(), deleteMusicList,mockRequest.getRequest());

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

        List<Playlist> playlists=new ArrayList<>();
        playlists.add(playlist);
        User user=User.builder().playlist(playlists).username("rlatkddns").password("1234").build();
        user.updatePlaylist(playlist);
        userRepository.save(user);

        Playlist save = playListRepository.save(playlist);

        // 공용 목 데이터 생성 및 사용자 이름 저장
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute("username", "rlatkddns");
        mockRequest.applyAttributes();

        //when
        playListService.deletePlayList(save.getId(),mockRequest.getRequest());
        //then
        Playlist fetched = playListRepository.findById(save.getId()).orElse(null);
        Assertions.assertNull(fetched);
    }

    // exception 테스트코드
    // 플레이리스트 생성할때 이름없는 경우 오류발생
    @Test
    public void testCreatePlayList_emptyName() {
        PlayListDTO.RequestPlaylistName playListDTO = PlayListDTO.RequestPlaylistName.builder().
                playlistName("").build();


        User user=User.builder().username("rlatkddns").password("1234").build();
        userRepository.save(user);


        // 공용 목 데이터 생성 및 사용자 이름 저장
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute("username", "rlatkddns");
        mockRequest.applyAttributes();

        Assertions.assertThrows(BusinessLogicException.class, () -> {
            playListService.createPlayList(playListDTO,mockRequest.getRequest());
        });
    }

}