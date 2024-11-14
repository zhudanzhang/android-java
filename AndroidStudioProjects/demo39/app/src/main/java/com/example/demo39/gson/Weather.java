package com.example.demo39.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    public String status;  // 请求状态
    public Basic basic;    // 基本信息
    public AQI aqi;        // 空气质量信息
    public Now now;        // 当前天气
    public Suggestion suggestion; // 建议
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList; // 未来几天的天气预报
}