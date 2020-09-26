package com.gss.jbc;

import android.os.Parcel;
import android.os.Parcelable;

/*
* Data model class for Info Directory object we get from json response
*
* */
public class InfoDirModel implements Parcelable{
    private String strName;
    private String strDesig;
    private String strCompName;
    private String strCompAddr;
    private String strType;
    private String strSubtype;
    private String strallied;
    private String strLandline;
    private String strWebsite;


    public String getStrallied() {
        return strallied;
    }

    public void setStrallied(String strallied) {
        this.strallied = strallied;
    }

    private String strMobile;
    private String strEmail;
    private String strCity, strState, strCountry;
    private String imgProfileStr;
    private String imagebaseurl,strId;

    public String getImagebaseurl() {
        return imagebaseurl;
    }

    public void setImagebaseurl(String imagebaseurl) {
        this.imagebaseurl = imagebaseurl;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    //default constructor
    public InfoDirModel(){


    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrDesig() {
        return strDesig;
    }

    public void setStrDesig(String strDesig) {
        this.strDesig = strDesig;
    }

    public String getStrCompName() {
        return strCompName;
    }

    public void setStrCompName(String strCompName) {
        this.strCompName = strCompName;
    }

    public String getStrCompAddr() {
        return strCompAddr;
    }

    public void setStrCompAddr(String strCompAddr) {
        this.strCompAddr = strCompAddr;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getStrSubtype() {
        return strSubtype;
    }

    public void setStrSubtype(String strSubtype) {
        this.strSubtype = strSubtype;
    }

    public String getStrMobile() {
        return strMobile;
    }

    public void setStrMobile(String strMobile) {
        this.strMobile = strMobile;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrCity() {
        return strCity;
    }

    public void setStrCity(String strCity) {
        this.strCity = strCity;
    }

    public String getStrState() {
        return strState;
    }

    public void setStrState(String strState) {
        this.strState = strState;
    }

    public String getStrCountry() {
        return strCountry;
    }

    public void setStrCountry(String strCountry) {
        this.strCountry = strCountry;
    }

    public String getImgProfileStr() {
        return imgProfileStr;
    }

    public void setImgProfileStr(String imgProfileStr) {
        this.imgProfileStr = imgProfileStr;
    }


    public String getLandlineStr() {
        return strLandline;
    }

    public void setLandlineStr(String landline) {
        this.strLandline = landline;
    }

    public String getWebsiteStr() {
        return strWebsite;
    }

    public void setWebsiteStr(String website) {
        this.strWebsite = website;
    }
    //Implementation of Parcelable

    private InfoDirModel(Parcel in) {
        strName = in.readString();
        strDesig = in.readString();
        strCompName = in.readString();
        strCompAddr = in.readString();
        strEmail = in.readString();
        strMobile = in.readString();
        strType = in.readString();
        strSubtype = in.readString();
        strCity = in.readString();
        strState = in.readString();
        strCountry = in.readString();
        imgProfileStr = in.readString();
        strId = in.readString();
        imagebaseurl = in.readString();
        strallied = in.readString();
        strLandline = in.readString();
        strWebsite = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(strName);
        dest.writeString(strDesig);
        dest.writeString(strCompName);
        dest.writeString(strCompAddr);
        dest.writeString(strEmail);
        dest.writeString(strMobile);
        dest.writeString(strType);
        dest.writeString(strSubtype);
        dest.writeString(strCity);
        dest.writeString(strState);
        dest.writeString(strCountry);
        dest.writeString(imgProfileStr);
        dest.writeString(strId);
        dest.writeString(imagebaseurl);
        dest.writeString(strallied);
        dest.writeString(strLandline);
        dest.writeString(strWebsite);

    }

    public static final Creator<InfoDirModel> CREATOR
            = new Creator<InfoDirModel>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public InfoDirModel createFromParcel(Parcel in) {
            return new InfoDirModel(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public InfoDirModel[] newArray(int size) {
            return new InfoDirModel[size];
        }
    };


}
