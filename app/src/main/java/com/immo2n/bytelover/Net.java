package com.immo2n.bytelover;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Net {
    private static Handler netHandler;
    private static WeakReference<Global> globalRef;
    public Net(Handler handler, Global global_object) {
        netHandler = handler;
        globalRef = new WeakReference<>(global_object);
    }
    public void get(String url) {
        httpRequest(url, "GET");
    }

    //This is separated from http request method because it has to transfer payloads
    public void post(String url, String payload){
        if(globalRef.get().netConnected()){
            Message message = netHandler.obtainMessage();
            message.obj = "ERROR_NO_NET";
            netHandler.sendMessage(message);
            return;
        }
        new Thread(() -> {
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                os.write(payload.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Message message = netHandler.obtainMessage();
                message.obj = response.toString();
                netHandler.sendMessage(message);
            }
            catch (Exception e){
                Message message = netHandler.obtainMessage();
                message.obj = e.toString();
                netHandler.sendMessage(message);
            }
        }).start();
    }
    public void data(String url, String token) {
        new Thread(() -> {
            if(globalRef.get().netConnected()){
                Message message = netHandler.obtainMessage();
                message.obj = "ERROR_NO_NET";
                netHandler.sendMessage(message);
                return;
            }
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream os = con.getOutputStream();
                os.write(("token=" + globalRef.get().makeUrlSafe(token)).getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Message message = netHandler.obtainMessage();
                message.obj = response.toString();
                netHandler.sendMessage(message);
            }
            catch (Exception e){
                Message message = netHandler.obtainMessage();
                message.obj = e.toString();
                netHandler.sendMessage(message);
            }
        }).start();
    }
    public static void httpRequest(String url, String netMethod) {
        new Thread(() -> {
            if(globalRef.get().netConnected()){
                Message message = netHandler.obtainMessage();
                message.obj = "ERROR_NO_NET";
                netHandler.sendMessage(message);
                return;
            }
            String method = netMethod;
            try {
                if (!method.equals("GET") && !method.equals("POST")) {
                    method = "GET";
                }
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod(method);
                con.setRequestProperty("Accept", "application/json");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Message message = netHandler.obtainMessage();
                message.obj = response.toString();
                netHandler.sendMessage(message);
            }
            catch (Exception e){
                Message message = netHandler.obtainMessage();
                message.obj = e.toString();
                netHandler.sendMessage(message);
            }
        }).start();
    }
}
