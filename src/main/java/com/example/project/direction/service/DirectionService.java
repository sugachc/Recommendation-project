package com.example.project.direction.service;

import com.example.project.api.dto.DocumentDto;
import com.example.project.api.service.KakaoCategorySearchService;
import com.example.project.direction.entity.Direction;
import com.example.project.direction.repository.DirectionRepository;
import com.example.project.recommendation.dto.RecommendDto;
import com.example.project.recommendation.service.RecommendSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT= 3;//장소 최대 검색 갯수
    private static final double RADIUS_KM= 10.0; //반경10km이내
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    //의존성주입
    private final RecommendSearchService recommendSearchService;
    private final DirectionRepository directionRepository;

    private final Base62Service base62Service;
    private final KakaoCategorySearchService kakaoCategorySearchService;


    //응답주소 저장
    @Transactional
    public List<Direction> saveAll(List<Direction> directionList){

        if(CollectionUtils.isEmpty(directionList)) return Collections.emptyList();

        return directionRepository.saveAll(directionList);
    }

    @Transactional(readOnly = true)
    public String findDirectionUrlById(String encodedId) {

        Long decodedId = base62Service.decodeDirectionId(encodedId);
        Direction direction = directionRepository.findById(decodedId).orElse(null);

        String params = String.join(",", direction.getTargetRecommendName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));
        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();

        return result;
    }

    // search by category kakao api
    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {
        if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return kakaoCategorySearchService
                .requestPlaceCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .getDocumentList()
                .stream().map(resultDocumentDto ->
                        Direction.builder()
                                .inputAddress(inputDocumentDto.getAddressName())
                                .inputLatitude(inputDocumentDto.getLatitude())
                                .inputLongitude(inputDocumentDto.getLongitude())
                                .targetRecommendName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001) // km 단위
                                .build())
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }



    //문자열주소가 위도경도 위치기반 데이터로 변환된 결과값
    public List<Direction> buildDirectionList(DocumentDto documentDto){

        //validation check
        if(Objects.isNull(documentDto)) return Collections.emptyList();


        //주소데이터 조회
        return recommendSearchService.searchRecommendDtoList()
                .stream().map(recommendDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetRecommendName(recommendDto.getRecommendName())
                                .targetAddress(recommendDto.getRecommendAddress())
                                .targetLatitude(recommendDto.getLatitude())
                                .targetLongitude(recommendDto.getLongitude())
                                .distance(     //거리계산 알고리즘 사용해서 고객주소와 추천위치주소를 비교해서 거리채워넣음
                                        calculateDistance(documentDto.getLatitude(),documentDto.getLongitude(),
                                                recommendDto.getLatitude(),recommendDto.getLongitude())
                                )
                                .build())
                .filter(direction -> direction.getDistance()<=RADIUS_KM) //반경거리 필터링
                .sorted(Comparator.comparing(Direction::getDistance)) //가까운 순으로 필터링
                .limit(MAX_SEARCH_COUNT) //최대검색조건
                .collect(Collectors.toList());


        //장소 RecommendDto를 하나씩 돌면서 거리를 구할 것
        //거리계산 알고리즘으로 고객과 장소사이의 거리계산하고 sort

    }

    // Haversine formula
    //고객정보,약국주소정보 2개의 사이 거리를 구함
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }


}
