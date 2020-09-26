package com.gss.jbc.Model;

public class CountryModel {
    private String Country_name;
    private String Country_code;
    private String Flag;
    private String mobile_code;


    public String getMobile_code() {
        return mobile_code;
    }

    public void setMobile_code(String mob_code) {
        this.mobile_code = mob_code;
    }


    public String getCountry_name() {
        return Country_name;
    }

    public void setCountry_name(String name) {
        Country_name = name;
    }

    public String getCountry_code() {
        return Country_code;
    }

    public void setCountry_code(String country_code) {
        Country_code = country_code;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }
}
