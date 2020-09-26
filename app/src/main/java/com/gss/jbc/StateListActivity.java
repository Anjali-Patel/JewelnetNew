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

import com.gss.jbc.Adapter.CountryAdapter;
import com.gss.jbc.Adapter.StateAdapter;
import com.gss.jbc.AsynkTask.CountryListAsynktask;
import com.gss.jbc.AsynkTask.StateListAsyncTask;
import com.gss.jbc.Model.CityModel;
import com.gss.jbc.Model.CountryModel;
import com.gss.jbc.Model.StateModel;

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

public class StateListActivity extends AppCompatActivity {

    private ArrayList<StateModel> arrayList = new ArrayList<StateModel>();
    private ArrayList<StateModel> arrayListTemp = new ArrayList<StateModel>();
    JSONObject json;
    ListView stateListview;
    StateAdapter adapter;
    EditText searchflags;
    ProgressBar progess_load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_list);
        progess_load = findViewById(R.id.progess_load);

        stateListview = findViewById(R.id.stateListview);
        View view;
        searchflags = (EditText)findViewById(R.id.searchflags);
        GetAllStates();
        adapter = new StateAdapter(getApplicationContext(), arrayList);

        stateListview.setAdapter(adapter);


        stateListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrayListTemp.isEmpty()) {
                    String statename = arrayList.get(position).getState_name();
                    Intent intent = new Intent(StateListActivity.this, SearchByStateDirectory.class);
                    intent.putExtra("statename", statename);
                    startActivity(intent);
                    finish();
                }
                else {
                    String statename = arrayListTemp.get(position).getState_name().trim();
                    Intent intent = new Intent(StateListActivity.this, SearchByStateDirectory.class);
                    intent.putExtra("statename", statename);
                    startActivity(intent);
                    finish();
                }
            }
        });
        searchflags.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    arrayListTemp.clear();
                    for(int i = 0; i<arrayList.size();i++){
                        if(arrayList.get(i).getState_name().toLowerCase().contains(s.toString().toLowerCase())){
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                    adapter = new StateAdapter(getApplicationContext(),arrayListTemp);
                    stateListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    adapter = new StateAdapter(getApplicationContext(),arrayList );
                    stateListview.setAdapter(adapter);
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
                        if(arrayList.get(i).getState_name().toLowerCase().contains(s.toString().toLowerCase())){
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                    adapter = new StateAdapter(getApplicationContext(),arrayListTemp);
                    stateListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    adapter = new StateAdapter(getApplicationContext(),arrayList);
                    stateListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }


    private void GetAllStates() {

        progess_load.setVisibility(View.VISIBLE);

        arrayList.clear();
        String finalUrl = "http://jewelnet.in/index.php/Api/list_states";

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
                                    StateModel model = new StateModel();
                                    model.setState_name(newJson.getString("state"));
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
