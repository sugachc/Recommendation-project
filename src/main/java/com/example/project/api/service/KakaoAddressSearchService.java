package com.example.project.api.service;

import com.example.project.api.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoAddressSearchService {

    private final RestTemplate restTemplate;
    private final KakaoUrlBuilderService kakaoUrlBuilderService;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApikey;


    public KakaoApiResponseDto requestAddressSearch(String address){

        if(ObjectUtils.isEmpty(address))return null;

        URI uri=kakaoUrlBuilderService.buildUriByAddressSearch(address);

        HttpHeaders headers=new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION,"kakaoAK"+kakaoRestApikey);
        HttpEntity httpEntity=new HttpEntity<>(headers);

        //kakao api호출
        return restTemplate.exchange(uri, HttpMethod.GET,httpEntity,KakaoApiResponseDto.class).getBody();
    }

}