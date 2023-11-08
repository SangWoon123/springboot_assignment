package com.week.gdsc.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    PLAYLIST_NAME_NOT_FOUND(404,"플레이리스트 이름을 입력해주세요"),
    MUSIC_NOT_FOUND(404, "해당음악이 존재하지 않습니다."),
    MUSIC_NOT_FOUND_FOR_DELETE(404, "삭제하려는 음악이 존재하지 않습니다."),
    PLAYLIST_NOT_FOUND(404, "플레이리스트가 존재하지 않습니다.");

    private int status;
    private String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}