# 使用网络技术

使用HTTP协议和服务器端进行网络交互，并对服务器返回的数据进行解析

## WebView的用法

在Android开发中，如果需要在应用内展示网页内容而不跳转到系统浏览器，`WebView`控件可以满足此需求。通过在应用中嵌入`WebView`控件，便可以轻松加载和显示网页内容，以下为具体使用方法。

**1. 布局文件配置**

首先，在布局文件`activity_main.xml`中添加`WebView`控件：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >
    <WebView
        android:id="@+id/web_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

此处定义了一个`WebView`控件，占满整个屏幕，并设置了一个ID以便在代码中引用。

**2. 主Activity代码配置**

接下来，在`MainActivity`中初始化`WebView`控件并加载网页内容：

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        WebView webView = findViewById(R.id.web_view);
        
        // 启用JavaScript支持
        webView.getSettings().setJavaScriptEnabled(true);
        
        // 设置WebViewClient
        webView.setWebViewClient(new WebViewClient());
        
        // 加载网址
        webView.loadUrl("http://www.baidu.com");
    }
}
```

1. **启用JavaScript支持**：通过`webView.getSettings().setJavaScriptEnabled(true);`启用`JavaScript`，让网页可以运行JavaScript脚本。
2. **设置`WebViewClient`**：调用`setWebViewClient()`并传入`WebViewClient`实例，避免点击网页链接时跳转到系统浏览器。
3. **加载网页**：使用`loadUrl()`方法加载指定网址，此处加载百度首页。

**3. 添加网络访问权限**

由于`WebView`需要访问网络，需在`AndroidManifest.xml`中声明网络权限：

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.webviewtest">
    <uses-permission android:name="android.permission.INTERNET" />
    ...
</manifest>
```

**4. 运行效果**

确保手机或模拟器联网，即可运行程序并展示百度首页。点击页面内的链接可以浏览更多内容。

**5. 注意事项**

* `WebView`具有加载网页的功能，但并非完整浏览器，适合在应用内展示轻量级网页内容。

## 使用HTTP协议访问网络

**HTTP 协议简介**

HTTP 协议是客户端和服务器之间进行数据交换的基础协议，主要过程如下：

* 客户端向服务器发出 HTTP 请求。
* 服务器接收请求并返回数据。
* 客户端解析并处理返回的数据。

在 Android 开发中，通过 WebView 等组件可以轻松展示网页，但这种方式封装度较高，开发者无法直接观察 HTTP 协议的工作细节。

### 使用 HttpURLConnection

> demo31

Android 官方推荐使用 `HttpURLConnection` 进行 HTTP 请求。以下是其基本使用步骤：

1. **创建 `HttpURLConnection` 实例**  
   通过 `URL` 对象的 `openConnection()` 方法创建连接实例：
   ```java
   URL url = new URL("http://www.baidu.com");
   HttpURLConnection connection = (HttpURLConnection) url.openConnection();
   ```

2. **设置请求方法**  
   常用方法有 `GET` 和 `POST`：
   ```java
   connection.setRequestMethod("GET");
   ```

3. **设置连接和读取超时**  
   可以根据需求设置连接和读取的超时时间（单位为毫秒）：
   ```java
   connection.setConnectTimeout(8000);
   connection.setReadTimeout(8000);
   ```

4. **获取服务器响应的输入流**  
   通过 `getInputStream()` 获取服务器返回的数据流：
   ```java
   InputStream in = connection.getInputStream();
   ```

5. **读取输入流**  
   使用 `BufferedReader` 对返回的数据流进行读取：
   ```java
   BufferedReader reader = new BufferedReader(new InputStreamReader(in));
   StringBuilder response = new StringBuilder();
   String line;
   while ((line = reader.readLine()) != null) {
       response.append(line);
   }
   ```

6. **关闭连接**  
   请求完成后，调用 `disconnect()` 关闭连接：
   ```java
   connection.disconnect();
   ```

**1. 实现示例：发送 HTTP 请求并显示响应**

