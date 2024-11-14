package com.example.demo37;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AMapLocationListener {

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private TextView positionTextView;
    private MapView mapView;
    private AMap aMap;

    private boolean isFirstLocate = true; // 判断是否是第一次定位
    private static final int REQUEST_CODE_PERMISSIONS = 1; // 权限请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置隐私政策弹窗和用户同意状态
        AMapLocationClient.updatePrivacyShow(this, true, true); // 弹窗并显示隐私政策文本
        AMapLocationClient.updatePrivacyAgree(this, true);      // 设置用户已同意隐私政策

        // 初始化控件
        positionTextView = findViewById(R.id.position_text_view);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);  // 初始化地图
        // 获取地图实例
        aMap = mapView.getMap();
        // 检查和申请权限
        checkPermissions();
    }

    // 检查权限
    private void checkPermissions() {
        // 权限列表
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
        };

        // 用于存储未授权的权限
        List<String> permissionList = new ArrayList<>();

        // 检查每个权限是否已被授权
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        // 如果有未授权的权限，进行申请
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[0]),
                    REQUEST_CODE_PERMISSIONS);
        } else {
            // 权限已经获取，初始化定位
            initLocation();
        }
    }

    // 处理权限申请结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            boolean allPermissionsGranted = true;

            // 检查每个权限是否都被授权
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // 所有权限都被授予，初始化定位
                initLocation();
            } else {
                // 如果有权限被拒绝，关闭当前程序
                positionTextView.setText("定位权限未授权");
                Toast.makeText(this, "定位权限未授权，应用将退出", Toast.LENGTH_SHORT).show();
                finish(); // 关闭当前程序
            }
        }
    }

    // 初始化定位
    private void initLocation() {
        try {
            // 初始化定位客户端
            mLocationClient = new AMapLocationClient(getApplicationContext());
            mLocationClient.setLocationListener(this);  // 设置定位监听

            // 初始化定位参数
            mLocationOption = new AMapLocationClientOption();
//            此模式下，定位会尽量优先使用 GPS 定位（如果设备支持）。在 GPS 信号弱的情况下，会自动切换到基站定位或 Wi-Fi 定位。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 此模式下，定位只使用设备传感器（如 GPS），不使用基站或 Wi-Fi 定位。适用于需要严格依赖 GPS 定位的场景。
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            mLocationOption.setInterval(5000);  // 设置定位间隔，单位毫秒
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);

            // 启动定位
            mLocationClient.startLocation();

            // 设置蓝点样式
            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle(); // 设置定位蓝点的显示类型
            myLocationStyle.interval(2000); // 设置定位间隔
            aMap.setMyLocationStyle(myLocationStyle); // 设置定位蓝点样式
            aMap.setMyLocationEnabled(true); // 启动定位图层
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 定位回调方法
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                // 定位成功，调用提取的方法更新UI
//                updateLocationUI(amapLocation);

                int locationType = amapLocation.getLocationType();
              // 仅在 GPS 定位或网络定位时进行地图更新
                if (locationType == AMapLocation.LOCATION_TYPE_GPS
                        || locationType == AMapLocation.LOCATION_TYPE_NETWORK
                        || locationType == AMapLocation.LOCATION_TYPE_WIFI) {
                    // 调用 navigateTo 方法更新地图
                    navigateTo(amapLocation);
                } else {
                    Log.d("LocationType", "当前为其他类型的定位，不更新地图，"+ getLocationType(amapLocation.getLocationType()));
                }
            } else {
                String errorInfo = amapLocation.getErrorInfo();
                Toast.makeText(this, "定位失败: " + errorInfo, Toast.LENGTH_SHORT).show();
            }
        }
    }
    // 更新地图的方法
    private void navigateTo(AMapLocation amapLocation) {
        if (isFirstLocate) {
//            AMap aMap = mapView.getMap();
            LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.5f));  // 移动地图并放大
            isFirstLocate = false; // 更新首次定位标志
        }
    }
    private void updateLocationUI(AMapLocation amapLocation) {
        // 获取定位信息
        double latitude = amapLocation.getLatitude();
        double longitude = amapLocation.getLongitude();

        // 获取定位方式
        String locationType = getLocationType(amapLocation.getLocationType());

        // 获取详细地址信息
        String country = amapLocation.getCountry();     // 国家
        String province = amapLocation.getProvince();   // 省
        String city = amapLocation.getCity();           // 市
        String district = amapLocation.getDistrict();   // 区
        String street = amapLocation.getStreet();       // 街道
        String address = amapLocation.getAddress();     // 完整地址

        // 更新UI
        positionTextView.setText("纬度: " + latitude + "\n经度: " + longitude + "\n定位方式: " + locationType + "\n国家: " + country + "\n" +
                "省: " + province + "\n" +
                "市: " + city + "\n" +
                "区: " + district + "\n" +
                "街道: " + street + "\n" +
                "详细地址: " + address);
    }
    // 获取定位方式的字符串表示
    private String getLocationType(int locationType) {
        switch (locationType) {
            case AMapLocation.LOCATION_TYPE_GPS:
                return "GPS定位";
            case AMapLocation.LOCATION_TYPE_SAME_REQ:
                return "相同请求定位";
            case AMapLocation.LOCATION_TYPE_FIX_CACHE:
                return "定位缓存";
            case AMapLocation.LOCATION_TYPE_WIFI:
                return "Wi-Fi定位";
            case AMapLocation.LOCATION_TYPE_CELL:
                return "基站定位";
            case AMapLocation.LOCATION_TYPE_AMAP:
                return "高德定位";
            case AMapLocation.LOCATION_TYPE_OFFLINE:
                return "离线定位";
            case AMapLocation.LOCATION_TYPE_LAST_LOCATION_CACHE:
                return "最后位置缓存";
            case AMapLocation.LOCATION_COMPENSATION:
                return "定位补偿";
            case AMapLocation.LOCATION_TYPE_COARSE_LOCATION:
                return "粗略定位";
            case AMapLocation.LOCATION_TYPE_NETWORK:
                return "网络定位";
            default:
                return "未知定位";
        }
    }

    // 在Activity销毁时停止定位
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止定位
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();    // 销毁定位客户端
        }
        mapView.onDestroy();  // 销毁地图
    }
}