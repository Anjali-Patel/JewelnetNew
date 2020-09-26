package com.gss.jbc;

import android.os.AsyncTask;

import com.gss.jbc.Extra.RestJsonClient;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.URLEncoder;


public class GetDirectoryInfoAsyncTask extends AsyncTask<String, String, JSONObject> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... url) {
        JSONObject object = new JSONObject();
        try {
            String baseUrl = url[0];
            String typeInfo =  URLEncoder.encode(url[1], "UTF-8");
            String finalUrl = baseUrl.concat(typeInfo);

            object = RestJsonClient.connect(finalUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}
