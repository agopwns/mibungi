package com.example.mibungi.dto;

import com.google.gson.annotations.SerializedName;

public class SpecificInfoDTO {

    //region InitBind
    @SerializedName("BookSh")
    String BookSh;
    @SerializedName("CashSh")
    String CashSh;
    @SerializedName("Change")
    String Change;
    @SerializedName("Dividend")
    String Dividend;
    @SerializedName("DividendPer")
    String DividendPer;
    @SerializedName("Income")
    String Income;
    @SerializedName("Market_Cap")
    String Market_Cap;
    @SerializedName("PB")
    String PB;
    @SerializedName("PC")
    String PC;
    @SerializedName("PE")
    String PE;
    @SerializedName("PEG")
    String PEG;
    @SerializedName("PS")
    String PS;
    @SerializedName("Price")
    String Price;
    @SerializedName("ROA")
    String ROA;
    @SerializedName("ROE")
    String ROE;
    @SerializedName("ROI")
    String ROI;
    @SerializedName("Sales")
    String Sales;
    @SerializedName("Target_Price")
    String Target_Price;
    //endregion

    public SpecificInfoDTO(String bookSh, String cashSh, String change, String dividend, String dividendPer,
                           String income, String market_Cap, String PB, String PC, String PE, String PEG,
                           String PS, String price, String ROA, String ROE, String ROI, String sales, String target_Price) {
        this.BookSh = bookSh;
        this.CashSh = cashSh;
        this.Change = change;
        this.Dividend = dividend;
        this.DividendPer = dividendPer;
        this.Income = income;
        this.Market_Cap = market_Cap;
        this.PB = PB;
        this.PC = PC;
        this.PE = PE;
        this.PEG = PEG;
        this.PS = PS;
        this.Price = price;
        this.ROA = ROA;
        this.ROE = ROE;
        this.ROI = ROI;
        this.Sales = sales;
        this.Target_Price = target_Price;
    }

    public String getBookSh() {
        return BookSh;
    }

    public String getCashSh() {
        return CashSh;
    }

    public String getChange() {
        return Change;
    }

    public String getDividend() {
        return Dividend;
    }

    public String getDividendPer() {
        return DividendPer;
    }

    public String getIncome() {
        return Income;
    }

    public String getMarket_Cap() {
        return Market_Cap;
    }

    public String getPB() {
        return PB;
    }

    public String getPC() {
        return PC;
    }

    public String getPE() {
        return PE;
    }

    public String getPEG() {
        return PEG;
    }

    public String getPS() {
        return PS;
    }

    public String getPrice() {
        return Price;
    }

    public String getROA() {
        return ROA;
    }

    public String getROE() {
        return ROE;
    }

    public String getROI() {
        return ROI;
    }

    public String getSales() {
        return Sales;
    }

    public String getTarget_Price() {
        return Target_Price;
    }
}
