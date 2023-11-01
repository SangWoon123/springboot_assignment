package com.week.gdsc.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String artist;
    private String title;
    private int playTime;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    // 분,초 형식으로 변경
    public String formationTime(){
        int minutes = playTime / 60;
        int seconds = playTime % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    public void setPlaylist(Playlist playlist) {
        if (this.playlist != null) {
            this.playlist.getMusicList().remove(this);
        }
        if (playlist != null && !playlist.getMusicList().contains(this)) {
            playlist.getMusicList().add(this);
        }
        this.playlist = playlist;
    }

}
