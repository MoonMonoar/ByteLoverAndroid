package com.immo2n.bytelover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

public class Error extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        Objects.requireNonNull(getSupportActionBar()).hide();
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //Default
        LottieAnimationView ue_a = findViewById(R.id.unknownError);
        RelativeLayout ue_l = findViewById(R.id.error);
        Button restart = findViewById(R.id.app_restart);
        restart.setOnClickListener(v-> {
            Intent intent = new Intent(this, Splash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        ue_a.playAnimation();
        //Try catching error
        String error = getIntent().getStringExtra("type");
        if(null != error && !error.isEmpty()){
            if("maintenance".equals(error)) {
                RelativeLayout maintenance = findViewById(R.id.maintenance);
                LottieAnimationView devs = findViewById(R.id.devsBusy);
                ue_l.setVisibility(View.GONE);
                ue_a.pauseAnimation();
                devs.playAnimation();
                maintenance.setVisibility(View.VISIBLE);
            }
        }
    }
}