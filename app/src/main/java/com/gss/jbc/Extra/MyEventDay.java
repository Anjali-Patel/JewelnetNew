package com.gss.jbc.Extra;

import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;

public class MyEventDay extends EventDay implements Parcelable {
    private String mNote;
    private String mDesc;
    private String mWebsite;
    private String mEventLogo;


    public MyEventDay(Calendar day, int imageResource, String note, String Description, String WebLink, String EventLogo) {
        super(day, imageResource);
        mNote = note;
        mDesc = Description;
        mWebsite = WebLink;
        mEventLogo = EventLogo;
    }

    public String getNote() {
        return mNote;
    }

    public String getDescription() {
        return mDesc;
    }

    public String getWebLink() {
        return mWebsite;
    }

    public String getEventLogo() {
        return mEventLogo;
    }



    private MyEventDay(Parcel in) {
        super((Calendar) in.readSerializable(), in.readInt());
        mNote = in.readString();
        mDesc = in.readString();
        mWebsite = in.readString();
        mEventLogo  = in.readString();
    }

    public static final Creator<MyEventDay> CREATOR = new Creator<MyEventDay>() {
        @Override
        public MyEventDay createFromParcel(Parcel in) {
            return new MyEventDay(in);
        }

        @Override
        public MyEventDay[] newArray(int size) {
            return new MyEventDay[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(getCalendar());
        parcel.writeInt(getImageResource());
        parcel.writeString(mNote);
        parcel.writeString(mDesc);
        parcel.writeString(mWebsite);
        parcel.writeString(mEventLogo);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}