**布局文件 (`activity_main.xml`)：**
包含一个 `Button` 和一个 `TextView` 控件用于发送请求并显示响应。

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <Button
        android:id="@+id/send_request"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Send Request" />
    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <TextView
            android:id="@+id/response_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
    </ScrollView>
</LinearLayout>
```

**`MainActivity` 代码：**

1. **Button 点击事件**  
   点击按钮后，调用 `sendRequestWithHttpURLConnection()` 方法发起请求。
   
2. **子线程中执行请求**  
   网络请求应在子线程中执行，避免阻塞主线程。

3. **显示服务器响应**  
   使用 `runOnUiThread()` 在主线程中更新 UI。

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button sendRequest = findViewById(R.id.send_request);
        responseText = findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
            sendRequestWithHttpURLConnection();
        }
    }

    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
}
```

**注意事项：**
- 网络请求需要声明网络权限：
  ```xml
  <uses-permission android:name="android.permission.INTERNET" />
  ```

**2. 使用 `POST` 方法提交数据**

若需提交数据，可将请求方法设置为 `POST`，并在获取输入流之前通过 `DataOutputStream` 将数据写出：
```java
connection.setRequestMethod("POST");
DataOutputStream out = new DataOutputStream(connection.getOutputStream());
out.writeBytes("username=admin&password=123456");
```

### 使用OkHttp

> demo32

在 Android 开发中，除了 `HttpURLConnection` 外，还可以选择功能强大的开源网络库，比如 `OkHttp`。

`OkHttp` 是由 Square 公司开发的，Square 还创建了其他知名项目如 `Picasso` 和 `Retrofit`。与原生的 `HttpURLConnection` 相比，`OkHttp` 不仅在接口封装上更为简洁易用，在底层实现上也有独到之处。

**1. 添加 OkHttp 依赖**

首先，在项目的 `app/build.gradle` 文件中添加 OkHttp 的依赖：

```gradle
dependencies {
    implementation 'com.squareup.okhttp3:okhttp:4.9.3'
}
```

**2. 发送 GET 请求**

1. **创建 OkHttpClient 实例**：用于管理请求队列和网络连接。
    ```java
    OkHttpClient client = new OkHttpClient();
    ```
   
2. **构建 Request 对象**：指定目标 URL。
    ```java
    Request request = new Request.Builder()
            .url("http://www.baidu.com")
            .build();
    ```

3. **发送请求并接收响应**：
    ```java
    Response response = client.newCall(request).execute();
    String responseData = response.body().string();
    ```

**2. 发送 POST 请求**

1. **构建 RequestBody 对象**：用于存放 POST 请求参数。
    ```java
    RequestBody requestBody = new FormBody.Builder()
            .add("username", "admin")
            .add("password", "123456")
            .build();
    ```

2. **构建 Request 对象，设置请求类型为 POST**：
    ```java
    Request request = new Request.Builder()
            .url("http://www.baidu.com")
            .post(requestBody)
            .build();
    ```

3. **发送请求并接收响应**：
    和 GET 请求一致，使用 `execute()` 方法发送请求。

**3. 使用 OkHttp 实现示例**

将之前的 `HttpURLConnection` 示例改用 `OkHttp` 实现。只需在 `MainActivity` 中调整网络请求部分代码。

1. 修改 `MainActivity` 的代码，增加 `sendRequestWithOkHttp()` 方法：

    ```java
    public class MainActivity extends AppCompatActivity implements View.OnClickListener {
        TextView responseText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button sendRequest = findViewById(R.id.send_request);
            responseText = findViewById(R.id.response_text);
            sendRequest.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.send_request) {
                sendRequestWithOkHttp();
            }
        }

        private void sendRequestWithOkHttp() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://www.baidu.com")
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        showResponse(responseData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        private void showResponse(final String response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    responseText.setText(response);
                }
            });
        }
    }
    ```

2. **运行程序**：点击“Send Request”按钮，即可看到与 `HttpURLConnection` 相同的效果。 `OkHttp` 的网络请求功能已成功实现。


## 解析XML格式数据

在 Android 应用开发中，服务器通常会提供格式化的数据来进行网络通信，以便客户端解析并提取需要的内容。常见的数据格式包括 XML 和 JSON。

