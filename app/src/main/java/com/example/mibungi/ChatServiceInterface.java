package com.example.mibungi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.mibungi.dto.MessageDTO;

public class ChatServiceInterface {

    private ServiceConnection mServiceConnection;
    private ChatService mService;

    public ChatServiceInterface(Context context) {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = ((ChatService.ChatServiceBinder) service).getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServiceConnection = null;
                mService = null;
            }
        };
        context.bindService(new Intent(context, ChatService.class)
                .setPackage(context.getPackageName()), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void setTempRoomId(String roomId) {
        if (mService != null) {
            mService.setTempRoomId(roomId);
        }
    }
    public void sendToChatServer(String message) {
        if (mService != null) {
            mService.sendToChatServer(message);
        }
    }
    public MessageDTO getSendMessageDTO() {
        return mService.getSendMessageDTO();
    }

    public MessageDTO getReceiveMessageDTO() {
        return mService.getReceiverMessageDTO();
    }



}
