package com.week.gdsc.repository;

import com.week.gdsc.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken findByRefreshToken(String refreshToken);
}
