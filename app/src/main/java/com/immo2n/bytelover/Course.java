package com.immo2n.bytelover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class Course extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        TextView title = findViewById(R.id.course_title),
                short_des = findViewById(R.id.course_short_description),
                full_des = findViewById(R.id.course_full_description),
                code = findViewById(R.id.course_code),
                level = findViewById(R.id.course_level),
                batch = findViewById(R.id.course_batch),
                slot = findViewById(R.id.course_slot),
                duration = findViewById(R.id.course_duration),
                date_start = findViewById(R.id.course_date_start),
                date_end = findViewById(R.id.course_date_end),
                course_lang = findViewById(R.id.course_language),
                price = findViewById(R.id.course_price);

        Objects.requireNonNull(getSupportActionBar()).hide();
        findViewById(R.id.back).setOnClickListener(v-> this.finish());
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main));
        Intent intent = getIntent();
        ArrayList<String> data = intent.getStringArrayListExtra("data");
        if(null == data || data.size() == 0){
            Toast.makeText(this, "Failed to open! Try again", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        com.immo2n.bytelover.Objects.Course course = new com.immo2n.bytelover.Objects.Course(
                data.get(0),
                data.get(1),
                data.get(2),
                data.get(3),
                data.get(4),
                data.get(5),
                data.get(6),
                data.get(7),
                data.get(8),
                data.get(9),
                data.get(10),
                data.get(11),
                data.get(12),
                data.get(13),
                data.get(14),
                data.get(15),
                data.get(16)
        );
        title.setText(course.getName());
        TextView page_title = findViewById(R.id.title);
        page_title.setText(course.getName());
        short_des.setText(course.getShortDescription());
        full_des.setText(course.getMainDescription());
        code.setText(String.format(": %s", course.getCourseCode()));
        level.setText(String.format(": %s", course.getLevel()));
        batch.setText(String.format(": %s", course.getBatch()));
        slot.setText(String.format(": %s", course.getSlot()));
        duration.setText(String.format(": %s", course.getDuration()));
        date_start.setText(String.format(": %s", course.getStartDate()));
        date_end.setText(String.format(": %s", course.getEndDate()));
        course_lang.setText(String.format(": %s", course.getCourseLanguage()));
        price.setText(String.format(" %s", course.getPrice()));

        findViewById(R.id.share).setOnClickListener(v -> {
            String link = "https://bytelover.com/courses/"+course.getCourseCode()+"?ref=app";
            Intent intent_share = new Intent(Intent.ACTION_SEND);
            intent_share.setType("text/plain");
            intent_share.putExtra(Intent.EXTRA_TEXT, link);
            startActivity(Intent.createChooser(intent_share, "Share via"));
        });
    }
}