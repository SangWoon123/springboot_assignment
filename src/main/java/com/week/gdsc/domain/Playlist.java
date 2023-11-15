package com.week.gdsc.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // music 읽기만 가능 연관관계의 주인 X
    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY)
    private List<Music> musicList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public void updateName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
