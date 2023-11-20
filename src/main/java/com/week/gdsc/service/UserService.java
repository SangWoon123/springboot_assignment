package com.week.gdsc.service;

import com.week.gdsc.config.TokenProvider;
import com.week.gdsc.domain.User;
import com.week.gdsc.dto.request.UserRequest;
import com.week.gdsc.dto.response.TokenResponse;
import com.week.gdsc.dto.response.UserResponse;
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
    public UserResponse.SignUpResponse createUser(final UserRequest userRequest){
        if(userRequest ==null || userRequest.getUsername()==null){
            throw new RuntimeException("Invalid arguments");
        }

        User user= User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encrypt(userRequest.getUsername(), userRequest.getPassword()))
                .build();

        if(userRepository.existsByUsername(user.getUsername())){
            log.warn("Username already exists {}",user.getUsername());
            throw new RuntimeException("Username already exists");
        }

        User savedUser = userRepository.save(user);

        return UserResponse.SignUpResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .build();
    }

    public User findByUsername(String username){
        User findUser = userRepository.findByUsername(username);

        if(findUser==null || findUser.getUsername()==null){
            throw new RuntimeException("Invalid arguments");
        }

        return findUser;
    }

    public UserResponse signInUser(UserRequest userRequest) {
        User user = getByCredentials(userRequest.getUsername(), userRequest.getPassword());
        if (user != null) {
            final TokenResponse token = tokenProvider.createToken(user);
            return UserResponse.builder()
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
            return originalUser;
        }

        return null;
    }

}
