package com.gss.jbc;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gss.jbc.Adapter.DisplayDataUsersAdapter;
import com.gss.jbc.Extra.RestJsonClient;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SearchchBycityDirectory extends AppCompatActivity {
    RecyclerView displayRecyclerView;
    DisplayDataUsersAdapter adapter;
    ArrayList<InfoDirModel> infoDirModelList;

    ProgressBar progess_load;

    JSONObject json;
    JSONArray json2;
    LinearLayoutManager linearLayoutManager;
    String selectedType, selectedSubType, userid;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView title;
    String cityname;
    String statename = "0";
    String username ="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        progess_load = findViewById(R.id.progess_load);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cityname = (getIntent().getStringExtra("cityname"));


        infoDirModelList = new ArrayList<InfoDirModel>();
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        selectedType = this.getIntent().getExtras().getString("type");
        selectedSubType = getIntent().getExtras().getString("subtype");

        title = findViewById(R.id.toolbar_title1);
        title.setText(selectedType);
        userid = sharedPreferences.getString("userid", "");
        displayRecyclerView = findViewById(R.id.display);

        GetUserData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();        //To handle navigation properly in types -subtypes screen
                // as both activity can navigate to Directory.java
                return true;
            case R.id.back:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showAlertDialog(Context context){

        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(SearchchBycityDirectory.this, R.style.myDialog)).create();
        alertDialog.setTitle("JewelNet");
        alertDialog.setMessage("No result found...");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }



    private void GetUserData() {

        progess_load.setVisibility(View.VISIBLE);

        infoDirModelList.clear();
        String url = "http://jewelnet.in/index.php/Api/search_by_key?search_by=city&key="+cityname;


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url).get().build();
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

                            if (json == null)
                            {
                                progess_load.setVisibility(View.GONE);

                                showAlertDialog (getApplicationContext());
                            }

                            json2 = json.getJSONArray("data");
                            for (int i = 0; i < json2.length(); i++) {
                                JSONObject jsonObj = json2.getJSONObject(i);

                                InfoDirModel idModelObj = new InfoDirModel();
                                idModelObj.setImgProfileStr(jsonObj.getString("profile_picture"));
                                idModelObj.setImagebaseurl("http://104.236.14.235/assets/upload/users");
                                idModelObj.setStrId(jsonObj.getString("id"));
                                idModelObj.setStrName(jsonObj.getString("fname"));
                                idModelObj.setStrDesig(jsonObj.getString("designation"));
                                idModelObj.setStrCompName(jsonObj.getString("company_name"));
                                idModelObj.setStrCompAddr(jsonObj.getString("company_address"));
                                idModelObj.setStrEmail(jsonObj.getString("email"));
                                idModelObj.setStrMobile(jsonObj.getString("mobile_phone"));
                                idModelObj.setStrType(jsonObj.getString("nature_of_business"));
                                idModelObj.setStrSubtype(jsonObj.getString("sub_nature"));
                                idModelObj.setStrCity(jsonObj.getString("location"));
                                idModelObj.setStrState(jsonObj.getString("state"));
                                idModelObj.setStrCountry(jsonObj.getString("country"));
                                idModelObj.setStrallied(jsonObj.getString("allied_user"));

                                if (jsonObj.getString("website") == null || jsonObj.getString("website").equalsIgnoreCase("null") || jsonObj.getString("website").equalsIgnoreCase("")){
                                    idModelObj.setWebsiteStr(" ");
                                }
                                else{
                                    idModelObj.setWebsiteStr(jsonObj.getString("website"));
                                }

                                if (jsonObj.getString("landline_no") == null || jsonObj.getString("landline_no").equalsIgnoreCase("null") || jsonObj.getString("landline_no").equalsIgnoreCase("")){
                                    idModelObj.setLandlineStr(" ");
                                }
                                else{
                                    idModelObj.setLandlineStr(jsonObj.getString("landline_no"));
                                }

                                infoDirModelList.add(idModelObj);
                            }

                            adapter = new DisplayDataUsersAdapter(SearchchBycityDirectory.this,getApplicationContext(), infoDirModelList, statename, cityname, username);
                            linearLayoutManager = new LinearLayoutManager(SearchchBycityDirectory.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            displayRecyclerView.setLayoutManager(linearLayoutManager);


                            progess_load.setVisibility(View.GONE);
                            displayRecyclerView.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (json2.length() == 0)
                        {         progess_load.setVisibility(View.GONE);
                            showAlertDialog (getApplicationContext()); }
                    }
                });
            }
        });


    }
}
