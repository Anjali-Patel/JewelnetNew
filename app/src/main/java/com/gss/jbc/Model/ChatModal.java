package com.gss.jbc.Model;

public class ChatModal {
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    String baseUrl;
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    String fname, msg, profile_picture;

    public String getMsg() {
        return msg;
    }

    public String getProfile_picture(String profile_picture) {
        return this.profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
