package com.immo2n.bytelover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Slides extends AppCompatActivity {
    private Global global;
    private int current_slide = 1;
    private boolean only_go_back = false;
    private boolean skip_now_prev = false;
    private GestureDetector gestureDetector;
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slides);
        Objects.requireNonNull(getSupportActionBar()).hide();
        global = new Global(this);
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_robust));
        List<LinearLayout> slides = new ArrayList<>();
        slides.add(findViewById(R.id.slide_one));
        slides.add(findViewById(R.id.slide_two));
        slides.add(findViewById(R.id.slide_three));
        slides.add(findViewById(R.id.slide_four));
        ConstraintLayout main_body = findViewById(R.id.slide_background);
        only_go_back = getIntent().getBooleanExtra("comeback", false);

        findViewById(R.id.continue_app).setOnClickListener(v-> continue_app());
        findViewById(R.id.skip).setOnClickListener(v-> {
            if(skip_now_prev){
                current_slide-= 2;
                findViewById(R.id.slide_next).performClick();
                return;
            }
            continue_app();
        });
        findViewById(R.id.slide_next).setOnClickListener(v -> {
            current_slide++;
            if(current_slide <= 4){
                int x = 1;
                while (x <= 4){
                    if(current_slide != x){
                        //Hide it
                        slides.get(x-1).setVisibility(View.GONE);
                    }
                    x++;
                }
            }
            else {
                //No more slides
                current_slide = 1;
            }
            //New slide
            slides.get(current_slide-1).setVisibility(View.VISIBLE);
            if(current_slide == 2){
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.feature_card_two));
                main_body.setBackground(ContextCompat.getDrawable(this, R.color.feature_card_two));
            }
            else if(current_slide == 3){
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.feature_card_three));
                main_body.setBackground(ContextCompat.getDrawable(this, R.color.feature_card_three));
            }
            else if(current_slide == 4){
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.feature_card_four));
                main_body.setBackground(ContextCompat.getDrawable(this, R.color.feature_card_four));
            }
            else {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_robust));
                main_body.setBackground(ContextCompat.getDrawable(this, R.color.main_robust));
            }
            if(current_slide == 4){
                findViewById(R.id.slide_next).setVisibility(View.GONE);
                findViewById(R.id.continue_app).setVisibility(View.VISIBLE);
            }
            else {
                if(findViewById(R.id.slide_next).getVisibility() == View.GONE) {
                    findViewById(R.id.slide_next).setVisibility(View.VISIBLE);
                }
                if(findViewById(R.id.continue_app).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.continue_app).setVisibility(View.GONE);
                }
                TextView skip_icon = findViewById(R.id.skip_icon), text = findViewById(R.id.skip_text);
                if(!skip_now_prev) {
                    skip_icon.setText("\\\uf060");
                    text.setText("Previous");
                    skip_now_prev = true;
                }
                if(current_slide == 1){
                    skip_icon.setText("\\\uf00d");
                    text.setText("Skip");
                    skip_now_prev = false;
                }
            }
        });
        //Swipes
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // Swipe right
                            if(current_slide == 1){
                                current_slide = 3;
                                findViewById(R.id.slide_next).performClick();
                                return true;
                            }
                            if(current_slide == 4){
                                current_slide = 2;
                                findViewById(R.id.slide_next).performClick();
                                return true;
                            }
                            findViewById(R.id.skip).performClick();
                        } else {
                            // Swipe left
                            if(current_slide == 4){
                                current_slide = 2;
                                findViewById(R.id.skip).performClick();
                                return true;
                            }
                            findViewById(R.id.slide_next).performClick();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        });
        findViewById(R.id.slide_background).setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }
    private void continue_app() {
        if(only_go_back){
            this.finish();
            return;
        }
        //Newly start the app
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}