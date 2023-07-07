package com.immo2n.bytelover;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Objects;

public class Dashboard extends AppCompatActivity {
    private Global global;
    private String user_token;
    private ViewPager2 tabs;

    //TAB-ELEMENTS
    private TextView tab_text_dash, tab_text_tasks, tab_text_quizzes, tab_text_marks,
            tab_text_courses;
    private TextView tab_dash, tab_tasks, tab_quizzes, tab_marks, tab_courses;
    private LinearLayout button_tab_main, button_tab_assignments, button_tab_quizzes,
            button_tab_marks, button_tab_courses;
    private HashMap<Integer, TextView[]> tab_buttons = new HashMap<>();
    //TAB-ELEMENT
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        global = new Global(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        if(!global.isLoggedIn()){
            //Goto login -- User not logged it
            Intent intent = new Intent(global.getContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }
        user_token = global.getUserToken();
        //LOGGED IN -- MAIN DASHBOARD

        //Tab-buttons
        TextView tab_text_dash = findViewById(R.id.tab_text_dash);
        TextView tab_text_tasks = findViewById(R.id.tab_text_tasks);
        TextView tab_text_quizzes = findViewById(R.id.tab_text_quizzes);
        TextView tab_text_marks = findViewById(R.id.tab_text_marks);
        TextView tab_text_courses = findViewById(R.id.tab_text_courses);

        TextView tab_icon_dash = findViewById(R.id.tab_icon_dash);
        TextView tab_icon_tasks = findViewById(R.id.tab_icon_tasks);
        TextView tab_icon_quizzes = findViewById(R.id.tab_icon_quizzes);
        TextView tab_icon_marks = findViewById(R.id.tab_icon_marks);
        TextView tab_icon_courses = findViewById(R.id.tab_icon_courses);

        TextView[] dashPair = {tab_text_dash, tab_icon_dash};
        TextView[] tasksPair = {tab_text_tasks, tab_icon_tasks};
        TextView[] quizzesPair = {tab_text_quizzes, tab_icon_quizzes};
        TextView[] marksPair = {tab_text_marks, tab_icon_marks};
        TextView[] coursesPair = {tab_text_courses, tab_icon_courses};

        tab_buttons.put(0, dashPair);
        tab_buttons.put(1, tasksPair);
        tab_buttons.put(2, quizzesPair);
        tab_buttons.put(3, marksPair);
        tab_buttons.put(4, coursesPair);

        button_tab_main = findViewById(R.id.button_tab_main);
        button_tab_assignments = findViewById(R.id.button_tab_assignments);
        button_tab_quizzes = findViewById(R.id.button_tab_quizzes);
        button_tab_marks = findViewById(R.id.button_tab_marks);
        button_tab_courses = findViewById(R.id.button_tab_courses);
        //Tab-buttons-ends

        tabs = findViewById(R.id.main_pager);
        tabs.setAdapter(new DashboardAdapter(this));
        tabs.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                change_tab_state(position);
                super.onPageSelected(position);
            }
        });
        //Tabs - buttons
        button_tab_main.setOnClickListener(v-> {
            tabs.setCurrentItem(0);
            change_tab_state(0);
        });
        button_tab_assignments.setOnClickListener(v-> {
            tabs.setCurrentItem(1);
            change_tab_state(1);
        });
        button_tab_quizzes.setOnClickListener(v-> {
            tabs.setCurrentItem(2);
            change_tab_state(2);
        });
        button_tab_marks.setOnClickListener(v-> {
            tabs.setCurrentItem(3);
            change_tab_state(3);
        });
        button_tab_courses.setOnClickListener(v-> {
            tabs.setCurrentItem(4);
            change_tab_state(4);
        });
        //Tabs


        //LOGGED IN -- MAIN DASHBOARD ENDS
    }

    private void change_tab_state(int position){
        for(int x = 0; x < 5; x++) {
            TextView[] pair = tab_buttons.get(x);
            if (null != pair && null != pair[0] && null != pair[1]) {
                TextView icon = pair[0], text = pair[1];
                if (x == position) {
                    icon.setTextColor(ContextCompat.getColor(this, R.color.main));
                    text.setTextColor(ContextCompat.getColor(this, R.color.main));
                } else {
                    icon.setTextColor(ContextCompat.getColor(this, R.color.black_light));
                    text.setTextColor(ContextCompat.getColor(this, R.color.grey_text));
                }
            }
        }
    }
    public static void goto_error(Context context, String type){
        Intent error = new Intent(context, Error.class);
        error.putExtra("type", type);
        context.startActivity(error);
    }
    public static void goto_home(Context context){
        Intent home = new Intent(context, Home.class);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(home);
    }

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Quit ByteLover?");
        alert.setMessage("Do you really want to close ByteLover?");
        alert.setPositiveButton("Quit", (dialog, whichButton) -> super.onBackPressed());
        alert.setNegativeButton("Cancel",
                (dialog, whichButton) -> {
                    //Do nothing
                });
        alert.show();
    }
}