package com.gss.jbc;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gss.jbc.Extra.RestJsonClient;
import com.gss.jbc.Utility.CommonUtils;
import com.gss.jbc.Utility.SharedPreferenceUtils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class register extends AppCompatActivity implements LocationListener {
    ImageView profile, back;
    EditText name, mobile, email, company, companyAdd, designation, city, dob, country, state, otp;
    TextView terms;
    CheckBox termsncondi;
    Button sign;
    JSONObject json;
    Uri uri;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    StringBuilder build;
    InputStream inStream;
    List<NameValuePair> nameValuePairs;
    ArrayList<String> data;
    File file;
    String imageurl;
    private static final int READ_REQUEST_CODE = 200;
    String userid, pword;
    Spinner nature_bus;
    int select;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    ArrayList<String> arraylist;
    CheckBox checkGold, checkDiamond, checkPlatinum, checkSilver, checkGemStone, checkLooseDimond, checkLabDimond;
    String Gold = "", Diamond = "", Platinum = "", Silver = "", GemStone = "", LooseDiamond = "", LabGrownDiamonds = "";
    String SubNature;
    LinearLayout maincheckbox;
    TextView upload_ProfilePic;
    EditText allied_subnature;
    ProgressBar register_progress;
    EditText website, landline, pincode;
    Double latitude, longitude;
    Location location;
    String fetch_city, fetch_state, fetch_zip, fetch_country;
    SharedPreferenceUtils preferances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        company = findViewById(R.id.company);
        companyAdd = findViewById(R.id.companyAddress);
        designation = findViewById(R.id.user_desig);
        dob = findViewById(R.id.dob);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        otp = findViewById(R.id.otp);
        state = findViewById(R.id.state);

        website = findViewById(R.id.website);
       // website.setVisibility(View.GONE);

        landline = findViewById(R.id.landline);
        //landline.setVisibility(View.GONE);
        //landline_no
                //website_url

        pincode = findViewById(R.id.pincode);
        //pincode.setVisibility(View.GONE);

        terms = findViewById(R.id.terms);
        nature_bus = findViewById(R.id.nat_bus);
        back = findViewById(R.id.backbutton_register);
        register_progress = findViewById(R.id.register_progress);
        upload_ProfilePic = findViewById(R.id.upload_ProfilePic);
        profile = findViewById(R.id.profile);
        termsncondi = findViewById(R.id.termsncondi);
        maincheckbox = findViewById(R.id.maincheckbox);
        allied_subnature = findViewById(R.id.allied_sunature);
        checkDiamond = findViewById(R.id.checkDiamond);
        checkGold = findViewById(R.id.checkGold);
        checkPlatinum = findViewById(R.id.checkPlatinum);
        checkSilver = findViewById(R.id.checkSilver);
        checkGemStone = findViewById(R.id.checkGemStone);
        checkLooseDimond = findViewById(R.id.checkLooseDimond);
        checkLabDimond = findViewById(R.id.checkLabDiamond);

        sign = findViewById(R.id.sign);

        preferances = SharedPreferenceUtils.getInstance(this);

        pincode.setText(preferances.getStringValue(CommonUtils.PINCODE,""));
        city.setText(preferances.getStringValue(CommonUtils.CITY,""));
        country.setText(preferances.getStringValue(CommonUtils.COUNTRY,""));
        state.setText(preferances.getStringValue(CommonUtils.STATE,""));

        mobile.setText(preferances.getStringValue(CommonUtils.PHONE,""));


        if (getIntent().getStringExtra("userid") != null) {
            userid = getIntent().getStringExtra("userid");
        }
        if (getIntent().hasExtra("mobileno")) {
            mobile.setText(getIntent().getStringExtra("mobileno"));
        }
        if (getIntent().getStringExtra("pword") != null) {
            pword = getIntent().getStringExtra("pword");
        }

        arraylist = new ArrayList<>();
        arraylist.add("Select Nature of Business");
        arraylist.add("MANUFACTURER");
        arraylist.add("WHOLESALER");
        arraylist.add("RETAILER");
        arraylist.add("ALLIED");

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(register.this, terms.class);
                startActivity(i);
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
//
//        if (!getIntent().getExtras().getString("pword").equals("")) {
//            otp.setText(getIntent().getExtras().getString("pword"));
//        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(register.this, R.layout.spinner_layout, arraylist);
        nature_bus.setAdapter(adapter);



        try {
            String url = Constant.BASE_URL + "getuserdetail?userid=" + userid;
            json = new getUserdetails().execute(url).get();
            Log.e("jsonUserDetails", json.toString());
            name.setText(json.getString("fname").replace("null", ""));
            email.setText(json.getString("email").replace("null", ""));
            mobile.setText(json.getString("mobile_phone").replace("null", ""));
            company.setText(json.getString("company_name").replace("null", ""));
            companyAdd.setText(json.getString("company_address").replace("null", ""));
            designation.setText(json.getString("designation").replace("null", ""));
            landline.setText(json.getString("landline_no").replace("null", ""));
            website.setText(json.getString("website").replace("null", ""));
            //pincode.setText(json.getString("pincode").replace("null", ""));
            //state.setText(json.getString("state").replace("null", ""));

            if (pincode.getText().toString().trim() == null ||  pincode.getText().toString().trim().equals("")){
                pincode.setText(json.getString("pincode").replace("null", ""));}

            if (state.getText().toString().trim() != null ||  state.getText().toString().trim().equals("")){
            state.setText(json.getString("state").replace("null", ""));}

            if (city.getText().toString().trim()!= null ||  city.getText().toString().trim().equals("")){
            city.setText(json.getString("location").replace("null", ""));}

            if (country.getText().toString().trim() != null ||  country.getText().toString().trim().equals("")){
            country.setText(json.getString("country").replace("null", ""));}
            if (state.getText().toString().trim() == null ||  state.getText().toString().trim().equals("")){
                state.setText(json.getString("state").replace("null", ""));}

            if (city.getText().toString().trim()== null ||  city.getText().toString().trim().equals("")){
                city.setText(json.getString("location").replace("null", ""));}

            if (country.getText().toString().trim() == null ||  country.getText().toString().trim().equals("")){
                country.setText(json.getString("country").replace("null", ""));}

            if (json.getString("state") == null) {
                getLatLong();
            }

//            website.setText(json.getString("website").replace("null", ""));
//            landline.setText(json.getString("landline").replace("null", ""));


            if (!json.getString("dob").equalsIgnoreCase("00-00-0000"))
                dob.setText(json.getString("dob"));

            if (json.getString("nature_of_business").equalsIgnoreCase("manufacturer")) {
                select = adapter.getPosition("MANUFACTURER");
                nature_bus.setSelection(select, true);
            } else if (json.getString("nature_of_business").equalsIgnoreCase("wholesaler")) {
                select = adapter.getPosition("WHOLESALER");
                nature_bus.setSelection(select, true);
            } else if (json.getString("nature_of_business").equalsIgnoreCase("retailer")) {
                select = adapter.getPosition("RETAILER");
                nature_bus.setSelection(select, true);
            } else if (json.getString("nature_of_business").equalsIgnoreCase("allied")) {
                select = adapter.getPosition("ALLIED");
                nature_bus.setSelection(select, true);
                maincheckbox.setVisibility(View.GONE);
                allied_subnature.setVisibility(View.VISIBLE);
                allied_subnature.setText(json.getString("sub_nature").replace("null", ""));
            }


//            state.setText(json.getString("state").replace("null",""));
            if (!json.getString("profile_picture").isEmpty() || json.getString("profile_picture") != null) {
                Picasso.with(register.this).load(json.getString("profile_picture"))
                        .error(R.drawable.user).into(profile);
            }
            String names = json.getString("sub_nature");
            try {

                String sub1 = null, sub2 = null, sub3 = null, sub4 = null, sub5 = null, sub6 = null, sub7 = null;
                String[] namesList = names.split(",");
                if (namesList.length == 1) {
                    sub1 = namesList[0];
                    if (sub1.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub1.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub1.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub1.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub1.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub1.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub1.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);
                }
                if (namesList.length == 2) {
                    sub1 = namesList[0];
                    sub2 = namesList[1];
                    if (sub1.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub1.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub1.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub1.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub1.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub1.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub1.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub2.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub2.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub2.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub2.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub2.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub2.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub2.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);
                }
                if (namesList.length == 3) {
                    sub1 = namesList[0];
                    sub2 = namesList[1];
                    sub3 = namesList[2];
                    if (sub1.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub1.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub1.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub1.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub1.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub1.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub1.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub2.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub2.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub2.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub2.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub2.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub2.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub2.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub3.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub3.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub3.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub3.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub3.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub3.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub3.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);
                }
                if (namesList.length == 4) {
                    sub1 = namesList[0];
                    sub2 = namesList[1];
                    sub3 = namesList[2];
                    sub4 = namesList[3];
                    if (sub1.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub1.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub1.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub1.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub1.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub1.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub1.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub2.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub2.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub2.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub2.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub2.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub2.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub3.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);


                    if (sub3.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub3.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub3.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub3.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub3.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub3.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub3.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub4.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub4.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub4.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub4.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub4.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub4.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub4.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);
                }
                if (namesList.length == 5) {
                    sub1 = namesList[0];
                    sub2 = namesList[1];
                    sub3 = namesList[2];
                    sub4 = namesList[3];
                    sub5 = namesList[4];

                    if (sub1.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub1.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub1.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub1.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub1.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub1.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub1.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub2.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub2.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub2.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub2.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub2.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub2.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub2.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub3.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub3.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub3.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub3.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub3.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub3.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub3.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub4.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub4.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub4.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub4.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub4.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub4.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub4.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub5.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub5.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub5.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub5.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub5.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub5.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub5.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);
                }

                if (namesList.length == 6) {
                    sub1 = namesList[0];
                    sub2 = namesList[1];
                    sub3 = namesList[2];
                    sub4 = namesList[3];
                    sub5 = namesList[4];
                    sub6 = namesList[5];


                    if (sub1.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub1.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub1.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub1.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub1.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub1.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub1.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub2.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub2.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub2.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub2.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub2.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub2.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub2.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub3.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub3.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub3.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub3.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub3.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub3.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub3.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub4.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub4.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub4.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub4.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub4.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub4.equalsIgnoreCase("LOOSE DIAMOND"))
                        checkLooseDimond.setChecked(true);
                    if (sub4.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub5.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub5.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub5.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub5.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub5.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub5.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub5.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub6.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub6.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub6.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub6.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub6.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub6.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub6.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);
                }

                if (namesList.length == 7) {
                    sub1 = namesList[0];
                    sub2 = namesList[1];
                    sub3 = namesList[2];
                    sub4 = namesList[3];
                    sub5 = namesList[4];
                    sub6 = namesList[5];
                    sub7 = namesList[6];


                    if (sub1.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub1.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub1.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub1.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub1.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub1.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub1.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub2.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub2.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub2.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub2.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub2.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub2.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub2.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub3.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub3.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub3.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub3.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub3.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub3.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub3.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub4.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub4.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub4.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub4.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub4.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub4.equalsIgnoreCase("LOOSE DIAMOND"))
                        checkLooseDimond.setChecked(true);
                    if (sub4.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub5.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub5.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub5.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub5.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub5.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub5.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub5.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub6.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub6.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub6.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub6.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub6.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub6.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub6.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);

                    if (sub7.equalsIgnoreCase("GOLD"))
                        checkGold.setChecked(true);
                    if (sub7.equalsIgnoreCase("PLATINUM"))
                        checkPlatinum.setChecked(true);
                    if (sub7.equalsIgnoreCase("SILVER"))
                        checkSilver.setChecked(true);
                    if (sub7.equalsIgnoreCase("DIAMOND"))
                        checkDiamond.setChecked(true);
                    if (sub7.equalsIgnoreCase("GEMSTONE"))
                        checkGemStone.setChecked(true);
                    if (sub7.equalsIgnoreCase("LOOSE DIAMONDS"))
                        checkLooseDimond.setChecked(true);
                    if (sub7.equalsIgnoreCase("LAB GROWN DIAMONDS"))
                        checkLabDimond.setChecked(true);
                }

                Log.e("Hello Subnature", sub1 + " \n" + sub2 + " \n" + sub3 + " \n" + sub4 + " \n" + sub5 + " \n" + sub6);

            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("Method Halted!, continuing doing the next thing");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i, READ_REQUEST_CODE);*/
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(register.this);
            }
        });

        upload_ProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i, READ_REQUEST_CODE);*/
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(register.this);
            }
        });


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nature_bus.getSelectedItem().toString().equalsIgnoreCase("allied")) {
                    ValidationforAllied();
                } else {
                    ValidationforOthers();
                }
            }
        });
        nature_bus.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getSelectedItem().toString();

                if (selectedItem.equalsIgnoreCase("allied")) {
                    maincheckbox.setVisibility(View.GONE);
                    allied_subnature.setVisibility(View.VISIBLE);
                } else {
                    maincheckbox.setVisibility(View.VISIBLE);
                    allied_subnature.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void ValidationforAllied() {
        if (!otp.getText().toString().trim().equalsIgnoreCase(pword)) {
            Toast.makeText(register.this, "Invalid otp", Toast.LENGTH_SHORT).show();
        } else if (otp.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter otp", Toast.LENGTH_SHORT).show();
        } else if (name.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter name", Toast.LENGTH_SHORT).show();
            name.setText("");
        } else if (company.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter company name", Toast.LENGTH_SHORT).show();
            company.setText("");
        } else if (companyAdd.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter company address", Toast.LENGTH_SHORT).show();
            companyAdd.setText("");
        } else if (designation.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter designation", Toast.LENGTH_SHORT).show();
            designation.setText("");
        } else if (dob.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please select date of birth", Toast.LENGTH_SHORT).show();

        } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter email", Toast.LENGTH_SHORT).show();
            email.setText("");
        } else if (!isValidEmail(email.getText().toString().trim())) {
            Toast.makeText(register.this, "Please enter valid email", Toast.LENGTH_SHORT).show();

        } else if (city.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter location", Toast.LENGTH_SHORT).show();
            city.setText("");

        } else if (state.getText().toString().trim().equalsIgnoreCase("")) {
            state.setText("");
            Toast.makeText(register.this, "Please enter state", Toast.LENGTH_SHORT).show();

        } else if (country.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter country", Toast.LENGTH_SHORT).show();
            country.setText("");

        } else if (pincode.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter pincode", Toast.LENGTH_SHORT).show();
            pincode.setText("");

        } else if (landline.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter landline", Toast.LENGTH_SHORT).show();
            landline.setText("");

        } else if (website.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter website", Toast.LENGTH_SHORT).show();
            landline.setText("");

        } else if (nature_bus.getSelectedItem().toString().trim().equalsIgnoreCase("") ||
                nature_bus.getSelectedItem().toString().equalsIgnoreCase("Select Nature of Business")) {
            Toast.makeText(register.this, "Please select nature of business", Toast.LENGTH_SHORT).show();

        } else if (!termsncondi.isChecked()) {
            Toast.makeText(register.this, "Please Accept Terms and Conditions", Toast.LENGTH_SHORT).show();

        } else if (allied_subnature.getText().toString().trim().equalsIgnoreCase("")) {
            maincheckbox.setVisibility(View.GONE);
            allied_subnature.setVisibility(View.VISIBLE);
            Toast.makeText(register.this, "Please enter your business type", Toast.LENGTH_SHORT).show();

        } else {
            try {
                json = new api2().execute(userid, name.getText().toString(), email.getText().toString(),
                        mobile.getText().toString(), designation.getText().toString(), company.getText().toString()
                        , companyAdd.getText().toString(), dob.getText().toString(), city.getText().toString(),
                        country.getText().toString(), state.getText().toString(), pincode.getText().toString(),
                        landline.getText().toString(), website.getText().toString(), nature_bus.getSelectedItem().toString()
                        , "OTHERS", allied_subnature.getText().toString(), otp.getText().toString(), imageurl).get();
                        Log.e("jsonENTER",json.getString("response"));
              if (json.getString("response").equalsIgnoreCase("success")) {
                  sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
                  editor = sharedPreferences.edit();
                  editor.putString("userid", userid);
                  editor.apply();
                  Intent i = new Intent(getApplicationContext(), ItemDisplay.class);
                  finishAffinity();
                  startActivity(i);
              }else {
                  Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
              }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Registeruser(allied_subnature.getText().toString());
        }
    }

    private void ValidationforOthers() {
        if (!otp.getText().toString().trim().equalsIgnoreCase(pword)) {
            Toast.makeText(register.this, "Invalid otp", Toast.LENGTH_SHORT).show();
        } else if (otp.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter otp", Toast.LENGTH_SHORT).show();
        } else if (name.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter name", Toast.LENGTH_SHORT).show();
            name.setText("");
        } else if (company.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter company name", Toast.LENGTH_SHORT).show();
            company.setText("");
        } else if (companyAdd.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter company address", Toast.LENGTH_SHORT).show();
            companyAdd.setText("");
        } else if (designation.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter designation", Toast.LENGTH_SHORT).show();
            designation.setText("");
        } else if (dob.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please select date of birth", Toast.LENGTH_SHORT).show();

        } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter email", Toast.LENGTH_SHORT).show();
            email.setText("");
        } else if (!isValidEmail(email.getText().toString().trim())) {
            Toast.makeText(register.this, "Please enter valid email", Toast.LENGTH_SHORT).show();

        } else if (city.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter location", Toast.LENGTH_SHORT).show();
            city.setText("");

        } else if (state.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter state", Toast.LENGTH_SHORT).show();
            state.setText("");

        } else if (country.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter country", Toast.LENGTH_SHORT).show();
            country.setText("");

        } else if (pincode.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter pincode", Toast.LENGTH_SHORT).show();
            pincode.setText("");

        } else if (landline.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter landline", Toast.LENGTH_SHORT).show();
            landline.setText("");

        } else if (website.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(register.this, "Please enter website", Toast.LENGTH_SHORT).show();
            landline.setText("");

        } else if (nature_bus.getSelectedItem().toString().trim().equalsIgnoreCase("") ||
                nature_bus.getSelectedItem().toString().equalsIgnoreCase("Select Nature of Business")) {
            Toast.makeText(register.this, "Please select nature of business", Toast.LENGTH_SHORT).show();

        } else if (!termsncondi.isChecked()) {
            Toast.makeText(register.this, "Please Accept Terms and Conditions", Toast.LENGTH_SHORT).show();

        } else if (!checkDiamond.isChecked() && !checkGold.isChecked() && !checkSilver.isChecked() &&
                !checkPlatinum.isChecked() && !checkGemStone.isChecked() && !checkLooseDimond.isChecked()) {
            Toast.makeText(register.this, "Please select Sub nature", Toast.LENGTH_SHORT).show();
        } else {
//                Log.d("Nature",nature_bus.getSelectedItem().toString());

            Platinum = "";
            Gold = "";
            Silver = "";
            Diamond = "";
            GemStone = "";
            LooseDiamond = "";
            LabGrownDiamonds = "";
            SubNature = "";

            if (checkPlatinum.isChecked())
                Platinum = "PLATINUM,";
            if (checkSilver.isChecked())
                Silver = "SILVER,";
            if (checkGold.isChecked())
                Gold = "GOLD,";
            if (checkDiamond.isChecked())
                Diamond = "DIAMOND";
            if (checkGemStone.isChecked())
                GemStone = "GEMSTONE";
            if (checkLooseDimond.isChecked())
                LooseDiamond = "LOOSE DIAMONDS";
            if (checkLooseDimond.isChecked())
                LooseDiamond = "LAB GROWN DIAMONDS";
            SubNature = Platinum + Silver + Gold + Diamond + GemStone + LooseDiamond + LooseDiamond;

            try {
                json = new api().execute(userid, name.getText().toString(), email.getText().toString(),
                        mobile.getText().toString(), designation.getText().toString(), company.getText().toString()
                        , companyAdd.getText().toString(), dob.getText().toString(),
                        city.getText().toString(), country.getText().toString(), state.getText().toString(), pincode.getText().toString(),
                        landline.getText().toString(), website.getText().toString(),nature_bus.getSelectedItem().toString(), SubNature, otp.getText().toString(), imageurl).get();
                Log.e("jsonENTER",json.getString("response"));

                if (json.getString("response").equalsIgnoreCase("success")) {
                    sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("userid", userid);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), ItemDisplay.class);
                    finishAffinity();
                    startActivity(i);
                }else {
                    Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Registeruser(SubNature);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (data != null) {
            if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                profile.setImageURI(resultUri);
                imageurl = resultUri.getPath();

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    class getUserdetails extends AsyncTask<String, String, JSONObject> {
        JSONObject json;

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String url = Constant.BASE_URL + "getuserdetail?userid=" + userid;
                json = RestJsonClient.connect(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
    }

    class api extends AsyncTask<String, String, JSONObject> {
        JSONObject json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            register_progress.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String url = Constant.BASE_URL + "profile";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                if (params[0] != null)
                    nameValuePairs.add(new BasicNameValuePair("userid", params[0]));
                if (params[1] != null)
                    nameValuePairs.add(new BasicNameValuePair("fname", params[1]));
                if (params[2] != null)
                    nameValuePairs.add(new BasicNameValuePair("email", params[2]));
                if (params[3] != null)
                    nameValuePairs.add(new BasicNameValuePair("mobile_phone", params[3]));
                if (params[4] != null)
                    nameValuePairs.add(new BasicNameValuePair("designation", params[4]));
                if (params[5] != null)
                    nameValuePairs.add(new BasicNameValuePair("company_name", params[5]));
                if (params[6] != null)
                    nameValuePairs.add(new BasicNameValuePair("company_address", params[6]));
                if (params[7] != null)
                    nameValuePairs.add(new BasicNameValuePair("dob", params[7]));
                if (params[8] != null)
                    nameValuePairs.add(new BasicNameValuePair("location", params[8]));
                if (params[9] != null)
                    nameValuePairs.add(new BasicNameValuePair("country", params[9]));
                if (params[10] != null)
                    nameValuePairs.add(new BasicNameValuePair("state", params[10]));
                if (params[11] != null)
                    nameValuePairs.add(new BasicNameValuePair("pincode", params[11]));
                if (params[12] != null)
                    nameValuePairs.add(new BasicNameValuePair("landline_no", params[12]));
                if (params[13] != null)
                    nameValuePairs.add(new BasicNameValuePair("website_url", params[13]));
                if (params[14] != null)
                    nameValuePairs.add(new BasicNameValuePair("nature_of_business", params[14]));
                if (params[15] != null)
                    nameValuePairs.add(new BasicNameValuePair("sub_nature", params[15]));
                if (params[16] != null)
                    nameValuePairs.add(new BasicNameValuePair("password", params[16]));
                if (params[17] != null)
                    nameValuePairs.add(new BasicNameValuePair("profile_picture", params[17]));
                json = RestJsonClient.post2(url, nameValuePairs);
//                json = RestJsonClient.post(url, nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject params) {

            register_progress.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            Toast.makeText(register.this, "Finished Uploading", Toast.LENGTH_SHORT).show();
            super.onPostExecute(params);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class api2 extends AsyncTask<String, String, JSONObject> {
        JSONObject json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            register_progress.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String url = Constant.BASE_URL + "profile";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                if (params[0] != null)
                    nameValuePairs.add(new BasicNameValuePair("userid", params[0]));
                if (params[1] != null)
                    nameValuePairs.add(new BasicNameValuePair("fname", params[1]));
                if (params[2] != null)
                    nameValuePairs.add(new BasicNameValuePair("email", params[2]));
                if (params[3] != null)
                    nameValuePairs.add(new BasicNameValuePair("mobile_phone", params[3]));
                if (params[4] != null)
                    nameValuePairs.add(new BasicNameValuePair("designation", params[4]));
                if (params[5] != null)
                    nameValuePairs.add(new BasicNameValuePair("company_name", params[5]));
                if (params[6] != null)
                    nameValuePairs.add(new BasicNameValuePair("company_address", params[6]));
                if (params[7] != null)
                    nameValuePairs.add(new BasicNameValuePair("dob", params[7]));
                if (params[8] != null)
                    nameValuePairs.add(new BasicNameValuePair("location", params[8]));
                if (params[9] != null)
                    nameValuePairs.add(new BasicNameValuePair("country", params[9]));
                if (params[10] != null)
                    nameValuePairs.add(new BasicNameValuePair("state", params[10]));
                if (params[11] != null)
                    nameValuePairs.add(new BasicNameValuePair("pincode", params[11]));
                if (params[12] != null)
                    nameValuePairs.add(new BasicNameValuePair("landline_no", params[12]));
                if (params[13] != null)
                    nameValuePairs.add(new BasicNameValuePair("website_url", params[13]));
                if (params[14] != null)
                    nameValuePairs.add(new BasicNameValuePair("nature_of_business", params[14]));
                if (params[15] != null)
                    nameValuePairs.add(new BasicNameValuePair("sub_nature", params[15]));
                if (params[16] != null)
                    nameValuePairs.add(new BasicNameValuePair("allied_user", params[16]));
                if (params[17] != null)
                    nameValuePairs.add(new BasicNameValuePair("password", params[17]));
                if (params[18] != null)
                    nameValuePairs.add(new BasicNameValuePair("profile_picture", params[18]));
                json = RestJsonClient.post2(url, nameValuePairs);
//                json = RestJsonClient.post(url, nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject params) {
            register_progress.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPostExecute(params);
        }
    }

    public void getLatLong() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(register.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(register.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(register.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    Log.e("latitude", location.getLatitude() + "");
                    Log.e("longitude", location.getLongitude() + "");
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    getLocationdata(latitude, longitude);
                }
            }
        }
    }

    public void getLocationdata(Double lat, Double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            Log.e("addresses", Arrays.toString(new List[]{addresses}));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            if (addresses.get(0).getLocality() != null) {
                fetch_city = addresses.get(0).getLocality();
                Log.e("fetch_city", fetch_city);
                city.setText(fetch_city);
            }
            if (addresses.get(0).getAdminArea() != null) {
                fetch_state = addresses.get(0).getAdminArea();
                Log.e("fetch_state", fetch_state);
                state.setText(fetch_state);
            }
            if (addresses.get(0).getPostalCode() != null) {
                fetch_zip = addresses.get(0).getPostalCode();
                Log.e("fetch_zip", fetch_zip);
                pincode.setText(fetch_zip);
            }
            if (addresses.get(0).getCountryName() != null) {
                fetch_country = addresses.get(0).getCountryName();
                country.setText(fetch_country);
            }
        }
    }
}