package com.workbankdata.model;


public class Country {
    private String code;
    private String name;
    private int internetUsers;
    private int adultLiteracyRate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(int internetUsers) {
        this.internetUsers = internetUsers;
    }

    public int getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(int adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }
}
