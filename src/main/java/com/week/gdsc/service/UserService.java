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
    public User create(final User user){
        if(user==null || user.getUserId()==null){
            throw new RuntimeException("Invalid arguments");
        }

        final String id=user.getUserId(); // 개발 안정성을 위해 final 선언

        if(userRepository.existsByUserId(id)){
            log.warn("Username already exists {}",id);
            throw new RuntimeException("Username already exists");
        }

        return userRepository.save(user);
    }

    public User byUserID(String userId){
        User findUser = userRepository.findByUserId(userId);

        if(findUser==null || findUser.getUserId()==null){
            throw new RuntimeException("Invalid arguments");
        }

        return findUser;
    }


    public UserDTO.UserVerifyResponseDto authenticateUser(UserDTO userDTO){
        if(userRepository.existsByUserId(userDTO.getUserId())){
            return UserDTO.UserVerifyResponseDto.builder()
                    .isValid(true)
                    .build();
        }
        return UserDTO.UserVerifyResponseDto.builder()
                .isValid(false)
                .build();
    }

    public User getByCredentials(String userId, String password) {
        User originalUser=userRepository.findByUserId(userId);

        if(originalUser!=null) return originalUser;

        return null;
    }
}
