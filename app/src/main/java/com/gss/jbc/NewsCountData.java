package com.gss.jbc;


import org.json.JSONObject;

public class NewsCountData {

    private String ViewCount="";

    NewsCountData(String newsviewcount){
        try{
            setCount(newsviewcount);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void setCount(String views_count) {
        this.ViewCount = views_count;
    }
    public String getCount() {
        return ViewCount;
    }

}