**1. 数据传输的背景**

数据在网络上传输时，不能以任意格式传递，必须遵循统一的格式。格式化的数据有特定的结构，使得接收端可以按照相同的结构进行解析，从而提取出有用的信息。XML 和 JSON 是最常用的数据传输格式。

**2. 准备 XML 数据源**

在解析 XML 数据之前，需要有一个可供访问的 XML 数据源。这里通过搭建一个 Apache 本地服务器来提供 XML 数据。

**3. 搭建 Apache 本地服务器**

1. **下载 Apache 服务器**：从 [Apache 官方网站](http://httpd.apache.org/download.cgi) 下载 Windows 版的安装包（若在官网下载不到，可以百度搜索“Apache服务器下载”）
2. **测试服务器**：在浏览器地址栏中输入 `127.0.0.1`，若看到欢迎页面，说明服务器已启动成功
    - `bin> ./httpd.exe`

**4. 创建 XML 数据文件**

1. 进入 `\htdocs` 目录，在此目录下新建一个名为 `get_data.xml` 的文件。
2. 编辑 `get_data.xml` 文件，添加如下 XML 数据内容：

   ```xml
   <apps>
       <app>
           <id>1</id>
           <name>Google Maps</name>
           <version>1.0</version>
       </app>
       <app>
           <id>2</id>
           <name>Chrome</name>
           <version>2.1</version>
       </app>
       <app>
           <id>3</id>
           <name>Google Play</name>
           <version>2.3</version>
       </app>
   </apps>
   ```

3. 在浏览器中访问 `http://127.0.0.1/get_data.xml`，此时应能看到上述 XML 数据内容。

**5. Android 端解析 XML 数据**

准备好服务器和 XML 数据文件后，便可以在 Android 程序中访问该数据源，并解析 XML 格式的数据内容。

### Pull解析方式

解析 XML 格式的数据有多种方式，其中比较常用的方式包括 Pull 解析和 SAX 解析

**1. 修改后的 `MainActivity` 代码**

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ...
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                          // 指定访问的服务器地址是电脑本机
                          .url("http://192.168.3.220/get_data.xml")
                          .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseXMLWithPull(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    ...
    private void parseXMLWithPull(String xmlData) {
        try {
            // 创建XmlPullParserFactory实例
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData)); // 设置XML数据

            int eventType = xmlPullParser.getEventType(); // 获取事件类型
            String id = "", name = "", version = "";

            // 解析XML数据
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName(); // 获取当前节点名称
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        // 解析节点的内容
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        // 完成解析某个节点时
                        if ("app".equals(nodeName)) {
                            Log.d("MainActivity", "id is " + id);
                            Log.d("MainActivity", "name is " + name);
                            Log.d("MainActivity", "version is " + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next(); // 获取下一个事件
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

**2. 解析过程**

1. **发送请求：**
   - 修改了 HTTP 请求的地址为 `http://192.168.3.220/get_data.xml`，在模拟器中，`192.168.3.220` 表示主机地址
   - 通过 OkHttp 获取服务器返回的 XML 数据

2. **解析 XML 数据：**
   - 创建了 `XmlPullParserFactory` 实例，通过它获取 `XmlPullParser` 对象，并设置输入为从服务器获取的 XML 数据
   - 使用 `getEventType()` 方法获取当前解析事件，并通过 `while` 循环不断进行解析，直到遇到 `END_DOCUMENT` 事件，表示解析完成
   - 使用 `getName()` 方法获取当前节点的名称，根据节点名称（如 `id`, `name`, `version`）调用 `nextText()` 方法获取节点的内容，并将解析到的值存储。

3. **打印解析结果：**
   - 每次解析完一个 `app` 节点后，将解析出的 `id`, `name`, `version` 打印出来。

**3. 测试**

- 在运行 **NetworkTest** 项目并点击 **Send Request** 按钮后，可以在 **Logcat** 中查看打印的日志信息，验证 XML 数据是否成功解析。

```
2024-11-06 12:24:38.931 6218-6290/com.example.demo32 D/MainActivity: XML Data: <apps>
        <app>
            <id>1</id>
            <name>Google Maps</name>
            <version>1.0</version>
        </app>
        <app>
            <id>2</id>
            <name>Chrome</name>
            <version>2.1</version>
        </app>
        <app>
            <id>3</id>
            <name>Google Play</name>
            <version>2.3</version>
        </app>
    </apps>
2024-11-06 12:24:38.933 6218-6290/com.example.demo32 D/MainActivity: id is 1
2024-11-06 12:24:38.933 6218-6290/com.example.demo32 D/MainActivity: name is Google Maps
2024-11-06 12:24:38.933 6218-6290/com.example.demo32 D/MainActivity: version is 1.0
2024-11-06 12:24:38.934 6218-6290/com.example.demo32 D/MainActivity: id is 2
2024-11-06 12:24:38.934 6218-6290/com.example.demo32 D/MainActivity: name is Chrome
2024-11-06 12:24:38.934 6218-6290/com.example.demo32 D/MainActivity: version is 2.1
2024-11-06 12:24:38.935 6218-6290/com.example.demo32 D/MainActivity: id is 3
2024-11-06 12:24:38.935 6218-6290/com.example.demo32 D/MainActivity: name is Google Play
2024-11-06 12:24:38.935 6218-6290/com.example.demo32 D/MainActivity: version is 2.3

```

### SAX解析方式

SAX（Simple API for XML）解析是另一种常用的XML解析方式，通过事件驱动的方式解析XML数据。尽管用法比Pull解析稍微复杂，但语义更加清晰。

**1. SAX解析基本步骤**

SAX解析工作原理是通过一系列事件方法来处理XML文档，在解析过程中，当XML的不同节点被解析时，相关的事件方法会被触发。要使用SAX解析XML，通常需要自定义一个类继承自 `DefaultHandler`，并重写以下五个方法：

1. **startDocument()**：在开始解析XML文档时调用。
2. **startElement(String uri, String localName, String qName, Attributes attributes)**：在解析某个元素（节点）时调用。
3. **characters(char[] ch, int start, int length)**：在获取节点内容时调用。
4. **endElement(String uri, String localName, String qName)**：在完成解析某个元素时调用。
5. **endDocument()**：在完成整个XML解析时调用。

这些方法的执行顺序反映了XML文档的解析过程。通常，需要在这些方法中进行数据的收集和处理。

**2. 实现SAX解析**

以下是一个使用SAX解析XML的示例：

1. **自定义ContentHandler类**：
   
   ```java
   public class ContentHandler extends DefaultHandler {
       private String nodeName;
       private StringBuilder id;
       private StringBuilder name;
       private StringBuilder version;
   
       @Override
       public void startDocument() throws SAXException {
           id = new StringBuilder();
           name = new StringBuilder();
           version = new StringBuilder();
       }
   
       @Override
       public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
           nodeName = localName;
       }
   
       @Override
       public void characters(char[] ch, int start, int length) throws SAXException {
           if ("id".equals(nodeName)) {
               id.append(ch, start, length);
           } else if ("name".equals(nodeName)) {
               name.append(ch, start, length);
           } else if ("version".equals(nodeName)) {
               version.append(ch, start, length);
           }
       }
   
       @Override
       public void endElement(String uri, String localName, String qName) throws SAXException {
           if ("app".equals(localName)) {
               Log.d("ContentHandler", "id is " + id.toString().trim());
               Log.d("ContentHandler", "name is " + name.toString().trim());
               Log.d("ContentHandler", "version is " + version.toString().trim());
               id.setLength(0);
               name.setLength(0);
               version.setLength(0);
           }
       }
   
       @Override
       public void endDocument() throws SAXException {
           super.endDocument();
       }
   }
   ```

2. **在Activity中调用SAX解析**：
   
   ```java
   public class MainActivity extends AppCompatActivity implements View.OnClickListener {
       ...
       private void sendRequestWithOkHttp() {
           new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                       OkHttpClient client = new OkHttpClient();
                       Request request = new Request.Builder()
                               .url("http://192.168.3.220/get_data.xml")  // 服务器地址
                               .build();
                       Response response = client.newCall(request).execute();
                       String responseData = response.body().string();
                       parseXMLWithSAX(responseData);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           }).start();
       }
   
       private void parseXMLWithSAX(String xmlData) {
           try {
               SAXParserFactory factory = SAXParserFactory.newInstance();
               XMLReader xmlReader = factory.newSAXParser().getXMLReader();
               ContentHandler handler = new ContentHandler();
               xmlReader.setContentHandler(handler);
               xmlReader.parse(new InputSource(new StringReader(xmlData)));
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
   ```

```
2024-11-06 12:32:22.642 6533-6596/com.example.demo32 D/ContentHandler: id is 1
2024-11-06 12:32:22.642 6533-6596/com.example.demo32 D/ContentHandler: name is Google Maps
2024-11-06 12:32:22.642 6533-6596/com.example.demo32 D/ContentHandler: version is 1.0
2024-11-06 12:32:22.643 6533-6596/com.example.demo32 D/ContentHandler: id is 2
2024-11-06 12:32:22.643 6533-6596/com.example.demo32 D/ContentHandler: name is Chrome
2024-11-06 12:32:22.643 6533-6596/com.example.demo32 D/ContentHandler: version is 2.1
2024-11-06 12:32:22.643 6533-6596/com.example.demo32 D/ContentHandler: id is 3
2024-11-06 12:32:22.643 6533-6596/com.example.demo32 D/ContentHandler: name is Google Play
2024-11-06 12:32:22.643 6533-6596/com.example.demo32 D/ContentHandler: version is 2.3
```

**3. 工作流程**

1. 在 `startDocument()` 中初始化需要用来保存数据的 `StringBuilder` 对象。
2. 在 `startElement()` 中记录当前节点名称。
3. 在 `characters()` 中根据节点名称将数据追加到相应的 `StringBuilder` 中。
4. 在 `endElement()` 中处理节点结束事件，打印数据并清空 `StringBuilder` 对象，以便处理下一个节点。
5. 在 `endDocument()` 中完成整个文档的解析。

**4. 注意事项**

- `characters()` 方法可能会被多次调用，尤其是在遇到换行符或空格时。需要在处理数据时注意去除这些无效字符。
- 解析完成后，要清空 `StringBuilder`，否则会影响下一次解析。

**5. 总结**

SAX解析通过事件驱动的方式高效地处理大规模的XML数据，相比于Pull解析，它更加节省内存，并且解析过程中的语义更加清晰，适用于大数据量的XML解析。

### DOM解析方式

DOM（Document Object Model）解析 也是一种常见的 XML 解析方式，由于 DOM 解析方式处理的是整个文档，因此它非常适合小型文件或需要频繁修改 XML 内容的场景。

## 解析JSON格式数据

JSON格式数据在现代应用中非常常见，尤其在网络请求和数据传输中，因其体积小且易于处理，广泛用于替代XML格式。尽管JSON的语义性较弱，看起来不如XML直观，但它通常更适合轻量级的数据交换。

**1. 准备JSON数据**

在开始解析之前，需要创建一个包含JSON数据的文件。在`htdocs`目录下新建一个名为 `get_data.json` 的文件，加入以下内容：

```json
[
    {"id":"5", "version":"5.5", "name":"Clash of Clans"},
    {"id":"6", "version":"7.0", "name":"Boom Beach"},
    {"id":"7", "version":"3.5", "name":"Clash Royale"}
]
```

然后，在浏览器中访问 `http://127.0.0.1/get_data.json`，即可看到类似的数据内容。

**2. 解析JSON数据的方法**

解析JSON数据有多种方式。可以使用Android官方提供的 `JSONObject`，也可以使用开源库如GSON、Jackson或FastJSON等。

### 使用 JSONObject

首先，修改 `MainActivity` 中的代码，通过 OkHttp 请求获取数据并解析。

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ...
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址
                            .url("http://192.168.3.220/get_data.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData); // 将JSON字符串转换成JSONArray
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i); // 获取每个JSON对象
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");

                // 打印解析出的数据
                Log.d("MainActivity", "id is " + id);
                Log.d("MainActivity", "name is " + name);
                Log.d("MainActivity", "version is " + version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

**1. 解析过程说明**

1. **发送请求：** 通过 OkHttp 库发送 HTTP 请求来获取服务器数据。请求地址为 `http://192.168.3.220/get_data.json`。
   
2. **解析JSON数据：** 
   - 接收到的JSON数据是一个数组，因此将其转换为 `JSONArray`。
   - 然后通过循环遍历 `JSONArray`，每个元素都是一个 `JSONObject`。
   - 使用 `getString()` 方法提取其中的 `id`、`name` 和 `version` 数据，并通过 `Log.d()` 打印出来。

**2. 重新运行程序**

重新运行程序，点击 `SendRequest` 按钮，应该能够看到日志中打印出的每个游戏的 `id`、`name` 和 `version` 数据。

```
2024-11-06 13:00:43.823 7047-7106/com.example.demo32 D/MainActivity: id is 5
2024-11-06 13:00:43.823 7047-7106/com.example.demo32 D/MainActivity: name is Clash of Clans
2024-11-06 13:00:43.823 7047-7106/com.example.demo32 D/MainActivity: version is 5.5
2024-11-06 13:00:43.823 7047-7106/com.example.demo32 D/MainActivity: id is 6
2024-11-06 13:00:43.823 7047-7106/com.example.demo32 D/MainActivity: name is Boom Beach
2024-11-06 13:00:43.823 7047-7106/com.example.demo32 D/MainActivity: version is 7.0
2024-11-06 13:00:43.823 7047-7106/com.example.demo32 D/MainActivity: id is 7
2024-11-06 13:00:43.823 7047-7106/com.example.demo32 D/MainActivity: name is Clash Royale
2024-11-06 13:00:43.823 7047-7106/com.example.demo32 D/MainActivity: version is 3.5
```

### 使用GSON

GSON是谷歌提供的一个开源库，可以使得JSON数据的解析变得更加简便，几乎不需要手动写解析代码。GSON并未被Android官方API包含，因此需要手动添加依赖。

**1. 添加GSON库依赖**

首先，打开项目的`app/build.gradle`文件，并在`dependencies`闭包中添加如下内容：

```gradle
dependencies {
    implementation 'com.google.code.gson:gson:2.8.8'
}
```

添加依赖后，点击“Sync Now”按钮，确保GSON库被成功导入到项目中。

**2. GSON解析基本概念**

GSON的神奇之处在于，它能够将JSON格式的字符串自动映射为Java对象。比如，对于以下JSON数据：

```json
{"name":"Tom", "age":20}
```

可以定义一个`Person`类，然后直接将JSON数据解析为`Person`对象：

```java
Gson gson = new Gson();
Person person = gson.fromJson(jsonData, Person.class);
```

对于JSON数组的解析，稍微复杂一些。需要借助`TypeToken`来指定目标类型：

```java
List<Person> people = gson.fromJson(jsonData, new TypeToken<List<Person>>(){}.getType());
```

**3. 实际应用：解析JSON数据到Java对象**

接下来，创建一个`App`类，包含`id`、`name`和`version`字段，并实现相应的getter和setter方法。

```java
public class App {
    private String id;
    private String name;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
```

然后，在`MainActivity`中修改代码来发送HTTP请求并解析JSON数据：

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ...
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址
                            .url("http://192.168.3.220/get_data.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData); // 调用解析方法
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        // 使用TypeToken解析JSON数组
        List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>(){}.getType());
        for (App app : appList) {
            Log.d("MainActivity", "id is " + app.getId());
            Log.d("MainActivity", "name is " + app.getName());
            Log.d("MainActivity", "version is " + app.getVersion());
        }
    }
}
```

**4. 重新运行程序**

现在，重新运行程序并点击`Send Request`按钮。在logcat中查看打印日志，应该可以看到每个`App`对象的`id`、`name`和`version`信息。

```
2024-11-06 13:07:06.231 7286-7350/com.example.demo32 D/MainActivity: id is 5
2024-11-06 13:07:06.231 7286-7350/com.example.demo32 D/MainActivity: name is Clash of Clans
2024-11-06 13:07:06.231 7286-7350/com.example.demo32 D/MainActivity: version is 5.5
2024-11-06 13:07:06.231 7286-7350/com.example.demo32 D/MainActivity: id is 6
2024-11-06 13:07:06.231 7286-7350/com.example.demo32 D/MainActivity: name is Boom Beach
2024-11-06 13:07:06.231 7286-7350/com.example.demo32 D/MainActivity: version is 7.0
2024-11-06 13:07:06.231 7286-7350/com.example.demo32 D/MainActivity: id is 7
2024-11-06 13:07:06.231 7286-7350/com.example.demo32 D/MainActivity: name is Clash Royale
2024-11-06 13:07:06.231 7286-7350/com.example.demo32 D/MainActivity: version is 3.5
```

**5. 总结**

通过GSON，可以更轻松地将JSON数据映射为Java对象，减少了手动解析的复杂度。GSON不仅支持普通对象的解析，也可以处理复杂的集合类数据（如List、Map等），非常适合进行高效的数据处理。


## 网络编程的最佳实践

在进行网络编程时，重复编写相同的HTTP请求代码显然不够高效，因此应该将常用的网络操作提取到一个公共类中，提供一个静态方法来简化代码。

通过回调机制和合适的封装方法，可以更高效、简洁地进行网络请求操作。

**1. 创建公共的 `HttpUtil` 类**

为了避免在每次需要发起网络请求时都重复编写相同的代码，将网络请求封装到 `HttpUtil` 类中，使用静态方法 `sendHttpRequest` 发送HTTP请求：

```java
public class HttpUtil {
    public static String sendHttpRequest(String address) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
```

使用时，只需调用 `HttpUtil.sendHttpRequest(address)` 即可。

**2. 异步处理网络请求**

由于网络请求是耗时操作，直接在主线程中执行会导致界面卡顿。

因此，应该将网络请求放到子线程中执行。如果直接在 `sendHttpRequest` 方法中开子线程，会导致无法获取到请求的返回结果。

为了解决这个问题，可以使用回调机制。

**3. 使用回调机制**

首先，定义一个回调接口 `HttpCallbackListener`：

```java
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
```

然后修改 `HttpUtil` 类，使用回调接口来处理网络请求的结果：

```java
public class HttpUtil {
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());  // 回调onFinish()
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);  // 回调onError()
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
```

调用时，将 `HttpCallbackListener` 的实现传入 `sendHttpRequest` 方法：

```java
HttpUtil.sendHttpRequest("http://www.baidu.com", new HttpCallbackListener() {
    @Override
    public void onFinish(String response) {
        // 处理成功响应
    }

    @Override
    public void onError(Exception e) {
        // 处理异常
    }
});
```

这样，在服务器响应后，回调 `onFinish` 方法进行处理；如果发生异常，则回调 `onError` 方法。

**4. 使用 OkHttp 简化代码**

虽然 `HttpURLConnection` 可以完成任务，但其使用较为繁琐，OkHttp 则提供了更简洁的 API。通过 OkHttp，只需要极少的代码就能完成网络请求。

首先，修改 `HttpUtil` 类，加入 `sendOkHttpRequest` 方法：

```java
public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
```

使用时，传入一个 `okhttp3.Callback` 实现：

```java
HttpUtil.sendOkHttpRequest("http://www.baidu.com", new okhttp3.Callback() {
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        // 获取服务器返回的数据
        String responseData = response.body().string();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        // 处理失败情况
    }
});
```

OkHttp 自动管理子线程，开发者只需要关注请求的响应结果或错误处理，极大地简化了代码量。

**5. 注意事项**

- 无论使用 `HttpURLConnection` 还是 `OkHttp`，网络请求都应该在子线程中执行，避免阻塞主线程。
- 网络请求的回调接口通常是在子线程中执行的，因此如果需要更新 UI，需要使用 `runOnUiThread` 进行线程切换。
