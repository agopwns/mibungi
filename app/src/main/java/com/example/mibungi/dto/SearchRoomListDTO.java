package com.example.mibungi.dto;

import com.google.gson.annotations.SerializedName;

public class SearchRoomListDTO {

    @SerializedName("room_id")
    String room_id;

    public SearchRoomListDTO(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
}
