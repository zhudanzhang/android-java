# 基于位置的服务

## 基于位置的服务简介

**1. 什么是LBS？**

基于位置的服务（Location-Based Services，简称LBS）是一种利用定位技术确定移动设备位置的服务。虽然这种技术早已存在，但随着移动互联网的兴起和智能设备的普及，LBS技术近年来变得十分流行。

**2. 为什么LBS最近几年才流行？**
- **过去：**  
  移动设备功能有限，即使能定位，也只能单纯获取位置，无法进一步操作。
- **现在：**  
  借助 Android 等智能系统，可以基于位置提供丰富多彩的功能。例如：
  - 天气应用自动选择城市
  - 发送微博时展示所在位置
  - 使用地图应用查询路线

**3. 定位技术的核心**

LBS的核心在于 **确定用户所在的位置**，通常有以下两种定位技术：

**（1）GPS定位**
- **工作原理：**  
  利用设备内置的 GPS 硬件与卫星交互获取经纬度信息
- **优点：**  
  - 定位精度高
- **缺点：**  
  - 只能在室外使用，室内难以接收卫星信号

**（2）网络定位**
- **工作原理：**  
  通过测量设备与附近三个基站之间的距离，使用三角定位法获取大致位置
- **优点：**  
  - 室内外均可使用
- **缺点：**  
  - 精度一般

**4. Android 中的定位支持**

Android 对 GPS 和网络定位都提供了 API 支持，但在国内环境下存在以下问题：
- **网络定位：**  
  由于 Google 服务在国内不可用，相关 API 失效
- **GPS 定位：**  
  不依赖网络，但只能在室外使用。室内开发时，可能出现无法定位的情况

**5. 使用第三方SDK**

考虑到上述限制，本书不介绍 Android 原生定位 API，而是推荐使用国内第三方公司提供的 SDK。国内在 LBS 领域表现优秀的公司包括：
- **百度地图 SDK**
- **高德地图 SDK**

## 使用高德地图定位 

### 申请API Key

