package com.example.mibungi.dto;

public class NewsDTO {

    String title;
    String url;
    String image;
    String content;
    String time;

    public NewsDTO(String title, String url, String image, String content, String time) {
        this.title = title;
        this.url = url;
        this.image = image;
        this.content = content;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
