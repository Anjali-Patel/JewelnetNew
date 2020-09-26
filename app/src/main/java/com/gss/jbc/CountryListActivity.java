package com.gss.jbc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.gss.jbc.Adapter.CountryAdapter;
import com.gss.jbc.AsynkTask.CountryListAsynktask;
import com.gss.jbc.Model.CountryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CountryListActivity extends AppCompatActivity {
    private ArrayList<CountryModel> arrayList = new ArrayList<CountryModel>();
    private ArrayList<CountryModel> arrayListTemp = new ArrayList<CountryModel>();
    JSONObject json;
    ListView countryListview;
    CountryAdapter adapter;
    EditText searchflags;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_country_list);
        setContentView(R.layout.layout_countrylist);
        countryListview = findViewById(R.id.countryListview);
        View view;
        searchflags = (EditText)findViewById(R.id.searchflags);
        loadCountry();
        adapter = new CountryAdapter(getApplicationContext(), arrayList);

        countryListview.setAdapter(adapter);

        countryListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrayListTemp.isEmpty()) {
                    String flag = arrayList.get(position).getFlag();
                    String mobilecode = arrayList.get(position).getMobile_code();
                    String country_name=arrayList.get(position).getCountry_name();
                    Intent intent = new Intent();
                    intent.putExtra("flag", flag);
                    intent.putExtra("mobilecode", mobilecode);
                    intent.putExtra("cname",country_name);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    String flag = arrayListTemp.get(position).getFlag();
                    String mobilecode = arrayListTemp.get(position).getMobile_code();
                    Intent intent = new Intent();
                    intent.putExtra("flag", flag);
                    intent.putExtra("mobilecode", mobilecode);
                    String country_name=arrayList.get(position).getCountry_name();
                    intent.putExtra("cname",country_name);
                    setResult(RESULT_OK, intent);
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
                        if(arrayList.get(i).getCountry_name().toLowerCase().contains(s.toString().toLowerCase())){
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                    adapter = new CountryAdapter(getApplicationContext(),arrayListTemp);
                    countryListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    adapter = new CountryAdapter(getApplicationContext(),arrayList );
                    countryListview.setAdapter(adapter);
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
                        if(arrayList.get(i).getCountry_name().toLowerCase().contains(s.toString().toLowerCase())){
                            arrayListTemp.add(arrayList.get(i));
                        }
                    }
                    adapter = new CountryAdapter(getApplicationContext(),arrayListTemp);
                    countryListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else{
                    adapter = new CountryAdapter(getApplicationContext(),arrayList);
                    countryListview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void loadCountry() {
        try {
            arrayList.clear();
            json = new CountryListAsynktask().execute().get();
            int success = json.getInt("status");
            if (success == 1) {
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newJson = jsonArray.getJSONObject(i);
                    CountryModel model = new CountryModel();
                    model.setMobile_code(newJson.getString("mob_code"));
                    model.setCountry_name(newJson.getString("name"));
                    model.setFlag(newJson.getString("image"));
                    arrayList.add(model);
                }
            }

            Log.d("finished", arrayList.size()+"");

            //adapter.notifyDataSetChanged();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
