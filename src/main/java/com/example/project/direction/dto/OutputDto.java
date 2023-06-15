package com.example.project.direction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutputDto {

    private String recommendName;    // 추천장소 주소명
    private String recommendAddress; // 추천 주소
    private String directionUrl;    // 길안내 url
    private String roadViewUrl;     // 로드뷰 url
    private String distance;        // 고객 주소와 추천 주소의 거리
}
