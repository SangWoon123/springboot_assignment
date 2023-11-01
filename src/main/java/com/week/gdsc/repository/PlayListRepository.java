package com.week.gdsc.repository;

import com.week.gdsc.domain.Music;
import com.week.gdsc.domain.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayListRepository extends JpaRepository<Playlist,Long> {

}
