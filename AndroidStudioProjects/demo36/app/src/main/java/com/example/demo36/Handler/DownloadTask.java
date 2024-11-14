package com.example.demo36.Handler;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.demo36.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class DownloadTask {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;
    public static final int TYPE_PROGRESS = 4;

    private DownloadListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;

    private Handler handler;

    public DownloadTask(DownloadListener listener, Handler handler) {
        this.listener = listener;
        this.handler = handler;
    }

    public void startDownload(final String downloadUrl) {
        new Thread(() -> {
            InputStream is = null;
            RandomAccessFile savedFile = null;
            File file = null;
            try {
                long downloadedLength = 0;
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).getPath();
                file = new File(directory + fileName);
                if (file.exists()) {
                    downloadedLength = file.length();
                }

                long contentLength = getContentLength(downloadUrl);
                if (contentLength == 0) {
                    sendMessage(TYPE_FAILED);
                    return;
                }
                if (contentLength == downloadedLength) {
                    sendMessage(TYPE_SUCCESS);
                    return;
                }

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                        .url(downloadUrl)
                        .build();
                Response response = client.newCall(request).execute();

                if (response != null) {
                    is = response.body().byteStream();
                    savedFile = new RandomAccessFile(file, "rw");
                    savedFile.seek(downloadedLength);
                    byte[] b = new byte[1024];
                    int total = 0;
                    int len;
                    while ((len = is.read(b)) != -1) {
                        if (isCanceled) {
                            sendMessage(TYPE_CANCELED);
                            return;
                        }
                        if (isPaused) {
                            sendMessage(TYPE_PAUSED);
                            return;
                        }

                        total += len;
                        savedFile.write(b, 0, len);
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        sendProgress(progress);
                    }
                    response.body().close();
                    sendMessage(TYPE_SUCCESS);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendMessage(TYPE_FAILED);
            } finally {
                try {
                    if (is != null) is.close();
                    if (savedFile != null) savedFile.close();
                    if (isCanceled && file != null) file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendProgress(int progress) {
        // 延时处理
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.what = TYPE_PROGRESS;
                message.arg1 = progress;
                handler.sendMessage(message);
            }
        }, 100); // 设置100ms的延迟
    }

    private void sendMessage(int status) {
        Message message = handler.obtainMessage();
        message.what = status;
        handler.sendMessage(message);
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}
