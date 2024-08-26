package com.up.and.down.chatroom.entity;

import lombok.Getter;
@Getter
public enum Category {
    ADVENTURE( "모험"),
    RELAXATION("휴식"),
    CULTURE("문화"),
    NATURE( "자연"),
    FOOD( "맛집"),
    HISTORY("역사"),
    LUXURY( "럭셔리"),
    FAMILY( "가족"),
    ROMANTIC( "로맨틱"),
    SHOPPING( "쇼핑"),
    FESTIVAL( "축제");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
}
