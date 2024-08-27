package com.up.and.down.chatroom.entity;

import lombok.Getter;
@Getter
public enum Category {
    NATURE("NATURE", "Nature", "자연"),
    CULTURE("CULTURE", "Culture", "문화"),
    HISTORY("HISTORY", "History", "역사"),
    FOOD("FOOD", "Food", "맛집"),
    FAMILY("FAMILY", "Family", "가족"),
    RELAXATION("RELAXATION", "Relaxation", "휴식"),
    SHOPPING("SHOPPING", "Shopping", "쇼핑"),
    ROMANTIC("ROMANTIC", "Romantic", "로맨틱");

    private final String name;
    private final String displayName;
    private final String displayKorName;

    Category(String name, String displayName, String displayKorName) {
        this.name = name;
        this.displayName = displayName;
        this.displayKorName = displayKorName;
    }
}
