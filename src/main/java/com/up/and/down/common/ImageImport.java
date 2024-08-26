package com.up.and.down.common;

import lombok.extern.slf4j.Slf4j;

// 저장소에서 이미지 가져오기
@Slf4j
public class ImageImport {
    public String getImageUrl(String directoryName, String imageName) {

        log.debug("getImageUrl 호출 완료 !!!");
        // directoryName : 내 파일이 존재하는 디렉토리 입력
        // imageName : 가져오고 싶은 이미지 이름
        String imageUrl = "https://kr.object.ncloudstorage.com/up-bucket/";


        // StringBuilder를 사용하여 URL 구성
        StringBuilder str = new StringBuilder();
        str.append(imageUrl)
                .append(directoryName)
                .append("/")
                .append(imageName);

        log.debug("str : {}", str);

        return str.toString();
    }


}