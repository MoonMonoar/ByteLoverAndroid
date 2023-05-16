package com.immo2n.bytelover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class Signup extends AppCompatActivity {
    private Global global;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        global = new Global(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        View body = findViewById(R.id.SignupBody);
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar));
        LinearLayout signup_body = findViewById(R.id.signup_body);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //Keyboard

        //Buttons
        TextView terms = findViewById(R.id.terms), policies = findViewById(R.id.policies);
        CheckBox show_password = findViewById(R.id.pass_show);

        //Fields
        EditText password = findViewById(R.id.password_edit_text),
                 email = findViewById(R.id.email_edit_text),
                 phone = findViewById(R.id.phone_edit_text),
                 full_name = findViewById(R.id.username_edit_text);

        //Show password
        show_password.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                password.setSelection(password.getText().length());
                password.setTypeface(global.getTypeface(this));
            }
            else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setSelection(password.getText().length());
                password.setTypeface(global.getTypeface(this));
            }
        });

        //Web links
        Intent web_intent = new Intent(this, Web.class);
        terms.setOnClickListener(v-> {
            web_intent.putExtra("link", global.terms_link);
            startActivity(web_intent);
        });
        policies.setOnClickListener(v-> {
            web_intent.putExtra("link", global.policies_link);
            startActivity(web_intent);
        });
        //Page behaviour
        signup_body.setOnClickListener(v-> release_all(full_name, email, phone, password, imm));
    }
    private static void release_all(EditText user, EditText email, EditText phone, EditText password, InputMethodManager imm){
        user.clearFocus();
        password.clearFocus();
        phone.clearFocus();
        email.clearFocus();
        imm.hideSoftInputFromWindow(user.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
    }
}