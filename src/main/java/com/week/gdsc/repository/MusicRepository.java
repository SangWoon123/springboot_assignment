package com.week.gdsc.repository;

import com.week.gdsc.domain.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music,Long> {
    // 플레이리스트 ID로 음악을 페이지 단위로 조회
    Page<Music> findByPlaylistId(Long playlistId, Pageable pageable);
}
