package com.week.gdsc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // music 읽기만 가능 연관관계의 주인이아
    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY)
    private List<Music> musicList;

    public void updateName(String name){
        this.name=name;
    }

}
