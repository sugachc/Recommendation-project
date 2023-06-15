package com.example.project.recommendation.service;

import com.example.project.recommendation.entity.Recommendation;
import com.example.project.recommendation.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j //로그어노테이션
@Service
@RequiredArgsConstructor //final이 붙은 필드생성자 자동주입
public class RecommendRepositoryService {

    private final RecommendRepository recommendRepository;

    @Transactional
    public void updateAddress(Long id,String address){
        Recommendation entity=recommendRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)){
            log.error("[RecommendRepository updateAddress] not found",id);
            return;
        }

        entity.changeRecommendAddress(address);
    }

    public void updateAddressWithoutTransaction(Long id,String address){
        Recommendation entity=recommendRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)){
            log.error("[RecommendRepository updateAddress] not found",id);
            return;
        }

        entity.changeRecommendAddress(address);
    }

    @Transactional(readOnly = true)
    public List<Recommendation> findAll(){
        return recommendRepository.findAll();
    }


}
