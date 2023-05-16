package com.immo2n.bytelover;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

public class Web extends AppCompatActivity {
    private String link;
    private WebView webView;
    private TextView error_code;
    private LinearLayout error_view;
    private LottieAnimationView morty;
    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Global global = new Global(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //Catch link
        link = getIntent().getStringExtra("link");
        if(null == link || link.isEmpty() || !global.isValidLink(link)){
            Toast.makeText(getApplicationContext(), "Invalid link!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        morty = findViewById(R.id.morty);
        View body = findViewById(R.id.WebBody);
        View decor = getWindow().getDecorView();
        RelativeLayout back = findViewById(R.id.back);
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main));
        webView = findViewById(R.id.web_view);
        ProgressBar progressBar = findViewById(R.id.progress);
        ImageView security = findViewById(R.id.security);
        TextView title = findViewById(R.id.page_title), address = findViewById(R.id.page_address);
        Button reload = findViewById(R.id.reload);
        error_view = findViewById(R.id.error);
        error_code = findViewById(R.id.error_code);

        title.setText("Loading...");
        address.setText(link);

        reload.setOnClickListener(v-> {
            webView.loadUrl(link);
            error_view.setVisibility(View.GONE);
            morty.pauseAnimation();
            webView.setVisibility(View.VISIBLE);
        });

        //Link load
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl(link);

        //Events
        webView.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
            return false;
        });
        back.setOnClickListener(v -> finish());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress == 100){
                    String new_link = webView.getUrl();
                    if(!new_link.equals("about:blank")) {
                        link = new_link;
                        address.setText(webView.getUrl());
                        title.setText(webView.getTitle());
                    }
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webView.loadUrl("about:blank");
                error_code.setText(error.getDescription());
                webView.setVisibility(View.GONE);
                morty.playAnimation();
                error_view.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url != null && url.startsWith("https://")) {
                    security.setImageDrawable(ContextCompat.getDrawable(global.getContext(), R.drawable.baseline_lock_24));
                } else {
                    security.setImageDrawable(ContextCompat.getDrawable(global.getContext(), R.drawable.baseline_lock_open_24));
                }
            }
        });
        //Ends
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}