package com.week.gdsc.domain;

import com.week.gdsc.audit.Auditable;
import com.week.gdsc.dto.UserDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 객체의 일관성보장
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Playlist> playlist;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private RefreshToken refreshToken;

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();
    }

    public void updatePlaylist(Playlist playlist) {
        this.playlist.add(playlist);
        playlist.setUser(this);
    }
}
