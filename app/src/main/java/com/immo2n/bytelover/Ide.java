package com.immo2n.bytelover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Ide extends AppCompatActivity {
    private Global global;
    private WebView webview;
    private String link;
    private ProgressBar progressBar;
    private LinearLayout error_view, loading_view;
    private LottieAnimationView morty;
    //Link set
    private String link_c = "https://www.programiz.com/c-programming/online-compiler/",
                    link_cpp = "https://www.programiz.com/cpp-programming/online-compiler/",
                    link_csharp = "https://www.programiz.com/csharp-programming/online-compiler/",
                    link_java = "https://www.programiz.com/java-programming/online-compiler/",
                    link_python = "https://www.programiz.com/python-programming/online-compiler/",
                    link_php = "https://www.programiz.com/php/online-compiler/",
                    link_html = "https://www.programiz.com/html/online-compiler/",
                    link_js = "https://www.programiz.com/javascript/online-compiler/",
                    link_go = "https://www.programiz.com/golang/online-compiler/",
                    link_sql = "https://www.programiz.com/sql/online-compiler/",
                    link_r = "https://www.programiz.com/r/online-compiler/",
                    link_swift = "https://www.programiz.com/swift/online-compiler/",
                    link_rust = "https://www.programiz.com/rust/online-compiler/";
    //Link set
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ide);
        global = new Global(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        View body = findViewById(R.id.ide_body);
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main));
        //Buttons
        ImageView info = findViewById(R.id.info);
        RelativeLayout back = findViewById(R.id.back);
        //Info window
        Dialog info_dialogue = global.makeDialogue(R.layout.dialogue_ide_info, R.drawable.dialogue_background);
        info_dialogue.setCancelable(true);
        info_dialogue.setCanceledOnTouchOutside(true);
        info.setOnClickListener(v-> info_dialogue.show());
        webview = findViewById(R.id.main_view);
        progressBar = findViewById(R.id.progress);
        morty = findViewById(R.id.morty);
        error_view = findViewById(R.id.error);
        loading_view = findViewById(R.id.loading);
        Button reload = findViewById(R.id.reload);
        reload.setOnClickListener(v-> {
            webview.loadUrl(link);
            error_view.setVisibility(View.GONE);
            morty.pauseAnimation();
            webview.setVisibility(View.VISIBLE);
        });

        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);


        //Start with c
        link = link_c;
        webview.loadUrl(link_c);
        //Buttons
        ImageButton c = findViewById(R.id.button_c),
                    cpp = findViewById(R.id.button_cpp),
                    csharp = findViewById(R.id.button_csharp),
                    java = findViewById(R.id.button_java),
                    python = findViewById(R.id.button_python),
                    php = findViewById(R.id.button_php),
                    html = findViewById(R.id.button_html),
                    js = findViewById(R.id.button_js),
                    go = findViewById(R.id.button_go),
                    sql = findViewById(R.id.button_sql),
                    r = findViewById(R.id.button_r),
                    swift = findViewById(R.id.button_swift),
                    rust = findViewById(R.id.button_rust);
        //Manually used variable to avoid cast warning
        Map<String, ImageButton> ide_buttons = new HashMap<>();
        ide_buttons.put("c", c);
        ide_buttons.put("cpp", cpp);
        ide_buttons.put("csharp", csharp);
        ide_buttons.put("java", java);
        ide_buttons.put("python", python);
        ide_buttons.put("php", php);
        ide_buttons.put("html", html);
        ide_buttons.put("js", js);
        ide_buttons.put("go", go);
        ide_buttons.put("sql", sql);
        ide_buttons.put("r", r);
        ide_buttons.put("swift", swift);
        ide_buttons.put("rust", rust);
        Set<String> map_keys = ide_buttons.keySet();

        //Listeners - no looping is required
        c.setOnClickListener(v-> {
            link = link_c;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "c");
            }
        });

        cpp.setOnClickListener(v-> {
            link = link_cpp;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "cpp");
            }
        });

        csharp.setOnClickListener(v-> {
            link = link_csharp;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "csharp");
            }
        });

        java.setOnClickListener(v -> {
            link = link_java;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "java");
            }
        });

        python.setOnClickListener(v -> {
            link = link_python;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "python");
            }
        });

        php.setOnClickListener(v -> {
            link = link_php;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "php");
            }
        });

        html.setOnClickListener(v -> {
            link = link_html;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "html");
            }
        });

        js.setOnClickListener(v -> {
            link = link_js;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "js");
            }
        });

        go.setOnClickListener(v -> {
            link = link_go;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "go");
            }
        });

        sql.setOnClickListener(v -> {
            link = link_sql;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "sql");
            }
        });

        r.setOnClickListener(v -> {
            link = link_r;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "r");
            }
        });

        swift.setOnClickListener(v -> {
            link = link_swift;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "swift");
            }
        });

        rust.setOnClickListener(v -> {
            link = link_rust;
            webview.loadUrl(link);
            for(String key:map_keys){
                button_mode(ide_buttons, key, "rust");
            }
        });

        //Events
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //Ignorable error
                if(error.getDescription().equals("net::ERR_EMPTY_RESPONSE")
                 || error.getDescription().equals("net::ERR_SOCKET_NOT_CONNECTED")){
                    return;
                }
                webview.loadUrl("about:blank");
                loading_view.setVisibility(View.GONE);
                webview.setVisibility(View.GONE);
                morty.playAnimation();
                error_view.setVisibility(View.VISIBLE);
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (view.getUrl().equals(link_html)) {
                    //Special case
                    //Remove title
                    view.loadUrl("javascript:(function() { " +
                            "var element = document.getElementsByTagName('" + "h1" + "')[0]; " +
                            "element.parentNode.removeChild(element); " +
                            "})()");
                    //Remove logo
                    view.loadUrl("javascript:(function() { " +
                            "var element = document.getElementsByTagName('" + "img" + "')[4]; " +
                            "element.parentNode.removeChild(element); " +
                            "})()");
                }
                else if (view.getUrl().equals(link_sql)){
                    view.loadUrl("javascript:(function() { " +
                            "var element = document.getElementsByTagName('" + "header" + "')[0]; " +
                            "element.parentNode.removeChild(element); " +
                            "})()");
                }
                else {
                    view.loadUrl("javascript:(function() { " +
                            "var element = document.getElementsByClassName('" + "header" + "')[0]; " +
                            "element.parentNode.removeChild(element); " +
                            "})()");
                }
                if(loading_view.getVisibility() != View.VISIBLE){
                    webview.setVisibility(View.GONE);
                    loading_view.setVisibility(View.VISIBLE);
                    if(!webview.getUrl().equals("about:blank")){
                        error_view.setVisibility(View.GONE);
                    }
                }
                progressBar.setProgress(newProgress);
                if(newProgress == 100){
                    webview.setVisibility(View.VISIBLE);
                    loading_view.setVisibility(View.GONE);
                }
            }
        });
        webview.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
                webview.goBack();
                return true;
            }
            return false;
        });
        back.setOnClickListener(v-> this.finish());
    }
    private void button_mode(Map<String, ImageButton> ide_buttons, String key, String compare){
        if(key.equals(compare)){
            Objects.requireNonNull(ide_buttons.get(key)).setBackground(ContextCompat.getDrawable(this, R.color.secondary));
        }
        else {
            Objects.requireNonNull(ide_buttons.get(key)).setBackground(ContextCompat.getDrawable(this, R.color.transparent));
        }
    }
}