package com.gss.jbc.AsynkTask;

import android.os.AsyncTask;

import com.gss.jbc.Extra.RestJsonClient;

import org.json.JSONObject;

public class NameListAsyncTask extends AsyncTask<String, String, JSONObject> {

    private JSONObject json;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... url) {

        try {
            String finalUrl = "http://jewelnet.in/index.php/Api/all_users";
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
