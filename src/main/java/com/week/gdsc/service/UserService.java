package com.week.gdsc.service;

import com.week.gdsc.domain.User;
import com.week.gdsc.dto.UserDTO;
import com.week.gdsc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    public UserDTO create(final User user){
        if(user==null || user.getUserId()==null){
            throw new RuntimeException("Invalid arguments");
        }

        final String id=user.getUserId(); // 개발 안정성을 위해 final 선언

        if(userRepository.existsByUserId(id)){
            log.warn("Username already exists {}",id);
            throw new RuntimeException("Username already exists");
        }

        User savedUser = userRepository.save(user);

        return UserDTO.toUserDTO(savedUser);
    }

    public User byUserID(String userId){
        User findUser = userRepository.findByUserId(userId);

        if(findUser==null || findUser.getUserId()==null){
            throw new RuntimeException("Invalid arguments");
        }

        return findUser;
    }


    /*
    패스워드를
 */
    public User getByCredentials(String userId, String password) {
        User originalUser=userRepository.findByUserId(userId);

        if(originalUser!=null ) {
            log.info("getByCredentials: 사용자 인증 완료");
            return originalUser;
        }

        return null;
    }
}
