package com.worldbankdata.model;


import javax.persistence.*;

@Entity
public class Country {
    @Id
    @Column(length = 3)
    private String code;
    @Column(length = 32)
    private String name;
    @Column(columnDefinition = "Decimal(11,8)")
    private Double internetUsers;
    @Column(columnDefinition = "Decimal(11,8)")
    private Double adultLiteracyRate;

    public Country() {}

    public Country (CountryBuilder countryBuilder){
        this.code = countryBuilder.code;
        this.name = countryBuilder.name;
        this.internetUsers = countryBuilder.internetUsers;
        this.adultLiteracyRate = countryBuilder.adultLiteracyRate;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", internetUsers=" + internetUsers +
                ", adultLiteracyRate=" + adultLiteracyRate +
                '}';
    }

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

    public Double getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(Double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Double getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(Double adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    public static class CountryBuilder{
        private String code;
        private String name;
        private Double internetUsers;
        private Double adultLiteracyRate;

        public CountryBuilder (String code, String name){
            this.code = code;
            this.name = name;
        }
        public CountryBuilder withInternetUsers(Double users){
            this.internetUsers = users;
            return this;
        }
        public CountryBuilder withAdultLiteracyRate(Double literacyRate){
            this.adultLiteracyRate = literacyRate;
            return this;
        }
        public Country build(){
            return new Country(this);
        }
    }
}