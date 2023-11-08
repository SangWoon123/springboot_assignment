package com.week.gdsc.repository;

import com.week.gdsc.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    Boolean existsByUsername(String username);
    User findByUsername(String username);
}
