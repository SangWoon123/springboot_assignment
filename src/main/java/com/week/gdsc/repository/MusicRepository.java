package com.week.gdsc.repository;

import com.week.gdsc.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music,Long> {
}
