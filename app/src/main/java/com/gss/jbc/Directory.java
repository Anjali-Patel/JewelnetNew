package com.gss.jbc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Directory extends AppCompatActivity {

    RecyclerView displayRecyclerView;
    DisplayData adapter;
    ArrayList<InfoDirModel> infoDirModelList = new ArrayList<InfoDirModel>();

    ProgressBar progess_load;
    JSONObject json;
    JSONArray json2;
    LinearLayoutManager linearLayoutManager;
    String selectedType, selectedSubType, userid;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView title;
    String statename = "0";
    String cityname = "0";
    String username = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        progess_load = findViewById(R.id.progess_load);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        selectedType = this.getIntent().getExtras().getString("type");
        selectedSubType = getIntent().getExtras().getString("subtype");

        title = findViewById(R.id.toolbar_title1);
        title.setText(selectedType);
        userid = sharedPreferences.getString("userid", "");
        displayRecyclerView = findViewById(R.id.display);

        adapter = new DisplayData(Directory.this,getApplicationContext(), infoDirModelList, statename, cityname, username);
        linearLayoutManager = new LinearLayoutManager(Directory.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        displayRecyclerView.setLayoutManager(linearLayoutManager);
        displayRecyclerView.setAdapter(adapter);


        GetAllUsers();


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

        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(Directory.this, R.style.myDialog)).create();
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


    private void GetAllUsers() {

        progess_load.setVisibility(View.VISIBLE);

        infoDirModelList.clear();

        String typeInfo = "{\"type\":\""+selectedType+"\",\"sub_type\":\""+selectedSubType+"\"}";
        String baseUrl = Constant.BASE_URL+"getAllDirectoryUser_type?data=";

        String typeInfoEncoded = null;
        try {
            typeInfoEncoded = URLEncoder.encode(typeInfo, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String finalUrl = baseUrl.concat(typeInfoEncoded);

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
                            json2 = json.getJSONArray("data");

                            for (int i = 0; i < json2.length(); i++) {
                                JSONObject jsonObj = json2.getJSONObject(i);

                                InfoDirModel idModelObj = new InfoDirModel();
                                idModelObj.setImgProfileStr(jsonObj.getString("profile_picture"));
                                idModelObj.setImagebaseurl(json.getString("img"));
                                idModelObj.setStrId(jsonObj.getString("id"));
                                idModelObj.setStrName(jsonObj.getString("fname"));
                                idModelObj.setStrDesig(jsonObj.getString("designation"));
                                idModelObj.setStrCompName(jsonObj.getString("company_name"));
                                idModelObj.setStrCompAddr(jsonObj.getString("company_address"));
                                idModelObj.setStrEmail(jsonObj.getString("email"));
                                idModelObj.setStrMobile(jsonObj.getString("mobile_phone"));
                                idModelObj.setStrType(selectedType);
                                idModelObj.setStrSubtype(selectedSubType);
                                idModelObj.setStrCity(jsonObj.getString("location"));
                                idModelObj.setStrState(jsonObj.getString("state"));
                                idModelObj.setStrCountry(jsonObj.getString("country"));

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


                                idModelObj.setStrallied(jsonObj.getString("allied_user"));
                                infoDirModelList.add(idModelObj);
                            }

                            progess_load.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();

                            if (json2.length() == 0)
                            {
                                progess_load.setVisibility(View.GONE);
                                showAlertDialog (getApplicationContext());
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        //progess_load.setVisibility(View.GONE);

    }

}
