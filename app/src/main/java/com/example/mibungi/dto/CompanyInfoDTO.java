package com.example.mibungi.dto;

public class CompanyInfoDTO {

    String ticker;
    String company_name;
    String sector;
    String industry;
    String location;
    String added_day;
    String cik;
    String founded;
    String book_mark;

    public CompanyInfoDTO(String ticker, String company_name, String sector, String industry, String location, String added_day, String cik, String founded, String book_mark) {
        this.ticker = ticker;
        this.company_name = company_name;
        this.sector = sector;
        this.industry = industry;
        this.location = location;
        this.added_day = added_day;
        this.cik = cik;
        this.founded = founded;
        this.book_mark = book_mark;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getSector() {
        return sector;
    }

    public String getIndustry() {
        return industry;
    }

    public String getLocation() {
        return location;
    }

    public String getAdded_day() {
        return added_day;
    }

    public String getCik() {
        return cik;
    }

    public String getFounded() {
        return founded;
    }

    public String getBook_mark() {
        return book_mark;
    }

    public void setBook_mark(String book_mark) {
        this.book_mark = book_mark;
    }
}
