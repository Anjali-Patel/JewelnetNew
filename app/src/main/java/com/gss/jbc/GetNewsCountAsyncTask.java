package com.gss.jbc;

import android.os.AsyncTask;
import android.util.Log;
import com.gss.jbc.Extra.RestJsonClient;
import org.json.JSONObject;

public class GetNewsCountAsyncTask extends AsyncTask <String,String,JSONObject> {
    private JSONObject jsonCount;
    @Override
    protected JSONObject doInBackground(String... urlInfo) {
        try
        {

            String urlString = "http://jewelnet.in/index.php/Api/add_views?user_id=735&news_id=2554";
            jsonCount = RestJsonClient.connect(urlString);
            //Log.e("qwererty",jsonNews.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  jsonCount;
    }
}
