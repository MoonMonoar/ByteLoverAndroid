package com.immo2n.bytelover;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Objects;

public class Home extends AppCompatActivity {
    private Global global;
    public static boolean is_user_logged_in;
    private boolean second_tab_open = false;
    private ViewPager2 tabs;
    private LinearLayout home_active, course_active;
    private TextView home_inactive, course_inactive;
    private Server server;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();
        global = new Global(this);
        is_user_logged_in = global.isLoggedIn();
        server = new Server(global);
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main));
        //Clock
        TextClock textClock = findViewById(R.id.home_clock);
        textClock.setFormat12Hour("hh:mm:ss a");
        textClock.setFormat24Hour("HH:mm:ss");
        textClock.setTimeZone("Asia/Dhaka");
        textClock.setAllCaps(true);

        //Tabs
        tabs = findViewById(R.id.home_pager);
        HomeAdapter adapter = new HomeAdapter(this);
        tabs.setAdapter(adapter);

        findViewById(R.id.login_button).setOnClickListener(v-> goto_dash(this));

        //The menu
        findViewById(R.id.home_menu).setOnClickListener(v -> {
            //Create the dialogue
            Dialog menu = global.makeDialogue(R.layout.home_menu, R.drawable.menu_background);
            menu.setCanceledOnTouchOutside(false);
            Window menu_window = menu.getWindow();
            TextView production = menu_window.findViewById(R.id.production);
            production.setText(global.appVersion+" - "+production.getText());
            menu_window.findViewById(R.id.close_menu).setOnClickListener(v1 -> menu.dismiss());
            Intent web_intent = new Intent(this, Web.class);
            menu_window.findViewById(R.id.terms).setOnClickListener(v2-> {
                web_intent.putExtra("link", global.terms_link);
                startActivity(web_intent);
            });
            menu_window.findViewById(R.id.policy).setOnClickListener(v3-> {
                web_intent.putExtra("link", global.policies_link);
                startActivity(web_intent);
            });
            menu_window.findViewById(R.id.facebook).setOnClickListener(v4-> global.openFacebookPage(global.facebook_page_id));
            menu_window.findViewById(R.id.messenger).setOnClickListener(v5-> {
                global.openUrl(global.messenger_link);
                Toast.makeText(this, "Opening messenger...", Toast.LENGTH_LONG).show();
            });
            menu_window.findViewById(R.id.call).setOnClickListener(v6-> global.makeCall(global.helpline_number));
            menu_window.findViewById(R.id.website).setOnClickListener(v7-> global.openUrl(global.website_link));
            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch dark_mode = menu_window.findViewById(R.id.toggle_dark);
            menu_window.findViewById(R.id.mode_changer).setOnClickListener(v8-> {
                dark_mode.toggle();
                dark_mode_toggle(dark_mode.isChecked(), menu);
            });
            if(global.wasDarkModeOn()){
                dark_mode.setChecked(true);
            }
            dark_mode.setOnClickListener(v8-> dark_mode_toggle(dark_mode.isChecked(), menu));
            menu.show();
        });

        //Active buttons
        home_active = findViewById(R.id.home_active);
        course_active = findViewById(R.id.course_active);
        //Inactive buttons
        home_inactive = findViewById(R.id.home_inactive);
        course_inactive = findViewById(R.id.course_inactive);

        tabs.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tab_mode(position == 0);
            }
        });

        //Button press
        course_inactive.setOnClickListener(v -> {
            tab_mode(false);
            tabs.setCurrentItem(1);
        });
        home_inactive.setOnClickListener(v -> {
            tab_mode(true);
            tabs.setCurrentItem(0);
        });
    }

    //Home methods
    public static void goto_dash(Context context) {
        //Check if already logged in
        Intent intent = new Intent(context, Login.class);
        if(is_user_logged_in) {
            intent = new Intent(context, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }
    private void tab_mode(boolean is_home){
        if(is_home){
            second_tab_open = false;
            home_active.setVisibility(View.VISIBLE);
            home_inactive.setVisibility(View.GONE);
            course_active.setVisibility(View.GONE);
            course_inactive.setVisibility(View.VISIBLE);
        }
        else {
            second_tab_open = true;
            home_active.setVisibility(View.GONE);
            home_inactive.setVisibility(View.VISIBLE);
            course_active.setVisibility(View.VISIBLE);
            course_inactive.setVisibility(View.GONE);
        }
    }
    private void dark_mode_toggle(boolean val, Dialog menu){
        if(val){
            Toast.makeText(this, "Dark mode enabled", Toast.LENGTH_SHORT).show();
            global.enableDarkMode();
        }
        else {
            Toast.makeText(this, "Dark mode disabled", Toast.LENGTH_SHORT).show();
            global.disableDarkMode();
        }
        menu.dismiss();
    }
    @Override
    protected void onResume() {
        super.onResume();
        tab_mode(tabs.getCurrentItem() == 0);
    }

    @Override
    public void onBackPressed() {
        if(second_tab_open){
            tab_mode(true);
            tabs.setCurrentItem(0);
            return;
        }
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