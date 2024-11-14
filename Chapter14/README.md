# 开发酷欧天气 

中文：酷欧天气 
英文：Cool Weather

## 功能需求及技术可行性分析

**1. 功能需求分析**

在开始编码之前，首先需要进行需求分析，明确酷欧天气应用需要具备哪些基本功能。根据需求分析，酷欧天气至少应具备以下功能：

1. **罗列全国所有的省、市、县**。
2. **查看全国任意城市的天气信息**。
3. **自由切换城市，查看其他城市的天气**。
4. **手动更新以及后台自动更新天气的功能**。

尽管功能看似简单，但实现这些功能需要涉及到 UI、网络、数据存储、服务等技术。因此，开发过程将考验综合应用能力，尤其是在前面章节中所学习的技术。

**2. 技术可行性分析**

分析完需求后，接下来要进行技术可行性分析。首先需要考虑如何获取全国省市县的数据信息，以及如何获取每个城市的天气数据。

- **天气数据的获取**：随着时间的推移，许多免费的天气预报接口已经关闭。例如，本书第一版中使用的中国天气网接口也已经不可用了。为了确保天气数据的获取，使用了**和风天气**API，它每天提供3000次免费请求，适合学习使用。
  
- **全国省市县数据的获取**：目前也没有公开稳定的接口提供全国所有省市县的数据。为了解决这个问题，作者特别架设了一台服务器，用于提供全国省市县的数据信息，确保学习过程顺利进行。

**3. 具体实现步骤**

**（1）获取全国省市县数据**

通过访问指定的 URL，可以获取全国省市县的相关数据。例如：

