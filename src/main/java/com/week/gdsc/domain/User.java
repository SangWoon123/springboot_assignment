package com.week.gdsc.domain;

import com.week.gdsc.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String password;

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .userId(userDTO.getUserId())
                .password(userDTO.getPassword())
                .build();
    }
}
