package com.example.mibungi.dto;

public class MarketPriceDTO {

    String marketName;
    String lastPrice;
    String changePrice;
    String changePercent;
    String time;

    public MarketPriceDTO(String marketName, String lastPrice, String changePrice, String changePercent, String time) {
        this.marketName = marketName;
        this.lastPrice = lastPrice;
        this.changePrice = changePrice;
        this.changePercent = changePercent;
        this.time = time;
    }

    public String getMarketName() {
        return marketName;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public String getChangePrice() {
        return changePrice;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public String getTime() {
        return time;
    }

}
