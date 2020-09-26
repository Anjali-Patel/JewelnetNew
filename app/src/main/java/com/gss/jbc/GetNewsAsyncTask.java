package com.gss.jbc;

import android.os.AsyncTask;
import android.util.Log;
import com.gss.jbc.Extra.RestJsonClient;
import org.json.JSONObject;

class GetNewsAsyncTask extends AsyncTask <String,String,JSONObject>{

private JSONObject jsonNews;
@Override
protected JSONObject doInBackground(String... urlInfo) {
    try
    {
/*      String urlString = Resources.getSystem().getString(R.string.newsapi)+urlInfo[0]+Resources.getSystem().getString(R.string.newsapibracket);;
        Log.d("URL",urlString);
        jsonNews = RestJsonClient.connect(urlString);
*/
        String urlString = Constant.BASE_URL+"getnews?data=" +urlInfo[0];
        jsonNews = RestJsonClient.connect(urlString);
        Log.e("qwererty",jsonNews.toString());
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    return  jsonNews;
  }
}
