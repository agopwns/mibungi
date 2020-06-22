package com.example.mibungi.dto;

import com.google.gson.annotations.SerializedName;

public class EstimationDTO {

    @SerializedName("negative")
    String negative;
    @SerializedName("positive")
    String positive;


    public EstimationDTO(String negative, String positive) {
        this.positive = positive;
        this.negative = negative;
    }

    public String getPositive() {
        return positive;
    }

    public String getNegative() {
        return negative;
    }
}
