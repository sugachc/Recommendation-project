package com.example.project.direction.service

import com.example.project.recommendation.dto.RecommendDto
import com.example.project.recommendation.service.RecommendSearchService
import spock.lang.Specification

class DirectionServiceTest extends Specification {

    private RecommendSearchService recommendSearchService=Mock()

    private DirectionService directionService=new DirectionService(recommendSearchService)

    private List<RecommendDto> recommendList

    //테스트메소드 전에 실행되는 메소드
    def setup(){
        recommendList = new ArrayList<>()
        recommendList.addAll(
                RecommendDto.builder()
                        .id(1L)
                        .recommendName("돌곶이온누리약국")
                        .recommendAddress("주소1")
                        .latitude(37.61040424)
                        .longitude(127.0569046)
                        .build(),
                RecommendDto.builder()
                        .id(1L)
                        .recommendName("호수온누리약국")
                        .recommendAddress("주소2")
                        .latitude(37.60894036)
                        .longitude(127.029052)
                        .build()
        )
    }

    //정해진 반경10km만 검색되는지 테스트
    def "buildDirectionList-결과값이 거리순으로 정렬되는지 확인"(){

    }

    def "buildDirectionList-정해진 반경10km 내에 검색이 되는지 확인"(){

    }

}
