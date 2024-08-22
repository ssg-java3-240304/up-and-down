package com.up.and.down.common;

import lombok.extern.slf4j.Slf4j;

// 저장소에서 이미지 가져오기
@Slf4j
public class ImageImport {
    public static String name() {
        String imgSrc = "https://kr.object.ncloudstorage.com";
        String bucket = "up-bucket";
        // 가져오고 싶은 파일 위치
        String packages = "Package/패키지2.png";

        // StringBuilder를 사용하여 URL을 구성합니다.
        StringBuilder str = new StringBuilder();
        str.append(imgSrc)
                .append("/")  // 버킷과 패키지 경로를 구분하는 슬래시 추가
                .append(bucket)
                .append("/")
                .append(packages);
        log.debug("str : {}", str);
        // URL을 문자열로 변환하여 반환합니다.
        return str.toString();
    }
}