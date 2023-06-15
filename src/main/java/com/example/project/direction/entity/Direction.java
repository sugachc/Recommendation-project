package com.example.project.direction.entity;

import com.example.project.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "direction")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Direction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //고객정보
    private String inputAddress;
    private double inputLatitude;
    private double inputLongitude;

    //주소
    private String targetRecommendName;
    private String targetAddress;
    private double targetLatitude;
    private double targetLongitude;

    //고객주소와 공간주소 사이거리
    private double distance;

}
