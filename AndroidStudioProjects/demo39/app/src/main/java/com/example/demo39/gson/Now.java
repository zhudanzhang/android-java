package com.example.demo39.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    @SerializedName("tmp")
    public String temperature;  // 当前温度
    @SerializedName("cond")
    public More more;           // 天气信息

    public class More {
        @SerializedName("txt")
        public String info; // 天气描述
    }
}