package com.example.mibungi;

import android.app.Application;

public class ChatApplication extends Application {

    private static ChatApplication mInstance;
    private ChatServiceInterface mInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInterface = new ChatServiceInterface(getApplicationContext());
    }

    public static ChatApplication getInstance() {
        return mInstance;
    }

    public ChatServiceInterface getServiceInterface() {
        return mInterface;
    }
}
