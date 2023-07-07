package com.immo2n.bytelover;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.immo2n.bytelover.CoreClasses.Net;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Splash extends AppCompatActivity {
    private Global global;
    private LottieAnimationView loading;
    private TextView net_alert;
    private boolean flag_tried_net = false, invalid_response = false;
    private Button try_again;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        global = new Global(this);
        if(global.wasDarkModeOn()){
            global.enableDarkMode();
        }
        else {
            global.disableDarkMode();
        }
        setContentView(R.layout.activity_splash);
        TextView production = findViewById(R.id.production);
        production.setText(global.appVersion+" - "+production.getText());
        production.setTextColor(ContextCompat.getColor(this, R.color.brand));
        Objects.requireNonNull(getSupportActionBar()).hide();
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //Send a get request to read version data and connection
        loading = findViewById(R.id.loading);
        loading.playAnimation();
        net_alert = findViewById(R.id.no_net);
        try_again = findViewById(R.id.try_again);
        /*Send a get request with a random signature to make sure data is fresh
        Go to error if caught an error.
        General get request block, with separate thread
        Make different handlers for concurrent requests with concurrent threads
        "newAndroidClient" is very important and must be included for the stating screen
         */
        load();
        try_again.setOnClickListener(v-> {
            if(!flag_tried_net){
                flag_tried_net = true;
            }
            load();
        });
    }
    private void load(){
        try_again.setVisibility(View.GONE);
        net_alert.setVisibility(View.GONE);
        loading.playAnimation();
        loading.setVisibility(View.VISIBLE);
        Handler starterHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String data = msg.obj.toString();
                if(data.equals("ERROR_NO_NET")){
                    error_net();
                }
                else {
                    success_net(data);
                }
            }
        };
        Net net = new Net(starterHandler, global, false);
        net.get(global.server + "/?newAndroidClient="+
                global.makeUrlSafe(global.getAndroidId(this))+ "&pushToken=" +
                global.makeUrlSafe(Push.getToken(this)));
    }
    private void error_net(){
        if(!invalid_response) {
            if (flag_tried_net) {
                Toast.makeText(this, "No internet!", Toast.LENGTH_SHORT).show();
            }
            net_alert.setText(R.string.no_internet_connection);
        }
        else {
            net_alert.setText(R.string.server_busy);
            invalid_response = false;
        }
        loading.setVisibility(View.GONE);
        loading.pauseAnimation();
        net_alert.setVisibility(View.VISIBLE);
        try_again.setVisibility(View.VISIBLE);
    }
    private void success_net(String data){
        try {
            Map<String, Object> responseObject = global.jsonMap(data);
            String signature = (String)responseObject.get("signature");
            if(null == signature || !signature.equals(global.getAndroidId(this))){
                Log.d("SPLASH-ERROR", "Signature mismatch");
                invalid_response = true;
                //To avoid connected toast
                flag_tried_net = false;
                error_net();
                return;
            }
            String android_latest = (String)responseObject.get("android_latest"),
                   status = (String)responseObject.get("status");
            //Risky but managed from server side
            ArrayList android_accepted = (ArrayList) responseObject.get("android_accepted");
            if(null != android_latest && !android_latest.equals(global.appVersion)){
                //If accepted then process login
                boolean accepted = false;
                if(null != android_accepted) {
                    for (Object version : android_accepted) {
                        if (version.equals(global.appVersion)) {
                            accepted = true;
                            break;
                        }
                    }
                }
                else {
                    error_net();
                    return;
                }
                if(!accepted){
                    //Must be updated!
                    Toast.makeText(this, "App must be updated", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //Maintenance
            if(null != status && status.equals("maintenance")){
                Intent error_intent = new Intent(this, Error.class);
                error_intent.putExtra("type", "maintenance");
                startActivity(error_intent);
                finish();
                return;
            }
            //Finally login
            login();
        }
        catch (Exception e){
            invalid_response = true;
            //To avoid connected toast
            flag_tried_net = false;
            error_net();
        }
    }
    private void login(){
        //Goto welcome slides
        if(global.newlyInstalled()) {
            Intent welcome = new Intent(this, Slides.class);
            startActivity(welcome);
            this.finish();
            return;
        }
        //Go to dashboard directly if logged in
        if(global.isLoggedIn()){
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            finish();
            return;
        }
        //Home for every user - later ask to log in
        Intent home = new Intent(this, Home.class);
        startActivity(home);
        this.finish();
    }
}