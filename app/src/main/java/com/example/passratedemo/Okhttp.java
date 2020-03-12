package com.example.passratedemo;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Okhttp {
    private static OkHttpClient client;

    static {
        client = new OkHttpClient();
    }

    public static void request(String url, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
