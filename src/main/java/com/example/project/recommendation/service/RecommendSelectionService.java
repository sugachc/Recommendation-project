package com.example.project.recommendation.service;

import com.example.project.api.dto.DocumentDto;
import com.example.project.api.dto.KakaoApiResponseDto;
import com.example.project.api.service.KakaoAddressSearchService;
import com.example.project.direction.dto.OutputDto;
import com.example.project.direction.entity.Direction;
import com.example.project.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendSelectionService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    private static final String ROAD_VIEW_BASE_URL="https://map.kakao.com/link/roadview/"; //로드뷰 위도,경도 정보필요

    private static final String DIRECTION_BASE_URL="https://map.kakao.com/link/map/"; //장소명 위도 경도

    public List<OutputDto> recommendLocationList(String address){

        //위치기반 데이터로 변환
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        //응답값 유효성체크
        if(Objects.isNull(kakaoApiResponseDto)|| CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())){
            log.error("[recommendLocationList fail] Input address:{}",address);
            return Collections.emptyList();
        }

        //위도 경도 기준으로 가까운 위치찾음
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        //거리계산 알고리즘으로 가까운 위치리스트 리턴
        //List<Direction> directionList = directionService.buildDirectionList(documentDto);
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        //위치리스트 저장
        return directionService.saveAll(directionList)
                .stream()//스트림열어서 map메소드로 변환
                .map(t->convertToOutputDto(t))//dto컨버팅 메소드
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction){

        //장소명,위도,경도
        String params = String.join(",", direction.getTargetRecommendName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params).toUriString();

        return OutputDto.builder()
                .recommendAddress(direction.getTargetRecommendName())
                .recommendAddress(direction.getTargetAddress())
                .directionUrl(result)
                .roadViewUrl(ROAD_VIEW_BASE_URL+direction.getTargetLatitude()+","+direction.getTargetLongitude())
                .distance(String.format("%.2f km",direction.getDistance()))
                .build();
    }

}
