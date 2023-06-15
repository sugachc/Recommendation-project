package com.example.project.recommendation.service

import com.example.project.AbstractIntegrationContainerBaseTest
import com.example.project.recommendation.entity.Recommendation
import com.example.project.recommendation.repository.RecommendRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class RecommendRepositoryServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private RecommendRepositoryService recommendRepositoryService;

    @Autowired
    private RecommendRepository recommendRepository;

    def setup(){
        recommendRepository.deleteAll()
    }

    //주소변경
    def "RecommendRepository update - dirty checking success"() {

        given:
        String inputAddress = "서울 특별시 성북구 종암동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = "은혜 약국"

        //builder패턴으로 Recommedation 엔티티 생성
        def recommendation = Recommendation.builder()
                .recommendAddress(inputAddress)
                .recommendName(name)
                .build()

        when:
        def entity = recommendRepository.save(recommendation)
        //파라미터(아이디값,수정주소값) 전달해서 주소변경 메소드실행
        recommendRepositoryService.updateAddress(entity.getId(), modifiedAddress)

        //변경결과 조회해옴
        def result = recommendRepository.findAll()

        //조회한 변경결과==수정주소값 같으면 테스트성공
        then:
        result.get(0).getRecommendAddress() == modifiedAddress
    }


    //@Transactional 어노테이션없는 버전(더티체킹실패)
    def "RecommendRepository update - dirty checking fail"() {

        given:
        String inputAddress = "서울 특별시 성북구 종암동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = "은혜 약국"

        def recommendation = Recommendation.builder()
                .recommendAddress(inputAddress)
                .recommendName(name)
                .build()

        when:
        def entity = recommendRepository.save(recommendation)
        recommendRepositoryService.updateAddressWithoutTransaction(entity.getId(), modifiedAddress)

        def result = recommendRepository.findAll()

        then:
        result.get(0).getRecommendAddress() == modifiedAddress
    }





}
