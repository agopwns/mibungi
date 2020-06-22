package com.example.mibungi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.gson.JsonObject;



public class MyUtil {

    private static final String TAG = "MyUtil";

    public static boolean IsNetworkConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static void setToast(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String setJson(String flag, String sender_id, String receiver_id, String msg, String room_id){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("flag", flag);
        jsonObject.addProperty("sender_id", sender_id);
        jsonObject.addProperty("receiver_id", receiver_id);
        jsonObject.addProperty("msg", msg);
        jsonObject.addProperty("room_id", room_id);

        String returnJson = jsonObject.toString();
        return returnJson;
    }
}
