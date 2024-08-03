package com.up.and.down.chatroom.entity;

import lombok.Getter;
@Getter
public enum Category {
    HIKING("등산"),
    WALKING("산책"),
    CAMPING("캠핑"),
    FESTIVAL("축제"),
    BEACH("바다"),
    HEALING("힐링"),
    FISHING("낚시"),
    OTHERS("기타");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
}
