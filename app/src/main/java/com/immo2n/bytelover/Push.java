package com.immo2n.bytelover;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

public class Push extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("push_token", token).apply();
    }
    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("push_token", null);
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        //No intent Notification
        try {
            int id = 0;
            if(!message.getData().toString().equals("null")) {
                String id_s = message.getData().get("id");
                if (null != id_s) {
                    id = Integer.parseInt(id_s);
                }
            }
            Global.showNotification(getApplicationContext(),
                    Global.NOTIFICATION_CHANNEL_DEFAULT,
                    id,
                    Objects.requireNonNull(message.getNotification()).getTitle(),
                    Objects.requireNonNull(message.getNotification().getBody()),
                    Objects.requireNonNull(message.getNotification().getIcon()),
                    message.getData()
            );
        }
        catch (Exception e){
            Log.d("Push error", e.toString());
        }
    }
}
