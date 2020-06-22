package com.example.mibungi.dto;

import com.google.gson.annotations.SerializedName;

public class FriendDTO {

    @SerializedName("mem_id")
    String mem_id;
    @SerializedName("friend_id")
    String friend_id;

    public FriendDTO(String mem_id, String friend_id) {
        this.mem_id = mem_id;
        this.friend_id = friend_id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }
}
