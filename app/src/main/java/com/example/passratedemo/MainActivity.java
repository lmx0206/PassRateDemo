package com.example.passratedemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ib_back;
    private WebView wb_web;
    private String url = "http://192.168.1.111:8085/dataInterface/PassRate/getAll";
    private List<Integer> list = new ArrayList<>();
    private RadioGroup rg_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData();
    }

    private void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        wb_web = (WebView) findViewById(R.id.wb_web);
        wb_web.getSettings().setJavaScriptEnabled(true);
        wb_web.getSettings().setUseWideViewPort(true);
        wb_web.getSettings().setLoadWithOverviewMode(true);
        wb_web.loadUrl("file:///android_asset/echart.html");
        rg_group = (RadioGroup) findViewById(R.id.rg_group);
        rg_group.getChildAt(0).performClick();
        rg_group.setOnCheckedChangeListener(((group, checkedId) -> {
           switch (checkedId) {
               case 1:
                   showBar();
                   break;
               case 2:
                   showPie();
                   break;
           }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }
    }

    private void getData() {
        Okhttp.request(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                PassRate passRate = new Gson().fromJson(response.body().string(), PassRate.class);
                for (int i = 0; i < passRate.getData().size(); i++) {
                    list.add(passRate.getData().get(i).getRate());
                }
                String dataArray = Arrays.toString(list.toArray());
                runOnUiThread(() -> wb_web.post(() -> wb_web.loadUrl("javaScript:showBar(" + dataArray + ")")));
            }
        });
    }

    private void showBar(){
        if (!list.isEmpty()) {
            String dataArray = Arrays.toString(list.toArray());
            runOnUiThread(() -> wb_web.post(() -> wb_web.loadUrl("javaScript:showBar(" + dataArray + ")")));
        }
    }

    private void showPie() {
        int a, b, c;
        if (!list.isEmpty()) {
            a = list.get(0);
            b = list.get(1);
            c = list.get(2);
            runOnUiThread(() -> wb_web.post(() -> wb_web.loadUrl("javaScript:showPie(" + a + "," + b + "," + c + ")")));
        }
    }

}
