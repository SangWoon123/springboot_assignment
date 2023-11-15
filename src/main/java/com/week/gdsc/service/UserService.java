package com.week.gdsc.service;

import com.week.gdsc.config.TokenProvider;
import com.week.gdsc.domain.User;
import com.week.gdsc.dto.TokenDTO;
import com.week.gdsc.dto.UserDTO;
import com.week.gdsc.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    public UserDTO createUser(final UserDTO userDTO){
        if(userDTO==null || userDTO.getUsername()==null){
            throw new RuntimeException("Invalid arguments");
        }

        User user= User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encrypt(userDTO.getUsername(),userDTO.getPassword()))
                .build();

        if(userRepository.existsByUsername(user.getUsername())){
            log.warn("Username already exists {}",user.getUsername());
            throw new RuntimeException("Username already exists");
        }

        User savedUser = userRepository.save(user);

        return UserDTO.toUserDTO(savedUser);
    }

    public User findByUsername(String username){
        User findUser = userRepository.findByUsername(username);

        if(findUser==null || findUser.getUsername()==null){
            throw new RuntimeException("Invalid arguments");
        }

        return findUser;
    }

    public UserDTO signInUser(UserDTO userDTO) {
        User user = getByCredentials(userDTO.getUsername(), userDTO.getPassword());
        if (user != null) {
            final TokenDTO token = tokenProvider.createToken(user);
            return UserDTO.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .build();
        }

        return null;
    }

    /*
    패스워드를
 */
    private User getByCredentials(String username, String password) {
        final User originalUser=userRepository.findByUsername(username);

        if(originalUser!=null && passwordEncoder.encrypt(username,password).equals(originalUser.getPassword())) {
            log.info("getByCredentials: 사용자 인증 완료");
            return originalUser;
        }

        return null;
    }

}
