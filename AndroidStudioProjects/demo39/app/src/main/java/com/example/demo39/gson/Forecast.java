package com.example.demo39.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    public String date;  // 日期
    @SerializedName("tmp")
    public Temperature temperature; // 温度信息
    @SerializedName("cond")
    public More more;  // 天气信息

    public class Temperature {
        public String max; // 最高温度
        public String min; // 最低温度
    }

    public class More {
        @SerializedName("txt_d")
        public String info; // 白天气象描述
    }
}