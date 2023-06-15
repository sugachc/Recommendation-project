package com.example.project.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendDto {
    private Long id;
    private String recommendName;
    private String recommendAddress;
    private double latitude;
    private double longitude;
}
