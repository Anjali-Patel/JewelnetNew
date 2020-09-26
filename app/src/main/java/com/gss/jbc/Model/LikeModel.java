package com.gss.jbc.Model;

public class LikeModel {
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    String fname;

    public String getCompany_name() {
        return company_name;
    }

    public String getNewsIdNumber() {
        return NewsIdNumber;
    }

    public void setNewsIdNumber(String newsIdNumber) {
        NewsIdNumber = newsIdNumber;
    }



    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    String company_name;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String userid;
    String NewsIdNumber;
}