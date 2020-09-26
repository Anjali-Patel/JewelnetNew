package com.gss.jbc.AsynkTask;

import android.os.AsyncTask;

import com.gss.jbc.Constant;
import com.gss.jbc.Extra.RestJsonClient;

import org.json.JSONObject;

import java.net.URLEncoder;

public class CountryListAsynktask extends AsyncTask<String, String, JSONObject> {
    private JSONObject json;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... url) {

        try {
            String finalUrl = Constant.COUNTRY_URL;
            json = RestJsonClient.connect(finalUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}
