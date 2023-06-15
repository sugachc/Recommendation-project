package com.example.project.recommendation.service;

import com.example.project.recommendation.dto.RecommendDto;
import com.example.project.recommendation.entity.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendSearchService {

    private final RecommendRepositoryService recommendRepositoryService;

    public List<RecommendDto> searchRecommendDtoList() {

        //redis

        //db
        return recommendRepositoryService.findAll()
                .stream()
                .map(this::convertRecommendDto)
                .collect(Collectors.toList());

    }

    //엔티티 받아서 Dto변환
    private RecommendDto convertRecommendDto(Recommendation recommendation){

        //빌더패턴 변환
        return RecommendDto.builder()
                .id(recommendation.getId())
                .recommendAddress(recommendation.getRecommendAddress())
                .recommendName(recommendation.getRecommendName())
                .latitude(recommendation.getLatitude())
                .longitude(recommendation.getLongitude())
                .build();
    }

}
