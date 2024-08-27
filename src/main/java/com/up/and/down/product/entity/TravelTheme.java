package com.up.and.down.product.entity;

import lombok.Getter;

@Getter
public enum TravelTheme {
    NATURE("Nature", "자연"),
    CULTURE("Culture", "문화"),
    HISTORY("History", "역사"),
    FOOD("Food", "맛집"),
    FAMILY("Family", "가족"),
    RELAXATION("Relaxation", "휴식"),
    SHOPPING("Shopping", "쇼핑"),
    ROMANTIC("Romantic", "로맨틱");

    private final String displayName;
    private final String displayKorName;

    TravelTheme(String displayName, String displayKorName) {
        this.displayName = displayName;
        this.displayKorName = displayKorName;
    }

    public String getLowerCase() {
        return this.displayName.toLowerCase();
    }
}
