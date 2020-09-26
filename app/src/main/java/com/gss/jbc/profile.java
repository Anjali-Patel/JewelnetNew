
package com.gss.jbc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gss.jbc.Extra.RestJsonClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;


public class profile extends AppCompatActivity {

    String user_id;
    int position;
    JSONObject json;
    JSONArray json2;
    RoundedImageView profile;
    DrawerLayout drawer;
    ImageView post;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userid;
    TextView name, mobile, email, loc, state, country, dob,
            comp_name, comp_add, nat_bus, sub_nat_bus, designation, geolocation;
    String URL = "";
    String CompanyAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user_id = getIntent().getStringExtra("user_id");
        position = getIntent().getIntExtra("position", 0);
        String URL = Constant.BASE_URL + "getuserdetail?userid=" + user_id;
        Log.e("PROFILE_URL", URL);

//        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
//        userid = sharedPreferences.getString("userid", "");

        profile = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
//        mobile = findViewById(R.id.mobile);
        loc = findViewById(R.id.location);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        post = findViewById(R.id.share);
        dob = findViewById(R.id.dob);
        comp_name = findViewById(R.id.comp_name);
        comp_add = findViewById(R.id.comp_add);
        designation = findViewById(R.id.designation);
        nat_bus = findViewById(R.id.nat_bus);
        sub_nat_bus = findViewById(R.id.sub_nat_bus);
        geolocation = findViewById(R.id.geolocation);

        if (!URL.equalsIgnoreCase(""))
            try {
                json = new api().execute(URL).get();

//            new DownloadImageTask(profile).execute(json.getString("profile_picture"));

                Log.e("ProfileImage", json.getString("profile_picture"));
                if (!json.getString("profile_picture").equalsIgnoreCase("")) {
                    Picasso.with(getApplicationContext()).load(json.getString("profile_picture")).placeholder(R.drawable.user)
                            .error(R.drawable.user).resize(250, 250)
                            .skipMemoryCache().into(profile);
                } else {
                    Picasso.with(getApplicationContext()).load(R.drawable.user).placeholder(R.drawable.user)
                            .error(R.drawable.user).resize(250, 250)
                            .skipMemoryCache().into(profile);
                }
                Log.d("data", json.toString());
/*
            String form = getString(R.string.name, json.getString("fname"));
            name.setText(": " + Html.fromHtml(form != null ? form : ""));
            form = getString(R.string.mobileno, json.getString("mobile_phone"));
            mobile.setText(": " + Html.fromHtml(form));
            form = getString(R.string.email, json.getString("email"));
            email.setText(": " + Html.fromHtml(form));
            form = getString(R.string.comp_name, json.getString("company_name"));
            comp_name.setText(": " + Html.fromHtml(form));
            form = getString(R.string.state, json.getString("state"));
            state.setText(": " + Html.fromHtml(form));
            form = getString(R.string.comp_add, json.getString("company_address"));
            comp_add.setText(": " + Html.fromHtml(form));
            form = getString(R.string.nat_bus, json.getString("nature_of_business"));
            nat_bus.setText(": " + Html.fromHtml(form));
            form = getString(R.string.location, json.getString("location"));
            loc.setText(": " + Html.fromHtml(form));*/
                String desig = json.getString("designation");
                name.setText(": " + json.getString("fname"));
//            mobile.setText(": " + json.getString("mobile_phone"));
                email.setText(": " + json.getString("email"));
                comp_name.setText(": " + json.getString("company_name"));
                state.setText(": " + json.getString("state"));
                comp_add.setText(": " + json.getString("company_address"));
                nat_bus.setText(": " + json.getString("nature_of_business"));
                loc.setText(": " + json.getString("location"));
                designation.setText(": " + desig);

                if (json.getString("nature_of_business").equalsIgnoreCase("ALLIED")) {
                    sub_nat_bus.setText(": " + json.getString("sub_nature") +
                            "(" + json.getString("allied_user") + ")");
                } else {
                    sub_nat_bus.setText(": " + json.getString("sub_nature"));
                }
                country.setText(": " + json.getString("country"));

            } catch (Exception e) {
                e.printStackTrace();
            }

        try {
            CompanyAddr = json.getString("company_address").replace("&","and");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        geolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+CompanyAddr));
                startActivity(intent);
            }
        });


    }


    private class api extends AsyncTask<String, String, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... url) {
            try {
                json = RestJsonClient.connect(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject params) {
            super.onPostExecute(params);
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                profile.setImageBitmap(mIcon11);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