1. **打开高德地图控制台**  
   - 访问 [高德地图控制台](https://console.amap.com/dev/key/app)。

2. **创建应用并获取 Key**  
   1. **创建应用**  
      - 点击“创建应用”，填写应用名称（如 `LBSTest`）。

   2. **添加 Key**  
      - **Key 名称：** `LBSTest`  
      - **服务平台：** 选择 **Android 平台**  
      - **包名：** `com.example.demo37`  
      - **发布版 SHA1：** 需本地获取。

3. **获取发布版 SHA1**  
   1. **开发版 SHA1（调试签名文件）**  
      使用 `debug.keystore` 自动生成的指纹：  
      - 在 **Android Studio** 中：  
        - 右侧工具栏中找到 **Gradle**，路径：`Tasks -> android -> signingReport`。  
        - 执行任务后，查看生成的 SHA1。

   2. **发布版 SHA1（正式签名文件）**  
      如果需要正式发布：  
      - 通过命令行工具 **`keytool`** 获取 SHA1 指纹：  
        ```bash
        keytool -list -v -keystore <签名文件路径>
        ```

4. **临时方案**  
   - 如果尚未生成正式签名文件，可暂时将 **开发版 SHA1** 填入 **发布版 SHA1** 位置用于测试。  
   - 正式发布时，再替换为发布版的 SHA1 指纹。

5. **创建项目：**  
   在 Android Studio 中创建 `demo37` 项目。

6. **下载 SDK：**  
   [下载高德地图 SDK（2D 地图合包）](https://lbs.amap.com/api/android-sdk/download)。

7. **复制 SDK 文件：**  
   - 将下载好的 SDK 中的 `jar` 文件复制到项目的 `libs` 目录下。  
   - 如果 `libs` 目录不存在，可以手动在 `app` 模块下创建。

8. **修改 `build.gradle` 文件：**  
   在 `app/build.gradle` 文件的 `dependencies` 块中添加以下代码：

   ```gradle
   dependencies {
       implementation fileTree(dir: 'libs', include: ['*.jar'])
   }
   ```

9. **同步项目：**  
   在 Android Studio 中点击 `Sync Now`，确保依赖成功加载。

### 确定自己位置的经纬度

1. **activity_main.xml**
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent" >
        <TextView
            android:id="@+id/position_text_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
    </LinearLayout>
    ```
2. **AndroidManifest.xml**
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="com.example.demo37">
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <!--  网络定位  -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <!--  wifi定位 -->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <!-- 基站定位 -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
        
        <application
            android:allowBackup="true"
            android:icon="@mipmap/ic_launcher"
            android:label="@string/app_name"
            android:supportsRtl="true"
            android:theme="@style/Theme.Demo37">
            <!-- 设置key -->
            <meta-data
                android:name="com.amap.api.v2.apikey"
                android:value="b550db92253d12d1801576a1dc27248c"/>
            <activity android:name=".MainActivity">
                <intent-filter>
                    <action android:name="android.intent.action.MAIN" />
                    <category android:name="android.intent.category.LAUNCHER" />
                </intent-filter>
            </activity>
        </application>
    </manifest>
    ```
3. **MainActivity.java**
    ```java
    public class MainActivity extends AppCompatActivity implements AMapLocationListener {

        private AMapLocationClient mLocationClient;
        private AMapLocationClientOption mLocationOption;
        private TextView positionTextView;

        private static final int REQUEST_CODE_PERMISSIONS = 1; // 权限请求码

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // 初始化控件
            positionTextView = findViewById(R.id.position_text_view);

            // 检查和申请权限
            checkPermissions();
        }

        // 检查权限
        private void checkPermissions() {
            // 权限列表
            String[] permissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); // 高精度模式
                mLocationOption.setInterval(2000);  // 设置定位间隔，单位毫秒

                // 设置定位参数
                mLocationClient.setLocationOption(mLocationOption);

                // 启动定位
                mLocationClient.startLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 定位回调方法
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    // 定位成功
                    double latitude = amapLocation.getLatitude();
                    double longitude = amapLocation.getLongitude();

                    // 获取定位方式
                    String locationType = getLocationType(amapLocation.getLocationType());

                    // 更新UI
                    positionTextView.setText("纬度: " + latitude + "\n经度: " + longitude + "\n定位方式: " + locationType);
                } else {
                    // 定位失败
                    positionTextView.setText("定位失败: " + amapLocation.getErrorInfo());
                }
            }
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
        }
    }
    ```
4. 流程总结
    1. **权限检查与申请**：
        - **检查权限**：在 `onCreate()` 方法中，检查应用所需的权限（`ACCESS_FINE_LOCATION`、`READ_PHONE_STATE`）是否已经授予。
        - **权限申请**：如果发现有未授权的权限，将其加入 `permissionList` 列表，并使用 `ActivityCompat.requestPermissions()` 申请权限。
    2. **权限结果处理**：
        - **回调方法**：当权限申请完成后，`onRequestPermissionsResult()` 方法被触发。
        - **检查授权情况**：遍历所有请求的权限结果，如果有任何权限未被授权，则提示用户并退出应用；如果所有权限都被授予，继续进行下一步。
    3. **初始化定位**：
        - **初始化定位客户端**：如果权限申请通过，调用 `initLocation()` 方法初始化定位客户端 (`AMapLocationClient`)。
        - **设置定位参数**：配置定位模式（高精度模式）和定位间隔。
        - **启动定位服务**：通过 `mLocationClient.startLocation()` 启动定位。
    4. **定位结果回调**：
        - **获取定位信息**：定位成功后，`onLocationChanged()` 方法被触发，获取定位信息，包括经纬度、定位方式等。
        - **更新 UI**：根据定位结果更新界面，显示经纬度和定位方式。如果定位失败，显示错误信息。
    5. **定位方式说明**：
        - **获取定位方式**：根据返回的定位类型（如 GPS 定位、网络定位等），获取具体的定位方式并显示。
    6. **资源释放**：
        - **停止定位**：在 `onDestroy()` 方法中，调用 `mLocationClient.stopLocation()` 停止定位。
        - **销毁客户端**：调用 `mLocationClient.onDestroy()` 销毁定位客户端，释放资源。
    7. **程序退出**：
        - **权限未授权**：如果有权限未被授权，应用会提示用户并通过 `finish()` 方法退出程序。

### 选择定位模式

在Android中，主要有两种定位方式：**GPS定位**和**网络定位**。  


**1. 启用GPS定位功能**

1. 用户主动启用GPS功能  
    - GPS定位必须由用户在设备设置中手动启用。  
    - 进入 **设置 → 位置信息**，可以通过顶部开关控制定位功能的开启或关闭。
2. 定位模式选择
    在 **设置 → 位置信息 → 模式** 中，可以选择以下三种定位模式：
    - **高精确度模式**：优先使用 GPS 定位，当 GPS 信号弱或不可用时，自动切换为 网络定位（包括 Wi-Fi 和移动网络）
    - **节电模式**：仅使用 网络定位（包括 Wi-Fi 和移动网络），不使用 GPS
        - 适用于对电量消耗敏感且对精度要求较低的场景，如天气查询、社交签到等
    - **仅限设备模式**：强制使用 GPS 进行定位，不依赖网络信号
        - 适用于在室外且需要高精度定位的场景，例如户外运动、登山等

> **注意**：启用GPS不会立即增加电量消耗，只有在实际定位时才会消耗更多电量


**2. 切换到GPS定位（基于高德地图SDK）**

1. **确保设备设置为高精确度模式或仅限设备模式**  
   这样可以允许高德地图SDK使用GPS功能

2. **在代码中指定使用GPS定位**  
   通过设置高德定位SDK的选项，可以选择具体的定位模式

**3. 高德定位模式设置**

高德地图SDK中提供了三种定位模式，分别为：
- **Hight_Accuracy（高精度模式）**  
  优先使用GPS定位；若GPS信号不可用，则使用网络定位
- **Battery_Saving（节电模式）**  
  仅使用 网络定位（包括 Wi-Fi 和移动网络），不使用 GPS
- **Device_Sensors（仅设备模式）**  
  强制只使用GPS进行定位

**4. 强制使用GPS定位**

强制使用GPS定位的示例代码：

```java
mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors); // 传感器模式，仅使用GPS
```

**5. 运行测试**

1. 修改代码后重新运行应用程序。
2. 将手机移动到室外，确保设备可以接收到GPS信号。
3. 程序将切换到GPS定位模式，获取更精确的位置信息。

### 看得懂的位置信息

* 使用高精度定位模式
* 需要联网才能获取到地址信息

```java
//设置是否返回地址信息（默认返回地址信息）
mLocationOption.setNeedAddress(true);
```

```java
String country = amapLocation.getCountry();     // 国家
String province = amapLocation.getProvince();   // 省
String city = amapLocation.getCity();           // 市
String district = amapLocation.getDistrict();   // 区
String street = amapLocation.getStreet();       // 街道
String address = amapLocation.getAddress();     // 完整地址
```

## 使用高德地图

### 移动到我的位置

**1. activity_main.xml**

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >
    <TextView
        android:id="@+id/position_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="gone"/>
    <com.amap.api.maps2d.MapView
        android:id="@+id/map"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

**2. MainActivity.java**

```java
private MapView mapView;

mapView = findViewById(R.id.map);
mapView.onCreate(savedInstanceState);  // 初始化地图

 mapView.onDestroy();  // 销毁地图
```

**3. 移动到我的位置**

```java

AMap aMap = mapView.getMap();
LatLng latLng = new LatLng(latitude, longitude);
aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));  // 移动地图并放大
```

### 显示在地图上

[实现定位蓝点（5.0.0版本后）](https://lbs.amap.com/api/android-sdk/guide/create-map/mylocation)

**MainActivity.java**

```java
private AMap aMap;

 // 获取地图实例
aMap = mapView.getMap();

// 设置蓝点样式
MyLocationStyle myLocationStyle;
myLocationStyle = new MyLocationStyle(); // 设置定位蓝点的显示类型
myLocationStyle.interval(2000); // 设置定位间隔
aMap.setMyLocationStyle(myLocationStyle); // 设置定位蓝点样式
aMap.setMyLocationEnabled(true); // 启动定位图层
```