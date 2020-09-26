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

import com.gss.jbc.Adapter.NameAdapter;
import com.gss.jbc.Adapter.StateAdapter;
import com.gss.jbc.AsynkTask.NameListAsyncTask;
import com.gss.jbc.AsynkTask.StateListAsyncTask;
import com.gss.jbc.Model.StateModel;
import com.gss.jbc.Model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UserList extends AppCompatActivity {

    private ArrayList<UserModel> arrayList = new ArrayList<UserModel>();
    private ArrayList<UserModel> arrayListTemp = new ArrayList<UserModel>();
    JSONObject json;
    ListView userListview;
    NameAdapter adapter;
    EditText searchflags;

    ProgressBar progess_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        progess_load = findViewById(R.id.progess_load);

        userListview = findViewById(R.id.userListview);
        View view;
        searchflags = (EditText)findViewById(R.id.searchflags);
        GetAllUsers();
        adapter = new NameAdapter(getApplicationContext(), arrayList);

        userListview.setAdapter(adapter);


        userListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrayListTemp.isEmpty()) {
                    String username = arrayList.get(position).getUser_name();
                    Intent intent = new Intent(UserList.this, SearchByNameDirectory.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
                else {
                    String username = arrayListTemp.get(position).getUser_name();
                     Intent intent = new Intent(UserList.this, SearchByNameDirectory.class);
                    intent.putExtra("username", username);
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
                        if(arrayList.get(i).getUser_name().toLowerCase().contains(s.toString().toLowerCase())){
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                    adapter = new NameAdapter(getApplicationContext(),arrayListTemp);
                    userListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    adapter = new NameAdapter(getApplicationContext(),arrayList );
                    userListview.setAdapter(adapter);
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
                        if(arrayList.get(i).getUser_name().toLowerCase().contains(s.toString().toLowerCase())){
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                    adapter = new NameAdapter(getApplicationContext(),arrayListTemp);
                    userListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    adapter = new NameAdapter(getApplicationContext(),arrayList);
                    userListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }





    private void GetAllUsers() {

        progess_load.setVisibility(View.VISIBLE);

        arrayList.clear();
        String finalUrl = "http://jewelnet.in/index.php/Api/all_users";

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
                                    UserModel model = new UserModel();
                                    model.setUser_name(newJson.getString("fname"));
                                    arrayList.add(model);
                                }
                                progess_load.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }


                            else
                            {
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
