package com.example.mibungi.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class MessageDTO {
    @PrimaryKey
    @SerializedName("flag")
    String flag;
    @SerializedName("sender_id")
    String sender_id;
    @SerializedName("receiver_id")
    String receiver_id;
    @SerializedName("msg")
    String msg;
    @SerializedName("room_id")
    String room_id;

    public MessageDTO(String flag, String sender_id, String receiver_id, String msg, String room_id) {
        this.flag = flag;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.msg = msg;
        this.room_id = room_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
}
