package com.example.demo39.gson;

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;  // 空气质量指数
        public String pm25; // PM2.5浓度
    }
}