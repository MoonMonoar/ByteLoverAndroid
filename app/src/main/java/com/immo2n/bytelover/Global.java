package com.immo2n.bytelover;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

public class Global {
    public final String appVersion = "BL-A-1.0.01", appDatapath;
    public final String server = "https://api.bytelover.com/android",
                        helpline_number = "+8801317215403",
                        facebook_page_id = "118902037834228",
                        messenger_link = "https://m.me/immo2n",
                        website_link = "https://bytelover.com/?ref=app_"+appVersion,
                        terms_link = "https://bytelover.com/policies/terms/?ref=app",
                        policies_link = "https://bytelover.com/policies/privacy/?ref=app";
    public static final String NOTIFICATION_CHANNEL_DEFAULT = "DEFAULT";
    private final Gson gson = new Gson();
    private final Context context;
    public Global(Context target_context){
        context = target_context;
        createNotificationChannels(target_context);
        appDatapath = context.getFilesDir().getAbsolutePath();
    }
    public boolean isNight(){
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
    public String getRandom(){
        Random random = new Random();
        return Integer.toString(random.nextInt(900000) + 100000);
    }
    public Typeface getTypeface(Context context){
        return ResourcesCompat.getFont(context, R.font.ubuntu);
    }
    public boolean netConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null) {
                return !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) && !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            }
        }
        return true;
    }
    public Map<String, Object> jsonMap(String data){
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        return gson.fromJson(data, type);
    }
    public boolean isValidLink(String link) {
        String regex = "^(http|https)://([a-zA-Z0-9\\-.]+\\.[a-zA-Z]{2,}([0-9]+)?)(/[a-zA-Z0-9\\-._?,'&%$#=~]+)*$";
        return link.matches(regex);
    }
    public boolean writeAsfile(String data, String fileName) {
        File file = new File(appDatapath + fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.flush();
        } catch (IOException e) {
           return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public String getUserToken(){
        return readFromfile("userToken.blt");
    }
    public boolean isLoggedIn(){
        String check = getUserToken();
        return null != check && !check.isEmpty();
    }
    public String readFromfile(String fileName) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        File file = new File( appDatapath + fileName);
        if(!file.exists()){
            return null;
        }
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public String getAndroidId(Context context) {
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }
    public String makeUrlSafe(String input) {
        if(null == input){
            return null;
        }
        try {
            String encodedString = URLEncoder.encode(input, "UTF-8");
            encodedString = encodedString.replace("+", "%20");
            encodedString = encodedString.replace("*", "%2A");
            encodedString = encodedString.replace("%7E", "~");
            return encodedString;
        } catch (UnsupportedEncodingException e) {
            return input;
        }
    }
    private void createNotificationChannels(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("DEFAULT",
                    "ByteLover", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("App notifications");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    public Context getContext() {
        return context;
    }
    @SuppressLint("LaunchActivityFromNotification")
    public static void showNotification(Context context, String channel, Integer id, String title, String message, String icon, Map<String, String> data) throws ClassNotFoundException {
        int icon_id = R.mipmap.ic_launcher;
        if(!icon.equals("Default")){
            //Process it

        }

        //data objects - builder level
        Intent target_intent = null;
        Bitmap large_image = null;

        if(!data.toString().equals("null")) {
            //Expected filed -- data level
            String intent;
            String big_image;
            String intent_data;

            //Grabber
            intent = data.get("intent");
            intent_data = data.get("intent_data");
            big_image = data.get("big_image");

            //Processors -- mash data and builder level
            if (null != intent) {
                Class<?> clazz = Class.forName("com.immo2n.bytelover."+intent);
                target_intent = new Intent(context, clazz);
                target_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                if(null != intent_data){
                    target_intent.putExtra("data", intent_data);
                }
            }
            if(null != big_image){
                large_image = getBitmapFromUrl(big_image);
            }
            //Data process ends
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(icon_id);
        if(null != target_intent){
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, target_intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            builder.setContentIntent(pendingIntent);
        }
        else {
            builder.setAutoCancel(true);
        }
        if(null != large_image){
            builder.setLargeIcon(large_image);
        }
        // Show the notification
        notificationManager.notify(id, builder.build());
    }
    private static Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            input.close();
            connection.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Dialog makeDialogue(int layout, int background){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, background));
        dialog.setCanceledOnTouchOutside(true);
        Window d_window = dialog.getWindow();
        d_window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }
    public int pixelsToDp(int pixels) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        return Math.round(pixels / density);
    }
    public int dpToPixels(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        return Math.round(dp * density);
    }
    public boolean newlyInstalled(){
        String install_data_file = "InstallData.txt";
        if(readFromfile(install_data_file) != null){
            return false;
        }
        //Also save the data
        String data = "Install time: "+getCurrentDate()+"\nHardware ID: "+
                getAndroidId(context)+"\n Base version: "+appVersion;
        writeAsfile(data, install_data_file);
        return true;
    }
    public String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
    public void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
    public void openFacebookPage(String pageId) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + pageId));
            context.startActivity(intent);
        } catch (Exception e) {
            // Facebook app not installed, open in browser
            openUrl("https://www.facebook.com/" + pageId);
        }
    }
    public void makeCall(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }
    public void enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        writeAsfile("Yes", "DarkMode.txt");
    }
    public void disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        writeAsfile("No", "DarkMode.txt");
    }
    public boolean wasDarkModeOn(){
        String data = readFromfile("DarkMode.txt");
        return null == data || data.equals("Yes");
    }
    public boolean isDarkModeOn(){
        // Check if night mode is enabled
        UiModeManager uiManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

    public Gson getGson() {
        return gson;
    }
}