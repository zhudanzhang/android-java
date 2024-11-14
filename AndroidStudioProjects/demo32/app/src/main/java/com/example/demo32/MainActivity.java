package com.example.demo32;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

//    private void sendRequestWithOkHttp() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            .url("http://www.baidu.com")
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    showResponse(responseData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void showResponse(final String response) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                responseText.setText(response);
//            }
//        });
//    }

//    xml 解析  Pull解析
//    private void sendRequestWithOkHttp() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            // 指定访问的服务器地址是电脑本机
//                            .url("http://192.168.3.220/get_data.xml")
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    parseXMLWithPull(responseData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void parseXMLWithPull(String xmlData) {
//        try {
//            Log.d("MainActivity", "XML Data: " + xmlData);  // 打印 XML 数据
//            // 创建XmlPullParserFactory实例
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            XmlPullParser xmlPullParser = factory.newPullParser();
//            xmlPullParser.setInput(new StringReader(xmlData)); // 设置XML数据
//
//            int eventType = xmlPullParser.getEventType(); // 获取事件类型
//            String id = "", name = "", version = "";
//
//            // 解析XML数据
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String nodeName = xmlPullParser.getName(); // 获取当前节点名称
//                switch (eventType) {
//                    case XmlPullParser.START_TAG: {
//                        // 解析节点的内容
//                        if ("id".equals(nodeName)) {
//                            id = xmlPullParser.nextText();
//                        } else if ("name".equals(nodeName)) {
//                            name = xmlPullParser.nextText();
//                        } else if ("version".equals(nodeName)) {
//                            version = xmlPullParser.nextText();
//                        }
//                        break;
//                    }
//                    case XmlPullParser.END_TAG: {
//                        // 完成解析某个节点时
//                        if ("app".equals(nodeName)) {
//                            Log.d("MainActivity", "id is " + id);
//                            Log.d("MainActivity", "name is " + name);
//                            Log.d("MainActivity", "version is " + version);
//                        }
//                        break;
//                    }
//                    default:
//                        break;
//                }
//                eventType = xmlPullParser.next(); // 获取下一个事件
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//     SAX解析
//    private void sendRequestWithOkHttp() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            .url("http://192.168.3.220/get_data.xml") // 服务器地址
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    parseXMLWithSAX(responseData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void parseXMLWithSAX(String xmlData) {
//        try {
//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
//            ContentHandler handler = new ContentHandler();
//            xmlReader.setContentHandler(handler);
//            xmlReader.parse(new InputSource(new StringReader(xmlData)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    使用 `JSONObject` 解析JSON数据
//    private void sendRequestWithOkHttp() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            // 指定访问的服务器地址
//                            .url("http://192.168.3.220/get_data.json")
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    parseJSONWithJSONObject(responseData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void parseJSONWithJSONObject(String jsonData) {
//        try {
//            JSONArray jsonArray = new JSONArray(jsonData); // 将JSON字符串转换成JSONArray
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i); // 获取每个JSON对象
//                String id = jsonObject.getString("id");
//                String name = jsonObject.getString("name");
//                String version = jsonObject.getString("version");
//
//                // 打印解析出的数据
//                Log.d("MainActivity", "id is " + id);
//                Log.d("MainActivity", "name is " + name);
//                Log.d("MainActivity", "version is " + version);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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