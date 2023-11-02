package com.week.gdsc.repository;

import com.week.gdsc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    Boolean existsByUserId(String userId);
    User findByUserId(String userId);
}
