package com.example.project.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

    //검색된 주소
    @JsonProperty("address_name")
    private String addressName;

    //위도
    @JsonProperty("y")
    private double latitude;

    //경도
    @JsonProperty("x")
    private double longitude;

}
