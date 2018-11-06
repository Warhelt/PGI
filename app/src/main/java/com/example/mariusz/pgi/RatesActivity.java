package com.example.mariusz.pgi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RatesActivity extends AppCompatActivity {

    WebView webView;
    @BindView(R.id.wig20Btn)
    Button wig20Btn;
    @BindView(R.id.wig40Btn)
    Button wig40Btn;
    @BindView(R.id.wig80Btn)
    Button wig80Btn;

    public String ratesWig20 = "ratesWIG20.html";
    public String ratesWig40 = "ratesWIG40.html";
    public String ratesWig80 = "ratesWIG80.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        ButterKnife.bind(this);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + ratesWig20);
    }

    @OnClick(R.id.wig20Btn)
    public void getWig20Rates(){
        webView.loadUrl("file:///android_asset/" + ratesWig20);
    }

    @OnClick(R.id.wig40Btn)
    public void getWig40Rates(){
        webView.loadUrl("file:///android_asset/" + ratesWig40);
    }

    @OnClick(R.id.wig80Btn)
    public void getWig80Rates(){
        webView.loadUrl("file:///android_asset/" + ratesWig80);
    }
}

