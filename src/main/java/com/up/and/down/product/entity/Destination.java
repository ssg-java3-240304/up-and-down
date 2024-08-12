package com.up.and.down.product.entity;

import lombok.Getter;

@Getter
public enum Destination {
    JEJU("제주도"),
    GANGWON("강원"),
    JEOLLA("전라"),
    BUSAN("부산"),
    GEOJE("거제"),
    NAMHAE("남해"),
    TONGYEONG("통영"),
    GYEONGJU("경주"),
    YEOSU("여수"),
    ULLEUNGDO("울릉도");

    private final String krName;

    Destination(String krName) {
        this.krName = krName;
    }
}
