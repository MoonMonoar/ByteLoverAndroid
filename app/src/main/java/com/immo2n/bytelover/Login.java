package com.immo2n.bytelover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.immo2n.bytelover.CoreClasses.Net;

import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {
    private Global global;
    private String request_token;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        global = new Global(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar));
        //Body
        LinearLayout login_sub_body = findViewById(R.id.login_body);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //Keyboard

        //Buttons
        TextView signup = findViewById(R.id.button_signup),
                 policy = findViewById(R.id.policy),
                 terms = findViewById(R.id.terms);
        CheckBox show_password = findViewById(R.id.pass_show);
        Button login = findViewById(R.id.login_button);

        signup.setOnClickListener(v-> {
            Intent page_signup = new Intent(this, Signup.class);
            startActivity(page_signup);
        });
        //Web links
        Intent web_intent = new Intent(this, Web.class);
        terms.setOnClickListener(v-> {
            web_intent.putExtra("link", global.terms_link);
            startActivity(web_intent);
        });
        policy.setOnClickListener(v-> {
            web_intent.putExtra("link", global.policies_link);
            startActivity(web_intent);
        });

        //Fields
        EditText password = findViewById(R.id.password_edit_text),
                 user = findViewById(R.id.user_edit_text);

        //Show password
        show_password.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            password.setSelection(password.getText().length());
            password.setTypeface(global.getTypeface(this));
        });
        //Login process
        login.setOnClickListener(v -> {
            release_all(user, password, imm);
            String user_in = user.getText().toString(),
                   password_in = password.getText().toString();
            if(user_in.isEmpty()){
                Toast.makeText(this, "Enter phone or email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(password_in.isEmpty()){
                Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            login.setEnabled(false);
            login.setText(R.string.logging_in);
            Handler loginHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    String data = msg.obj.toString();
                    if(data.equals("ERROR_NO_NET")){
                        Toast.makeText(global.getContext(), "No internet!", Toast.LENGTH_SHORT).show();
                        login_reset(login);
                    }
                    else {
                        //Process the data
                        Map<String, Object> responseObject = global.jsonMap(data);
                        String status = (String)responseObject.get("status"),
                                signature = (String)responseObject.get("signature"),
                                token = (String)responseObject.get("token");
                        //Errors
                        if(null == status || status.isEmpty() || status.equals("busy")){
                            Toast.makeText(Login.this, "Sorry, server is busy! Try again.", Toast.LENGTH_SHORT).show();
                            login_reset(login);
                            return;
                        }
                        if(status.equals("denied")){
                            Toast.makeText(Login.this, "Server denied! Update app!", Toast.LENGTH_SHORT).show();
                            login_reset(login);
                            return;
                        }
                        if(status.equals("wrong_credentials")){
                            Toast.makeText(Login.this, "Wrong user or password!", Toast.LENGTH_LONG).show();
                            login_reset(login);
                            return;
                        }
                        if(null == token ||
                                token.isEmpty() ||
                                null == signature ||
                                !signature.equals(request_token)){
                            Toast.makeText(Login.this, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
                            login_reset(login);
                            return;
                        }
                        if(global.writeAsfile(token, "userToken.blt")) {
                            //Read check
                            String token_check = global.getUserToken();
                            if(null != token_check && !token_check.isEmpty()){
                                Intent intent = new Intent(global.getContext(), Dashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }
                        Toast.makeText(Login.this, "Could not login! Try again.", Toast.LENGTH_SHORT).show();
                        login_reset(login);
                    }
                }
            };
            request_token = global.getRandom();
            Net net = new Net(loginHandler, global, false);
            net.post(global.server+"/login.php", "user="+global.makeUrlSafe(user_in)+
                    "&pass="+global.makeUrlSafe(password_in)+"&hardwareId="+
                    global.makeUrlSafe(global.getAndroidId(this))
                    +"&requestToken="+request_token, null);
        });
        //Page behaviour
        login_sub_body.setOnClickListener(v -> release_all(user, password, imm));
    }
    private void login_reset(Button login){
        login.setText(R.string.log_in);
        login.setEnabled(true);
    }
    private static void release_all(EditText user, EditText password, InputMethodManager imm){
        user.clearFocus();
        password.clearFocus();
        imm.hideSoftInputFromWindow(user.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
    }
}