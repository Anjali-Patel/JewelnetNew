package com.gss.jbc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.gss.jbc.Adapter.CityAdapter;
import com.gss.jbc.AsynkTask.CityNameAsyncTask;
import com.gss.jbc.AsynkTask.StateListAsyncTask;
import com.gss.jbc.Model.CityModel;
import com.gss.jbc.Model.StateModel;
import com.gss.jbc.Model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CityNameActivity extends AppCompatActivity {

    private ArrayList<CityModel> arrayList = new ArrayList<CityModel>();
    private ArrayList<CityModel> arrayListTemp = new ArrayList<CityModel>();
    JSONObject json;
    ListView cityListview;
    CityAdapter adapter;
    EditText searchflags;
    ProgressBar progess_load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_name);

        progess_load = findViewById(R.id.progess_load);

        cityListview = findViewById(R.id.cityListview);
        View view;
        searchflags = (EditText)findViewById(R.id.searchflags);
        GetAllCities();
        adapter = new CityAdapter(getApplicationContext(), arrayList);

        cityListview.setAdapter(adapter);


        cityListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrayListTemp.isEmpty()) {
                    String cityname = arrayList.get(position).getCity_name();
                    Intent intent = new Intent(CityNameActivity.this, SearchchBycityDirectory.class);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                    finish();
                }
                else {

                    String cityname = arrayListTemp.get(position).getCity_name().trim();
                    Intent intent = new Intent(getApplicationContext(), SearchchBycityDirectory.class);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                    finish();

//                    String cityname = arrayListTemp.get(position).getCity_name();
//                    Intent intent = new Intent();
//                    intent.putExtra("cityname", cityname);
//                    setResult(RESULT_OK, intent);
//                    finish();
                }
            }
        });
        searchflags.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    arrayListTemp.clear();
                    for(int i = 0; i<arrayList.size();i++){
                        if(arrayList.get(i).getCity_name().toLowerCase().contains(s.toString().toLowerCase())){
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                    adapter = new CityAdapter(getApplicationContext(),arrayListTemp);
                    cityListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    adapter = new CityAdapter(getApplicationContext(),arrayList );
                    cityListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    arrayListTemp.clear();
                    for(int i = 0; i<arrayList.size();i++){
                        if(arrayList.get(i).getCity_name().toLowerCase().contains(s.toString().toLowerCase())){
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                    adapter = new CityAdapter(getApplicationContext(),arrayListTemp);
                    cityListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    adapter = new CityAdapter(getApplicationContext(),arrayList);
                    cityListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


//    private void loadCity() {
//        try {
//            arrayList.clear();
//            json = new CityNameAsyncTask().execute().get();
//            int success = json.getInt("status");
//            if (success == 1) {
//                JSONArray jsonArray = json.getJSONArray("data");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject newJson = jsonArray.getJSONObject(i);
//                    CityModel model = new CityModel();
//                    model.setCity_name(newJson.getString("location"));
//                    arrayList.add(model);
//                }
//            }
//
//            Log.d("finished", arrayList.size()+"");
//
//            //adapter.notifyDataSetChanged();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    private void GetAllCities() {

        progess_load.setVisibility(View.VISIBLE);

        arrayList.clear();
        String finalUrl = "http://jewelnet.in/index.php/Api/list_cities";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(finalUrl).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                final String myResponse = responseBody.string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(myResponse);
                            int success = json.getInt("status");

                            if (success == 1) {
                                JSONArray jsonArray = json.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newJson = jsonArray.getJSONObject(i);
                                    CityModel model = new CityModel();
                                    model.setCity_name(newJson.getString("location"));
                                    arrayList.add(model);
                                }
                                progess_load.setVisibility(View.GONE);

                                adapter.notifyDataSetChanged();
                            }

                            else{
                                progess_load.setVisibility(View.GONE);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }
}
