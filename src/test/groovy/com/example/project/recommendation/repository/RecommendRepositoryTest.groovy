package com.example.project.recommendation.repository

import com.example.project.AbstractIntegrationContainerBaseTest
import com.example.project.recommendation.entity.Recommendation
import com.example.project.recommendation.service.RecommendRepositoryService
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

class RecommendRepositoryTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private RecommendRepository recommendRepository



    void setup() {
        recommendRepository.deleteAll()
    }


    def "Recommendation Repository save"() {

        given:
        String address = "서울 특별시 성북구 종암동"
        String name = "은혜 약국"
        double latitude = 36.11
        double longitude = 128.11

        def recommendation = Recommendation.builder()
                .recommendAddress(address)
                .recommendName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def result = recommendRepository.save(recommendation)


        then:
        result.getRecommendAddress() == address
        result.getRecommendName() == name
        result.getLatitude() == latitude
        result.getLongitude() == longitude


    }

    def "PharmacyRepository saveAll"() {

        given:
        String address = "서울 특별시 성북구 종암동"
        String name = "은혜 약국"
        double latitude = 36.11
        double longitude = 128.11

        def recommendation = Recommendation.builder()
                .recommendAddress(address)
                .recommendName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()
        when:
        recommendRepository.saveAll(Arrays.asList(recommendation))
        def result = recommendRepository.findAll()

        then:
        result.get(0).getRecommendAddress() == address
        result.get(0).getRecommendName() == name
        result.get(0).getLatitude() == latitude
        result.get(0).getLongitude() == longitude
    }


    def "BaseTimeEntity_등록"() {

        given:
        //현재시간
        LocalDateTime now = LocalDateTime.now()
        String address = "서울 특별시 성북구 종암동"
        String name = "은혜 약국"

        // 주소,이름 세팅
        def recommendation = Recommendation.builder()
                .recommendAddress(address)
                .recommendName(name)
                .build()
        when:
        recommendRepository.save(recommendation)
        def result = recommendRepository.findAll()

        then:
        //생성시간이 현재시간,수정시간 보다 최근인지 확인
        result.get(0).getCreateDate().isAfter(now)
        result.get(0).getModifiedDate().isAfter(now)
    }





}