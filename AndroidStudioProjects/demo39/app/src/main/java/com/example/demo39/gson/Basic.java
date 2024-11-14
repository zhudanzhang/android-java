package com.example.demo39.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("city")
    public String cityName;  // 城市名
    @SerializedName("id")
    public String weatherId; // 天气ID
    public Update update;    // 更新信息

    public class Update {
        @SerializedName("loc")
        public String updateTime; // 更新时间
    }
}