- 获取全国所有省份数据：[http://guolin.tech/api/china](http://guolin.tech/api/china)
  
返回的数据为 JSON 格式，包含了所有省份的信息，包括省份名称和对应的 ID，例如：

```json
[{"id":1, "name":"北京"}, {"id":2, "name":"上海"}, {"id":3, "name":"天津"}, ... ]
```

- 获取某省内的所有城市：例如江苏省的 ID 是 16，访问 [http://guolin.tech/api/china/16](http://guolin.tech/api/china/16) 返回该省内的城市信息。
  
```json
[{"id":113, "name":"南京"}, {"id":114, "name":"无锡"}, {"id":115, "name":"镇江"}, ... ]
```

- 获取某市内的所有县或区：例如，苏州市的 ID 是 116，访问 [http://guolin.tech/api/china/16/116](http://guolin.tech/api/china/16/116) 返回苏州市的县区信息。
  
```json
[{"id":937, "name":"苏州", "weather_id":"CN101190401"}, {"id":938, "name":"常熟", "weather_id":"CN101190402"}, ... ]
```

通过这种方式，可以罗列全国所有的省、市、县。

**（2）获取天气信息**  

为了获取天气信息，需要使用到每个地区对应的 `weather_id`。例如，苏州的 `weather_id` 是 `CN101190401`。然后，通过访问和风天气的 API，可以获取该地区的天气信息。API 请求格式如下：

```
http://guolin.tech/api/weather?cityid=CN101190401&key=YOUR_API_KEY
```

返回的数据格式为：

```json
{
    "HeWeather": [
        {
            "status": "ok",
            "basic": {},
            "aqi": {},
            "now": {},
            "suggestion": {},
            "daily_forecast": []
        }
    ]
}
```

其中，`status` 表示请求状态，`basic` 包含城市基本信息，`aqi` 包含空气质量信息，`now` 包含当前天气信息，`suggestion` 包含生活建议，`daily_forecast` 包含未来几天的天气信息。

**（3）JSON解析**

获取到天气数据后，需要进行 JSON 解析工作，处理返回的数据并展示在应用中。这部分工作相对简单，可以通过 Android 提供的 JSON 解析工具（如 Gson 或 Jackson）来完成。

**4. 开源及代码托管**

为了便于管理和共享代码，[酷欧天气](https://github.com/guolindev/coolweather)将作为开源项目发布，并使用 GitHub 进行代码托管。

## 项目初始化

**1. 创建项目**

在`Android Studio`中新建一个`Android`项目，项目名叫作`CoolWeather`，包名叫作`com.example.demo39`。

**2. 目录结构**

- 在`com.example.demo39`新建包：
  - `db` 包：用于存放数据库模型相关的代码
  - `gson` 包：用于存放GSON模型相关的代码
  - `service` 包：用于存放服务相关的代码
  - `util` 包：用于存放工具相关的代码

**3. 添加依赖**

- `LitePal`：用于对数据库进行操作。右击未配置的jar包，点击`Add As Library`
- `OkHttp`：用于进行网络请求
- `GSON`：用于解析JSON数据
- `Glide`：用于加载和展示图片

```gradle
implementation fileTree(dir: 'libs', include: ['*.jar'])

// Gson
implementation 'com.google.code.gson:gson:2.8.9'

// OkHttp
implementation 'com.squareup.okhttp3:okhttp:4.9.3'

// Glide
implementation 'com.github.bumptech.glide:glide:4.15.1'
annotationProcessor 'com.github.bumptech.glide:compiler:4.15.1'

// CardView
implementation 'androidx.cardview:cardview:1.0.0'

// SwipeRefreshLayout
implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'

// CircleImageView
implementation 'de.hdodenhof:circleimageview:3.1.0'

// RecyclerView
implementation 'androidx.recyclerview:recyclerview:1.2.1'

// AppCompat
implementation 'androidx.appcompat:appcompat:1.3.1'

// Material Design
implementation 'com.google.android.material:material:1.4.0'

// ConstraintLayout
implementation 'androidx.constraintlayout:constraintlayout:2.1.0'

// JUnit 测试库
testImplementation 'junit:junit:4.13.2'

// AndroidX 测试支持
androidTestImplementation 'androidx.test.ext:junit:1.1.3'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
```

**4. 数据库的表结构**

建立3张表：`province`、`city`、`county`，分别用于存放省、市、县的数据信息。

对应到实体类中的话，就应该建立`Province`、`City`、`County`这3个类。

**在`db`包下新建一个`Province`类：**

```java
package com.example.demo39.db;

import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {
    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
```

- `id`：每个实体类中必须有的字段
- `provinceName`：记录省的名字
- `provinceCode`：记录省的代号

另外，LitePal中的每个实体类都必须继承自`LitePalSupport`类

**在`db`包下新建一个`City`类：**

```java
package com.example.demo39.db;

import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public int getCityCode() {
        return cityCode;
    }
    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
    public int getProvinceId() {
        return provinceId;
    }
    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
```

- `cityName`：记录市的名字
- `cityCode`：市的代号
- `provinceId`：当前市所属省的id值

**在`db`包下新建一个`County`类：**

```java
package com.example.demo39.db;

import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport {
    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
```

- `countyName`：记录县的名字
- `weatherId`：记录县所对应的天气id
- `cityId`：记录当前县所属市的id值

**5. 配置litepal.xml**

1. 在`app/src/main`目录下右击新建一个`assets`目录。
2. 在`assets`目录下新建一个`litepal.xml`文件，内容如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<litepal>
    <dbname value="cool_weather" />
    <version value="1" />
    <list>
        <mapping class="com.example.demo39.db.Province" />
        <mapping class="com.example.demo39.db.City" />
        <mapping class="com.example.demo39.db.County" />
    </list>
</litepal>
```

- `dbname`：指定数据库名为`cool_weather`。
- `version`：数据库版本为1。
- `mapping`：将`Province`、`City`和`County`类添加到映射列表中。

**配置LitePalApplication**

在`AndroidManifest.xml`文件中配置`LitePalApplication`，如下所示：

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.demo39">
    <application
        android:name="org.litepal.LitePalApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        ...
    </application>
</manifest>
```

完成以上配置后，数据库和表会在首次执行任意数据库操作时自动创建。

## 遍历全国省市县数据


**1. 与服务器交互的工具类 `HttpUtil`**

- 在`util`包下增加一个`HttpUtil`类
- 功能：发送HTTP请求，与服务器交互。
- **代码实现：**
  
  ```java
  public class HttpUtil {
      public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
          OkHttpClient client = new OkHttpClient();
          Request request = new Request.Builder().url(address).build();
          client.newCall(request).enqueue(callback);
      }
  }
  ```

- **要点**：
  - 使用 `OkHttp` 封装网络请求
  - 调用 `sendOkHttpRequest()` 方法传入地址和回调，异步处理服务器响应


**2. JSON 数据解析工具类 `Utility`**

- 在`util`包下增加一个`Utility`类
- 功能：解析服务器返回的省、市、县数据，保存到数据库。
- **方法实现**：

  **1. 解析省级数据**  
  ```java
  public static boolean handleProvinceResponse(String response) {
      if (!TextUtils.isEmpty(response)) {
          try {
              JSONArray allProvinces = new JSONArray(response);
              for (int i = 0; i < allProvinces.length(); i++) {
                  JSONObject provinceObject = allProvinces.getJSONObject(i);
                  Province province = new Province();
                  province.setProvinceName(provinceObject.getString("name"));
                  province.setProvinceCode(provinceObject.getInt("id"));
                  province.save();
              }
              return true;
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }
      return false;
  }
  ```

  **2. 解析市级数据**  
  ```java
  public static boolean handleCityResponse(String response, int provinceId) {
      if (!TextUtils.isEmpty(response)) {
          try {
              JSONArray allCities = new JSONArray(response);
              for (int i = 0; i < allCities.length(); i++) {
                  JSONObject cityObject = allCities.getJSONObject(i);
                  City city = new City();
                  city.setCityName(cityObject.getString("name"));
                  city.setCityCode(cityObject.getInt("id"));
                  city.setProvinceId(provinceId);
                  city.save();
              }
              return true;
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }
      return false;
  }
  ```

  **3. 解析县级数据**  
  ```java
  public static boolean handleCountyResponse(String response, int cityId) {
      if (!TextUtils.isEmpty(response)) {
          try {
              JSONArray allCounties = new JSONArray(response);
              for (int i = 0; i < allCounties.length(); i++) {
                  JSONObject countyObject = allCounties.getJSONObject(i);
                  County county = new County();
                  county.setCountyName(countyObject.getString("name"));
                  county.setWeatherId(countyObject.getString("weather_id"));
                  county.setCityId(cityId);
                  county.save();
              }
              return true;
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }
      return false;
  }
  ```

**3. 界面布局文件 `choose_area.xml`**

- 在`res/layout`目录中新建`choose_area.xml`布局
- 功能：定义选择省市县的界面布局。
- **布局结构**：
  
  ```xml
  <LinearLayout
      xmlns:android="http://schemas.android.com/apk/res/android"
      android:orientation="vertical"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:background="#fff">

      <RelativeLayout
          android:layout_width="match_parent"
          android:layout_height="?attr/actionBarSize"
          android:background="?attr/colorPrimary">

          <TextView
              android:id="@+id/title_text"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              android:layout_centerInParent="true"
              android:textColor="#fff"
              android:textSize="20sp" />

          <Button
              android:id="@+id/back_button"
              android:layout_width="25dp"
              android:layout_height="25dp"
              android:layout_marginLeft="10dp"
              android:layout_alignParentLeft="true"
              android:layout_centerVertical="true"
              android:background="@drawable/ic_back" />
      </RelativeLayout>

      <ListView
          android:id="@+id/list_view"
          android:layout_width="match_parent"
          android:layout_height="match_parent" />
  </LinearLayout>
  ```

1. **标题栏**
   - **高度**：设置为 `ActionBar` 的高度。
   - **背景色**：使用 `colorPrimary`。
   - **子控件**：
     - **TextView**：显示标题内容。
     - **Button**：用于返回操作，背景图设置为 `ic_back.png`。
   - **设计理由**：**自定义标题栏**是为了避免在碎片中直接使用 `ActionBar` 或 `Toolbar`，以防复用时出现不正常的效果。

2. **数据列表区域**
   - **ListView**：用于显示省、市、县数据。
   - **优点**：ListView 自动为每个子项添加分隔线，简化布局设计。
   - **选择理由**：相比 `RecyclerView`，`ListView` 在实现分隔线等功能时更简单，选择更优实现方案。

**4. 核心功能碎片 `ChooseAreaFragment`**

- **功能**：实现省市县数据的遍历和切换
- **代码实现**
```java
public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 县列表
     */
    private List<County> countyList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<? > parent, View view, int position,
                                    long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }
    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = LitePal.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }
    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }
    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = LitePal.where("cityid = ? ", String.valueOf(selectedCity.
                getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" +
                    cityCode;
            queryFromServer(address, "county");
        }
    }
    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     */
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText,
                            selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText,
                            selectedCity.getId());
                }
                if (result) {
//                    getActivity()
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
//                        getContext()
                        Toast.makeText(requireContext(), "加载失败", Toast.LENGTH_SHORT).
                                show();
                    }
                });
            }
        });
    }
    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
//            getActivity()
            progressDialog = new ProgressDialog(requireActivity());
            progressDialog.setMessage("正在加载．..");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
```

1. **初始化阶段**

- **`onCreateView()`**：
  - 初始化视图控件。
  - 获取 ListView、Button 等控件实例。
  - 使用 `ArrayAdapter` 绑定数据并设置到 `ListView`。
  
- **`onViewCreated()`**：
  - 为 ListView 和 Button 设置点击事件监听器。
  - 调用 `queryProvinces()` 开始加载省级数据。

2. **加载省级数据**

- **`queryProvinces()`**：
  - 设置界面标题为“中国”，并隐藏返回按钮，因为省级列表无需返回按钮。
  - 使用 LitePal 查询数据库获取省级数据。
  - 如果数据库没有数据，则构造请求地址，并调用 `queryFromServer()` 从服务器加载数据。

3. **从服务器加载数据**

- **`queryFromServer()`**：
  - 使用 `HttpUtil.sendOkHttpRequest()` 发送请求到服务器。
  - 在 `onResponse()` 回调中，使用 `Utility.handleProvincesResponse()` 解析服务器返回的数据并存储到数据库。
  - 解析完数据后，使用 `runOnUiThread()` 切换回主线程，并再次调用 `queryProvinces()` 来显示数据。

4. **用户交互逻辑**

- **省市县数据切换**：
  - 当用户点击 ListView 中的某个条目时，`onItemClick()` 会触发：
    - 如果当前是省级数据，调用 `queryCities()` 加载市级数据。
    - 如果当前是市级数据，调用 `queryCounties()` 加载县级数据。
  - `queryCities()` 和 `queryCounties()` 的实现与 `queryProvinces()` 类似，都是查询数据库，若没有数据则从服务器加载。

- **返回按钮**：
  - 当用户点击返回按钮时，根据当前的级别进行返回：
    - 从县级返回到市级。
    - 从市级返回到省级。
  - 返回到省级时，自动隐藏返回按钮。


**5. 碎片添加到活动中**

修改`activity_main.xml`中的代码

```xml
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <fragment
        android:id="@+id/choose_area_fragment"
        android:name="com.example.demo39.ChooseAreaFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</FrameLayout>
```

**6. 取消原生标题栏**

修改`res/values/styles.xml`中的代码

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
</resources>
```

修改 `AndroidManifest.xml`

```xml
<application
        ...
        android:theme="@style/AppTheme">
```

**7. 声明程序所需要的权限**

修改 `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## 显示天气信息

### 定义GSON实体类

**1. GSON实体类的定义**

GSON的使用非常简单，解析数据只需要一行代码即可完成，但首先需要定义好数据对应的实体类。

由于和风天气返回的数据内容较多，因此

只提取了其中的重要部分进行解析。


返回数据的大致格式：
```json
{
    "HeWeather": [
        {
            "status": "ok",
            "basic": {},
            "aqi": {},
            "now": {},
            "suggestion": {},
            "daily_forecast": []
        }
    ]
}
```

**2. Basic 实体类的定义**

根据该结构在gson包下创建 `Basic` 类：

- `city`表示城市名
- `id`表示城市对应的天气id
- `update`中的loc表示天气的更新时间

因为 `JSON` 数据中的字段名通常是由下划线分隔（例如 `user_name`），而 `Java` 中的字段名通常遵循驼峰命名法（例如 `userName`），这时直接进行解析会导致字段不匹配的问题。`@SerializedName` 注解解决了这个问题

```java
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
```

```json
"basic":{
    "city":"苏州",
    "id":"CN101190401",
    "update":{
        "loc":"2016-08-08 21:58"
    }
}
```

**3. AQI 实体类的定义**

`aqi` 部分包含空气质量和PM2.5数据。根据该结构创建 `AQI` 类：

```java
public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;  // 空气质量指数
        public String pm25; // PM2.5浓度
    }
}
```

```json
"aqi":{
    "city":{
        "aqi":"44",
        "pm25":"13"
    }
}
```

**4. Now 实体类的定义**

`now` 部分包含当前温度和天气状况。根据该结构创建 `Now` 类：

```java
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
```

```json
"now":{
    "tmp":"29",
    "cond":{
        "txt":"阵雨"
    }
}
```

**5. Suggestion 实体类的定义**

`suggestion` 部分包含舒适度、洗车建议、运动建议等信息。

根据该结构创建 `Suggestion` 类：

```java
public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carWash;
    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;  // 舒适度建议
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;  // 洗车建议
    }

    public class Sport {
        @SerializedName("txt")
        public String info;  // 运动建议
    }
}
```

```json
"suggestion":{
    "comf":{
        "txt":"白天天气较热，虽然有雨，但仍然无法削弱较高气温给人们带来的暑意，
            这种天气会让您感到不很舒适。"
    },
    "cw":{
        "txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水
            可能会再次弄脏您的爱车。"
    },
    "sport":{
        "txt":"有降水，且风力较强，推荐您在室内进行低强度运动；若坚持户外运动，
            请选择避雨防风的地点。"
    }
}
```

**6. Forecast 实体类的定义**

`daily_forecast` 部分包含未来几天的天气预报，是一个数组，数组中的每一项代表一天的天气信息。

**定义单日天气的实体类**，创建 `Forecast` 类来表示单日天气：

```java
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
```

```json
"daily_forecast":[
    {
        "date":"2016-08-08",
        "cond":{
            "txt_d":"阵雨"
        },
        "tmp":{
            "max":"34",
            "min":"27"
        }
    },
    {
        "date":"2016-08-09",
        "cond":{
            "txt_d":"多云"
        },
        "tmp":{
            "max":"35",
            "min":"29"
        }
    },
    ...
}
```

**7. Weather 实体类的定义**

创建一个 `Weather` 类，引用之前定义的各个实体类。注意，由于 `daily_forecast` 是一个数组，因此

使用 `List<Forecast>` 来表示：

```java
public class Weather {
    public String status;  // 请求状态
    public Basic basic;    // 基本信息
    public AQI aqi;        // 空气质量信息
    public Now now;        // 当前天气
    public Suggestion suggestion; // 建议
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList; // 未来几天的天气预报
}
```

返回的天气数据中还会包含一项`status`数据，成功返回`ok`，失败则会返回具体的原因，那么这里也需要添加一个对应的`status`字段。

### 编写天气界面

**1. 创建 WeatherActivity**
   - 首先，右击 `com.example.demo39` 包 → 选择 `New` → `Activity` → `Empty Activity`，创建一个 `WeatherActivity`
   - 将布局文件名指定为 `activity_weather.xml`

**2. 设计布局结构**
   - 由于所有的天气信息都将在同一个界面上显示，因此 `activity_weather.xml` 可能会变得非常长，导致布局代码混乱
   - 为了保持布局代码整洁，使用引入布局的方式，将界面分成多个部分并放在不同的布局文件中。这样可以通过在 `activity_weather.xml` 中引入这些布局来简化代码。

**3. 创建头部布局 (`title.xml`)**
   - 右击 `res/layout` 目录 → 选择 `New` → `Layout resource file`，新建一个名为 `title.xml` 的布局文件。
    - 该布局使用 `RelativeLayout`，包含两个 `TextView`：
        - `title_city`：显示城市名，居中显示。
        - `title_update_time`：显示更新时间，居右显示。
        
     ```xml
     <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
         android:layout_width="match_parent"
         android:layout_height="?attr/actionBarSize">
         <TextView
             android:id="@+id/title_city"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             android:layout_centerInParent="true"
             android:textColor="#fff"
             android:textSize="20sp" />
         <TextView
             android:id="@+id/title_update_time"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             android:layout_marginRight="10dp"
             android:layout_alignParentRight="true"
             android:layout_centerVertical="true"
             android:textColor="#fff"
             android:textSize="16sp"/>
     </RelativeLayout>
     ```

**4. 创建当前天气信息布局 (`now.xml`)**
   - 新建一个 `now.xml` 文件，用于显示当前天气的信息。
       - `now_temp`：显示当前温度。
       - `now_weather_info`：显示当前天气状况。

     ```xml
     <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="15dp">
        <TextView
            android:id="@+id/degree_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="end"
            android:textColor="#fff"
            android:textSize="60sp" />
        <TextView
            android:id="@+id/weather_info_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="end"
            android:textColor="#fff"
            android:textSize="20sp" />
    </LinearLayout>
     ```

**5. 新建 `forecast.xml` 作为未来几天天气信息的布局**

- **功能**：最外层使用 `LinearLayout` 定义了半透明的背景，然后使用 `TextView` 定义了标题，接着使用另一个 `LinearLayout` 定义了一个未来天气信息的容器，用于动态显示天气信息

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="15dp"
    android:background="#8000">
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="15dp"
        android:layout_marginTop="15dp"
        android:text="预报"
        android:textColor="#fff"
        android:textSize="20sp"/>
    <LinearLayout
        android:id="@+id/forecast_layout"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </LinearLayout>
</LinearLayout>
```


**6. 新建 `forecast_item.xml` 作为未来天气信息的子项布局**

- **功能**：定义了显示未来天气的子项，包括日期、天气概况、最高温度和最低温度。

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="15dp">
    <TextView
        android:id="@+id/date_text"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:layout_weight="2"
        android:textColor="#fff"/>
    <TextView
        android:id="@+id/info_text"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:layout_weight="1"
        android:gravity="center"
        android:textColor="#fff"/>
    <TextView
        android:id="@+id/max_text"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_weight="1"
        android:gravity="right"
        android:textColor="#fff"/>
    <TextView
        android:id="@+id/min_text"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_weight="1"
        android:gravity="right"
        android:textColor="#fff"/>
</LinearLayout>
```

**7. 新建 `aqi.xml` 作为空气质量信息的布局**

- **功能**：显示空气质量信息，包含 AQI 指数和 PM2.5 指数，使用 `LinearLayout` 和 `RelativeLayout` 嵌套布局来实现左右平分。

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="15dp"
    android:background="#8000">
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="15dp"
        android:layout_marginTop="15dp"
        android:text="空气质量"
        android:textColor="#fff"
        android:textSize="20sp"/>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="15dp">
        <RelativeLayout
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1">
            <LinearLayout
                android:orientation="vertical"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_centerInParent="true">
                <TextView
                    android:id="@+id/aqi_text"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center"
                    android:textColor="#fff"
                    android:textSize="40sp"/>
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center"
                    android:text="AQI指数"
                    android:textColor="#fff"/>
            </LinearLayout>
        </RelativeLayout>
        <RelativeLayout
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1">
            <LinearLayout
                android:orientation="vertical"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_centerInParent="true">
                <TextView
                    android:id="@+id/pm25_text"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center"
                    android:textColor="#fff"
                    android:textSize="40sp"/>
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center"
                    android:text="PM2.5指数"
                    android:textColor="#fff"/>
            </LinearLayout>
        </RelativeLayout>
    </LinearLayout>
</LinearLayout>
```


**8. 新建 `suggestion.xml` 作为生活建议信息的布局**

- **功能**：定义了显示生活建议的布局，包括舒适度、洗车建议和运动建议的相关信息。

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="15dp"
    android:background="#8000">
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="15dp"
        android:layout_marginTop="15dp"
        android:text="生活建议"
        android:textColor="#fff"
        android:textSize="20sp"/>
    <TextView
        android:id="@+id/comfort_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="15dp"
        android:textColor="#fff" />
    <TextView
        android:id="@+id/car_wash_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="15dp"
        android:textColor="#fff" />
    <TextView
        android:id="@+id/sport_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="15dp"
        android:textColor="#fff" />
</LinearLayout>
```

**9. 将布局引入 `activity_weather.xml` 中**

- **功能**：使用 `FrameLayout` 和 `ScrollView` 布局，允许滚动查看所有天气信息。通过 `include` 引入各个模块的布局文件（如标题、当前天气、未来天气、空气质量和生活建议）
- 由于`ScrollView`的内部只允许存在一个直接子布局，因此这里又嵌套了一个垂直方向的`LinearLayout`，然后在`LinearLayout`中将刚才定义的所有布局逐个引入

```xml
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorPrimary">
    <ScrollView
        android:id="@+id/weather_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="none"
        android:overScrollMode="never">
        <LinearLayout
            android:orientation="vertical"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
            <include layout="@layout/title" />
            <include layout="@layout/now" />
            <include layout="@layout/forecast" />
            <include layout="@layout/aqi" />
            <include layout="@layout/suggestion" />
        </LinearLayout>
    </ScrollView>
</FrameLayout>
```

### 将天气显示到界面上

**1. 在 `Utility` 类中添加解析天气数据的方法**

首先，需要在 `Utility` 类中添加一个解析天气 JSON 数据的方法，用于将天气的 JSON 数据转换为对应的实体类 `Weather`：

**说明：**
- `handleWeatherResponse()`方法先通过 `JSONObject` 和 `JSONArray` 将天气数据中的主体内容提取出来。
- 然后通过 `Gson` 的 `fromJson()` 方法将 JSON 数据转换成 `Weather` 对象。

```java
public class Utility {
    ...
    /**
      * 将返回的JSON数据解析成Weather实体类
      */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

```json
{
    "status": "ok",
    "basic": {},
    "aqi": {},
    "now": {},
    "suggestion": {},
    "daily_forecast": []
}
```

**2. 在 `WeatherActivity` 中请求天气数据并显示**

**`WeatherActivity` 初始化控件并获取缓存数据**

1. **初始化控件**：在 `onCreate()` 方法中，先获取各个控件的实例，如 `TextView`、`LinearLayout` 等，用于显示天气信息。

2. **读取缓存数据**：通过 `SharedPreferences` 判断是否有缓存的天气数据。如果有缓存，直接解析并显示数据。如果没有缓存，则需要从服务器请求天气数据。

3. **请求天气数据**：若没有缓存数据，获取通过 `Intent` 传入的天气ID，并调用 `requestWeather()` 方法。这个方法通过 `HttpUtil.sendOkHttpRequest()` 向指定的 URL 发起请求，获取天气信息。

4. **处理响应数据**：在 `onResponse()` 回调中，先将返回的 JSON 数据解析为 `Weather` 对象。然后判断数据是否有效（检查 `status` 是否为 "ok"），若有效，则将数据存入 `SharedPreferences`，并调用 `showWeatherInfo()` 方法显示数据。

5. **显示天气信息**：`showWeatherInfo()` 方法从 `Weather` 对象中提取天气数据并填充到相应的控件上。特别是未来几天的天气预报部分，通过 `for` 循环动态加载每一天的天气信息并显示。

6. **界面更新**：最后，将 `ScrollView` 设置为可见，以显示加载后的天气数据。

7. **缓存使用**：当用户下次进入 `WeatherActivity` 时，由于已经缓存了天气数据，直接从缓存中读取并显示数据，而无需再次发起网络请求。

```java
public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        
        // 初始化控件
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        
        if (weatherString != null) {
            // 有缓存时直接解析并显示天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            // 无缓存时请求天气数据
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    /**
    * 根据天气id请求城市天气信息
    */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
      * 处理并展示Weather实体类中的数据
      */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);
    }
}
```

**3. 从省市县列表跳转到天气界面**

修改 `ChooseAreaFragment` 中的代码，处理从省市县列表界面跳转到天气界面的逻辑：

- 在 `onItemClick()` 中，判断当前的级别，如果是县级，启动 `WeatherActivity` 并传递选中的 `weather_id`。

```java
public class ChooseAreaFragment extends Fragment {
    ...
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        ...
    }
    ...
}
```



**4. 在 `MainActivity` 中判断缓存数据并跳转**

最后，在 `MainActivity` 中添加缓存数据的判断逻辑：

- 在 `onCreate()` 方法中，检查缓存是否有天气数据，如果有，直接跳转到 `WeatherActivity`。

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather", null) != null) {
            // 如果已有缓存数据，直接跳转到WeatherActivity
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
```

**5. 运行应用并选择城市**

1. 启动应用，选择省份、城市、县（选择江苏→苏州→昆山）
2. 应用会请求天气数据，并在 `WeatherActivity` 中展示天气信息
3. 下一次启动时，会直接读取缓存，展示天气数据，而不会再次发起网络请求

### 获取必应每日一图

**1. 背景图片的需求和思路**：
   - 为了让天气界面更美观，避免单调的背景色，可以根据城市或天气情况显示不同的背景图片
   - 使用必应的每日一图作为背景，必应提供一个接口 (`https://s.cn.bing.net/th?id=OHR.KelpForest_ZH-CN2357269491_1920x1080.webp`) 返回今天的背景图链接。通过这个方法可以让界面每天更新不同的精美背景图

**2. 背景图的加载和显示**：
   - 在 `activity_weather.xml` 中修改布局，使用 `FrameLayout` 包含一个 `ImageView` 和一个 `ScrollView`：
   - `FrameLayout` 用来将 `ImageView` 设置为背景，`ScrollView` 通过覆盖 `ImageView` 来实现内容滚动
     ```xml
     <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:background="@color/colorPrimary">
         <ImageView
             android:id="@+id/bing_pic_img"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             android:scaleType="centerCrop" />
         <ScrollView
             android:id="@+id/weather_layout"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             android:scrollbars="none"
             android:overScrollMode="never">
             ...
         </ScrollView>
     </FrameLayout>
     ```

**3. 在 `WeatherActivity` 中实现背景图的加载**：
   - 在 `WeatherActivity` 中，通过 `Glide` 来加载背景图片：
   - `loadBingPic()` 方法请求必应背景图的链接，并通过 `Glide` 加载到 `ImageView` 中
   - 每次请求天气时都会调用 `loadBingPic()` 更新背景图
     ```java
     public class WeatherActivity extends AppCompatActivity {
         private ImageView bingPicImg;

         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_weather);

             // 获取背景图片控件
             bingPicImg = findViewById(R.id.bing_pic_img);

             // 尝试从缓存中读取背景图
             String bingPic = prefs.getString("bing_pic", null);
             if (bingPic != null) {
                 Glide.with(this).load(bingPic).into(bingPicImg);
             } else {
                 loadBingPic();
             }
         }

         private void loadBingPic() {
             String requestBingPic = "http://guolin.tech/api/bing_pic";
             HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
                 @Override
                 public void onResponse(Call call, Response response) throws IOException {
                     final String bingPic = response.body().string();
                     // 缓存背景图
                     SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                     editor.putString("bing_pic", bingPic);
                     editor.apply();

                     // 加载背景图
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                         }
                     });
                 }

                 @Override
                 public void onFailure(Call call, IOException e) {
                     e.printStackTrace();
                 }
             });
         }

         // 其它方法如请求天气信息...
     }
     ```
   

**4. 状态栏和背景图融合**：
   - 为了使背景图与状态栏融为一体，首先需要判断设备系统版本是否为 Android 5.0 及以上：
   - 这段代码使得活动布局扩展到状态栏，并设置状态栏为透明色，从而让背景图能够显示在状态栏上
     ```java
    public class WeatherActivity extends AppCompatActivity {
        ...
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (Build.VERSION.SDK INT >= 21) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                    View.SYSTEM UI FLAG LAYOUT FULLSCREEN
                    | View.SYSTEM UI FLAG LAYOUT STABLE);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
            setContentView(R.layout.activity_weather);
            ...
        }
        ...
    }
     ```


**5. 解决背景图与状态栏之间的布局问题**：
   - 如果背景图与状态栏紧贴，可以通过 `android:fitsSystemWindows` 属性为 `ScrollView` 内的布局添加适配系统窗口的功能：
   - 设置成`true`就表示会为系统状态栏留出空间
     ```xml
     <ScrollView
         android:id="@+id/weather_layout"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:scrollbars="none"
         android:overScrollMode="never">
         <LinearLayout
             android:orientation="vertical"
             android:layout_width="match_parent"
             android:layout_height="wrap_content"
             android:fitsSystemWindows="true">
             ...
         </LinearLayout>
     </ScrollView>
     ```

**6. 最终效果**：
   - 完成上述步骤后，天气界面将展示必应每日一图作为背景，且背景图与状态栏融合，视觉效果更佳
   - 每次进入应用时，背景图会根据必应的每日更新自动更换，提升界面的动态感

## 手动更新天气和切换城市

当前应用的一个严重 bug 是：当用户选择某个城市查看天气后，无法再查看其他城市的天气。即使退出程序，下次进入时也会直接跳转到之前查看的城市的 `WeatherActivity`

- **切换城市功能**：允许用户选择并查看不同城市的天气。
- **手动更新天气功能**：为了确保显示的是最新的天气信息，用户可以手动刷新天气数据。

### 手动更新天气

**1. 目标**：允许用户手动刷新天气信息，解决当前只显示缓存数据的问题。实现方式是采用下拉刷新的功能。

**2. 修改 `activity_weather.xml`**：
   - 在 `ScrollView` 外层嵌套 `SwipeRefreshLayout`，让 `ScrollView` 自动支持下拉刷新功能。
   
   ```xml
   <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@color/colorPrimary">
       ...
       <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
           android:id="@+id/swipe_refresh"
           android:layout_width="match_parent"
           android:layout_height="match_parent">
           <ScrollView
               android:id="@+id/weather_layout"
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               android:scrollbars="none"
               android:overScrollMode="never">
               ...
           </ScrollView>
       </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
   </FrameLayout>
   ```

**3. 修改 `WeatherActivity` 中的代码**：
   - **初始化 `SwipeRefreshLayout`**：
     在 `onCreate()` 方法中，初始化 `SwipeRefreshLayout` 并设置颜色方案。
   
   - **缓存数据**：
     判断是否有缓存的天气数据，如果有就直接解析并显示，如果没有则向服务器请求天气数据。
   
   - **设置下拉刷新监听**：
     通过 `setOnRefreshListener()` 设置下拉刷新的监听器，触发时调用 `requestWeather()` 方法重新获取天气数据。
   
   - **更新天气信息**：
     在请求天气数据时，调用 `HttpUtil.sendOkHttpRequest()` 请求服务器天气数据并更新 UI。数据请求成功后，缓存新天气数据，并更新界面。
   
   - **结束刷新动画**：
     无论请求成功与否，在 `onResponse()` 和 `onFailure()` 方法中都要调用 `swipeRefresh.setRefreshing(false)` 来停止下拉刷新动画。

   ```java
   public class WeatherActivity extends AppCompatActivity {
       public SwipeRefreshLayout swipeRefresh;
       private String mWeatherId;
       ...
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           ...
           swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
           swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
           
           SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
           String weatherString = prefs.getString("weather", null);

           if (weatherString != null) {
               // 有缓存时直接解析天气数据
               Weather weather = Utility.handleWeatherResponse(weatherString);
               mWeatherId = weather.basic.weatherId;
               showWeatherInfo(weather);
           } else {
               // 无缓存时去服务器查询天气
               mWeatherId = getIntent().getStringExtra("weather_id");
               weatherLayout.setVisibility(View.INVISIBLE);
               requestWeather(mWeatherId);
           }
           
           swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
               @Override
               public void onRefresh() {
                   requestWeather(mWeatherId);
               }
           });
           ...
       }

       public void requestWeather(final String weatherId) {
           String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
           HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
               @Override
               public void onResponse(Call call, Response response) throws IOException {
                   ...
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           if (weather != null && "ok".equals(weather.status)) {
                               SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                               editor.putString("weather", responseText);
                               editor.apply();
                               mWeatherId = weather.basic.weatherId;
                               showWeatherInfo(weather);
                           } else {
                               Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                           }
                           swipeRefresh.setRefreshing(false);  // 结束刷新动画
                       }
                   });
               }
               
               @Override
               public void onFailure(Call call, IOException e) {
                   e.printStackTrace();
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                           swipeRefresh.setRefreshing(false);  // 结束刷新动画
                       }
                   });
               }
           });
           loadBingPic();  // 刷新背景图片
       }
       ...
   }
   ```

**4. 效果**：
   - 当用户在天气界面下拉时，天气信息将重新从服务器获取并更新，刷新动画会在获取数据后消失。
   - 如果有缓存数据，将直接展示缓存数据；如果没有，则通过网络请求获取并缓存最新的天气数据。

### 切换城市

1. **手动更新天气功能**
   - 在上一步的基础上，实现了手动更新天气功能。通过引入 `SwipeRefreshLayout` 和下拉刷新的方式，用户可以手动刷新天气信息。
   - 在 `activity_weather.xml` 中，嵌套了 `SwipeRefreshLayout` 来实现下拉刷新。
   - 在 `WeatherActivity` 中，通过 `swipeRefresh.setOnRefreshListener()` 方法监听下拉刷新事件，并调用 `requestWeather()` 请求天气数据。

2. **切换城市功能实现**
   - 为了实现切换城市功能，首先需要遍历全国省市县的数据。考虑到后续复用，功能实现是在碎片中完成的。
   - 在天气界面的布局中引入 `ChooseAreaFragment` 碎片，直接集成切换城市的功能。

3. **滑动菜单实现**
   - 为了避免 `ChooseAreaFragment` 碎片遮挡住天气界面，使用了 **滑动菜单**（DrawerLayout）来包含该碎片，正常情况下不会占据主界面的空间。
   - 用户通过从左侧滑动来显示菜单，实现切换城市功能。

4. **修改标题栏布局**
   - 在 `title.xml` 中，加入了一个按钮用于切换城市，确保用户知道左侧可以滑动打开菜单。按钮样式如下：

   ```xml
   <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="match_parent"
       android:layout_height="?attr/actionBarSize">
       <Button
           android:id="@+id/nav_button"
           android:layout_width="30dp"
           android:layout_height="30dp"
           android:layout_marginLeft="10dp"
           android:layout_alignParentLeft="true"
           android:layout_centerVertical="true"
           android:background="@drawable/ic_home" />
   </RelativeLayout>
   ```

5. **修改天气界面布局**
   - 在 `activity_weather.xml` 中，使用 `DrawerLayout` 包含 `SwipeRefreshLayout` 和 `ChooseAreaFragment`，布局如下：

   ```xml
   <FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorPrimary">
       ...
       <androidx.drawerlayout.widget.DrawerLayout
           android:id="@+id/drawer_layout"
           android:layout_width="match_parent"
           android:layout_height="match_parent">
           <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
               android:id="@+id/swipe_refresh"
               android:layout_width="match_parent"
               android:layout_height="match_parent">
               ...
           </android.support.v4.widget.SwipeRefreshLayout>
           <fragment
               android:id="@+id/choose_area_fragment"
               android:name="com.example.demo39.ChooseAreaFragment"
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               android:layout_gravity="start" />
       </androidx.drawerlayout.widget.DrawerLayout
   </FrameLayout>
   ```

6. **WeatherActivity 中实现滑动菜单逻辑**
   - 在 `WeatherActivity` 中，处理滑动菜单的显示和隐藏：

   ```java
   public class WeatherActivity extends AppCompatActivity {
       public DrawerLayout drawerLayout;
       private Button navButton;
       ...
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           ...
           drawerLayout = findViewById(R.id.drawer_layout);
           navButton = findViewById(R.id.nav_button);
           ...
           navButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   drawerLayout.openDrawer(GravityCompat.START);
               }
           });
       }
       ...
   }
   ```

7. **处理切换城市后的逻辑**
   - 在 `ChooseAreaFragment` 中，处理选择城市后切换的逻辑：

   ```java
   public class ChooseAreaFragment extends Fragment {
       ...
           @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   if (currentLevel == LEVEL_PROVINCE) {
                       selectedProvince = provinceList.get(position);
                       queryCities();
                   } else if (currentLevel == LEVEL_CITY) {
                       selectedCity = cityList.get(position);
                       queryCounties();
                   } else if (currentLevel == LEVEL_COUNTY) {
                       String weatherId = countyList.get(position).getWeatherId();
                       if (getActivity() instanceof MainActivity) {
                           Intent intent = new Intent(getActivity(), WeatherActivity.class);
                           intent.putExtra("weather_id", weatherId);
                           startActivity(intent);
                           getActivity().finish();
                       } else if (getActivity() instanceof WeatherActivity) {
                           WeatherActivity activity = (WeatherActivity) getActivity();
                           activity.drawerLayout.closeDrawers();
                           activity.swipeRefresh.setRefreshing(true);
                           activity.requestWeather(weatherId);
                       }
                   }
               }
           });
           ...
       }
       ...
   }
   ```

   - 使用 `instanceof` 判断 `ChooseAreaFragment` 是在 `MainActivity` 还是 `WeatherActivity` 中，从而决定是跳转到新活动还是更新当前活动的天气。


## 后台自动更新天气

加入后台自动更新天气的功能。这样用户每次打开软件时就能看到最新的天气信息。

为了实现这个功能，需要创建一个长期在后台运行的定时任务。

**1. 创建后台服务**

首先，在 `service` 包下新建一个服务。右击 `com.example.demo39.service` → `New` → `Service`，创建一个名为 `AutoUpdateService` 的服务，并勾选 `Exported` 和 `Enabled` 属性。然后，修改 `AutoUpdateService` 中的代码

在 `onStartCommand()` 方法中，首先调用了 `updateWeather()` 方法来更新天气信息，接着调用 `updateBingPic()` 方法来更新背景图片。然后，使用 `AlarmManager` 来设置定时任务，确保每隔 8 小时更新一次天气信息和图片。

```java
public class AutoUpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();  // 更新天气
        updateBingPic();  // 更新必应每日一图

        // 设置定时任务
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000; // 8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);  // 取消之前的定时任务
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);  // 设置定时任务
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 如果有缓存，直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                    weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(AutoUpdateService.this).
                                edit();
                        editor.putString("weather", responseText);  // 存储天气信息
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", bingPic);  // 存储必应每日一图
                editor.apply();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
```

**2. 启动服务**

在天气界面 `WeatherActivity` 中，需要在选中某个城市并成功更新天气后启动 `AutoUpdateService` 服务，以确保后台自动更新。修改 `WeatherActivity` 中的代码

在 `showWeatherInfo()` 方法中，最后加入了启动 `AutoUpdateService` 服务的代码。这样一旦选中某个城市并成功更新天气，`AutoUpdateService` 就会在后台运行，并且每 8 小时更新一次天气信息。

```java
public class WeatherActivity extends AppCompatActivity {
    ...
    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        ...
        weatherLayout.setVisibility(View.VISIBLE);
        // 启动AutoUpdateService服务
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
```

## 修改图标和名称

**1. 准备图标图片**

为了让酷欧天气看起来更像一个正式的软件，需要替换掉 Android Studio 自动生成的图标。准备一张名为 `logo.png` 的图片作为应用图标，理论上应该为不同分辨率提供多个版本，并将它们放入相应的 `mipmap` 目录下。

为了简化操作，可以将同一张图标放入所有以 `mipmap` 开头的目录。

**2. 放入 `mipmap` 目录**

将准备好的 `logo.png` 图片放入 `mipmap` 相关的目录下，通常是：
- `mipmap-mdpi/`
- `mipmap-hdpi/`
- `mipmap-xhdpi/`
- `mipmap-xxhdpi/`
- `mipmap-xxxhdpi/`

**3. 修改 `AndroidManifest.xml`**

接下来，修改 `AndroidManifest.xml` 文件中的 `<application>` 标签，指定图标。将 `android:icon` 属性指向放入 `mipmap` 目录的图标资源。

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.coolweather.android">
    <uses-permission android:name="android.permission.INTERNET" />
    <application
        android:name="org.litepal.LitePalApplication"
        android:allowBackup="true"
        android:icon="@mipmap/logo"  <!-- 设置应用图标 -->
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        ...
    </application>
</manifest>
```

**4. 修改应用名称**

然后，修改 `res/values/strings.xml` 文件，找到 `app_name` 字符串资源，将其修改为 `酷欧天气`，以便显示在桌面图标下方。

```xml
<resources>
    <string name="app_name">酷欧天气</string>  <!-- 修改应用名称 -->
</resources>
```

**5. 重新运行程序**

完成以上修改后，重新运行应用。此时，观察酷欧天气的桌面图标已经更新为新的图标，并且应用名称也显示为 `酷欧天气`。

***

**总结**

经过五个阶段的开发，酷欧天气已经具备了基本的功能，但与商用天气软件相比，仍有很大的提升空间。以下是可以进一步完善酷欧天气的一些功能建议：

1. **增加设置选项**：允许用户选择是否启用后台自动更新天气，并设置更新频率。
2. **优化界面**：提供多种与天气匹配的背景图片，自动根据天气情况切换。
3. **多城市支持**：允许用户选择多个城市并同时查看天气信息，避免频繁切换。
4. **提供完整天气数据**：目前只使用了和风天气提供的部分数据，可以考虑增加更多的天气信息。
   