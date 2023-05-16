package com.immo2n.bytelover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {
    private Global global;
    private String user_token;
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







        //LOGGED IN -- MAIN DASHBOARD ENDS
    }
}