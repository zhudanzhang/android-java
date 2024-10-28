package com.example.demo04;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

// 活动管理器
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    // 添加活动到集合
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    // 从集合中移除活动
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    // 销毁所有活动
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();  // 清空列表
    }
}
