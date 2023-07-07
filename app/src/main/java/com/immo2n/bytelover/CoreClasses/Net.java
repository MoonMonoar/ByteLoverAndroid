package com.immo2n.bytelover.CoreClasses;

import android.os.Handler;
import android.os.Message;

import com.immo2n.bytelover.CrossProgramming.MD5;
import com.immo2n.bytelover.Global;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Net {
    NetCache netCache = NetCache.getInstance();
    private static Handler netHandler;
    private final boolean net_cache_mode;
    private static WeakReference<Global> globalRef;
    public Net(Handler handler, Global global_object, boolean cache) {
        netHandler = handler;
        net_cache_mode = cache;
        globalRef = new WeakReference<>(global_object);
    }
    public void get(String url) {
        httpRequest(url, "GET");
    }
    //This is separated from http request method because it has to transfer payloads
    public void post(String url, String payload, Integer request_identifier){
        String cache_key = MD5.Generate(url+payload); // MASH URL WITH PAYLOAD DATA TO MAKE KEY
        if(null == request_identifier){
            request_identifier = 0;
        }
        if(net_cache_mode){
            //Check cache --  return if exists
            String data = netCache.getDataFromCache(cache_key);
            if(null != data){
                Message message = netHandler.obtainMessage();
                message.obj = data;
                message.what = request_identifier;
                netHandler.sendMessage(message);
                return;
            }
        }
        if(globalRef.get().netConnected()){
            Message message = netHandler.obtainMessage();
            message.what = request_identifier;
            message.obj = "ERROR_NO_NET";
            netHandler.sendMessage(message);
            return;
        }
        Integer finalRequest_identifier = request_identifier;
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
                message.what = finalRequest_identifier;
                message.obj = response.toString();
                netHandler.sendMessage(message);
                if(net_cache_mode){
                    netCache.storeDataInCache(cache_key, response.toString());
                }
            }
            catch (Exception e){
                Message message = netHandler.obtainMessage();
                message.what = finalRequest_identifier;
                message.obj = e.toString();
                netHandler.sendMessage(message);
            }
        }).start();
    }
    public void data(String url, String token) {
        //CACHE FREE - HOT RELOAD EVERY TIME
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
