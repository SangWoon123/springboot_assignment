package com.week.gdsc.service;

import com.week.gdsc.config.TokenProvider;
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
    private final TokenProvider tokenProvider;
    public UserDTO createUser(final User user){
        if(user==null || user.getUsername()==null){
            throw new RuntimeException("Invalid arguments");
        }

        final String id=user.getUsername(); // 개발 안정성을 위해 final 선언

        if(userRepository.existsByUsername(id)){
            log.warn("Username already exists {}",id);
            throw new RuntimeException("Username already exists");
        }

        User savedUser = userRepository.save(user);

        return UserDTO.toUserDTO(savedUser);
    }

    public User byUsername(String username){
        User findUser = userRepository.findByUsername(username);

        if(findUser==null || findUser.getUsername()==null){
            throw new RuntimeException("Invalid arguments");
        }

        return findUser;
    }


    /*
    패스워드를
 */
    public User getByCredentials(String username, String password,PasswordEncoder passwordEncoder) {
        final User originalUser=userRepository.findByUsername(username);

        if(originalUser!=null && passwordEncoder.encrypt(username,password).equals(originalUser.getPassword())) {
            log.info("getByCredentials: 사용자 인증 완료");
            return originalUser;
        }

        return null;
    }

    public User findUser(String authorizationHeader){
        String token = authorizationParser(authorizationHeader);
        String username = tokenProvider.validateAndGetUsername(token);

        return userRepository.findByUsername(username);

    }

    private String authorizationParser(String authorizationHeader){
        String token=null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        return token;
    }
}
