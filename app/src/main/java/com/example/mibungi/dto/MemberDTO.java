package com.example.mibungi.dto;

import com.google.gson.annotations.SerializedName;

public class MemberDTO {

    @SerializedName("mem_id")
    String id;
    @SerializedName("password")
    String password;

    public MemberDTO(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
