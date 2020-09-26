package com.gss.jbc;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gss.jbc.Extra.AppConstant;
import com.gss.jbc.Extra.RestJsonClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProfileSelf extends AppCompatActivity {

    ImageView profile;
    TextView name,mobile,email,desi,company,company_address,city,dob,country,state,otp,terms;
    CheckBox button;
    Button edit,back;
    JSONObject json;
    Uri uri;
    StringBuilder build;
    InputStream inStream;
    List<NameValuePair> nameValuePairs;
    ArrayList<String> data;
    File file;
    String imageurl;
    private static final int READ_REQUEST_CODE = 200;
    String userid,pword;
    Spinner nature_bus;
    int select;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    ArrayList<String> arraylist;
    SharedPreferences sharedpreferences;
    int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_self);
        name = (TextView) findViewById(R.id.name);
        mobile = (TextView) findViewById(R.id.mobile);
        email =(TextView) findViewById(R.id.email);
        company = (TextView) findViewById(R.id.company);
        desi = findViewById(R.id.desig);
        company_address = (TextView) findViewById(R.id.comp_address);
        dob =(TextView) findViewById(R.id.dob);
        city =(TextView) findViewById(R.id.city);
        country=(TextView) findViewById(R.id.country);
        otp=(TextView) findViewById(R.id.otp);
        state=(TextView) findViewById(R.id.state);
        profile = findViewById(R.id.profile);
        nature_bus=(Spinner) findViewById(R.id.nat_bus);
        arraylist=new ArrayList<>();
        arraylist.add("SELECT NATURE OF BUSINESS");
        arraylist.add("MANUFACTURAR");
        arraylist.add("WHOLESALER");
        arraylist.add("RETAILER");
        arraylist.add("ALLIED");
        myCalendar = Calendar.getInstance();
        sharedpreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid=sharedpreferences.getString("userid","");
        otp.setText(sharedpreferences.getString("pword",""));


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }
        });

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                new DatePickerDialog(view.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileSelf.this,android.R.layout.simple_spinner_item,arraylist);
        nature_bus.setAdapter(adapter);

        try {
            String url=Constant.BASE_URL+"getuserdetail?userid="+userid;
            json=new getUserdetails().execute(url).get();

            name.setText(json.getString("fname").replace("null",""));
            email.setText(json.getString("email").replace("null",""));
            mobile.setText(json.getString("mobile_phone").replace("null",""));
            company.setText(json.getString("company_name").replace("null",""));
            company_address.setText(json.getString("company_address").replace("null",""));
            desi.setText(json.getString("designation").replace("null",""));
            if(!json.getString("dob").equalsIgnoreCase("00-00-0000"))
            dob.setText(json.getString("dob"));
            dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            if(json.getString("nature_of_business").equalsIgnoreCase("manufacturer"))
                select=1;
            else  if(json.getString("nature_of_business").equalsIgnoreCase("wholesaler"))
                select=2;
            else  if(json.getString("nature_of_business").equalsIgnoreCase("retailer"))
                select=3;
            else  if(json.getString("nature_of_business").equalsIgnoreCase("allied"))
                select=4;

            nature_bus.setSelection(select);

            city.setText(json.getString("location").replace("null",""));
            country.setText(json.getString("country").replace("null",""));
            state.setText(json.getString("state").replace("null",""));
            Picasso.with(getApplicationContext()).load(json.getString("profile_picture")).into(profile);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i,READ_REQUEST_CODE);
            }
        });

        edit = (Button) findViewById(R.id.edit);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(otp.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(ProfileSelf.this,"Please enter otp",Toast.LENGTH_SHORT).show();
                }
                else if(name.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(ProfileSelf.this,"Please enter name",Toast.LENGTH_SHORT).show();

                }
                else if(company.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(ProfileSelf.this,"Please enter company name",Toast.LENGTH_SHORT).show();

                }
                else if(dob.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(ProfileSelf.this,"Please select date of birth",Toast.LENGTH_SHORT).show();

                }
                else if(email.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(ProfileSelf.this,"Please enter email",Toast.LENGTH_SHORT).show();

                }
                else if(city.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(ProfileSelf.this,"Please enter location",Toast.LENGTH_SHORT).show();

                }
                else if(state.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(ProfileSelf.this,"Please enter state",Toast.LENGTH_SHORT).show();

                }
                else if(country.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(ProfileSelf.this,"Please enter country",Toast.LENGTH_SHORT).show();

                }
                else
                try
                {

                        json = new api().execute(userid, name.getText().toString(), email.getText().toString(), mobile.getText().toString(), company.getText().toString(), company_address.getText().toString(), dob.getText().toString(), city.getText().toString(), country.getText().toString(), state.getText().toString(), nature_bus.getSelectedItem().toString()).get();
                        Intent intent= new Intent(getApplicationContext(), profile.class);
                        intent.putExtra("user_id", userid);
                        startActivity(intent);

                   }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

        back = (Button) findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProfileSelf.super.onBackPressed();


            }
        });


    }


    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onActivityResult(int requestcode,int resultcode,Intent data) {
        Uri uri;
        if (data != null) {
            uri = data.getData();
            Log.d("data", uri.toString());
            ImageView categoryPic = (ImageView) findViewById(R.id.img);

            imageurl = AppConstant.getDocumentPath(ProfileSelf.this, uri);
            File file = new File(imageurl);
            Bitmap bitmap = BitmapFactory.decodeFile(imageurl);
            profile.setImageBitmap(bitmap);


        }
    }





    class getUserdetails extends AsyncTask<String,String,JSONObject>
    {

        JSONObject json;
        @Override
        protected JSONObject doInBackground(String... params) {

            try

            {

                String url="http://104.236.14.235/jewelnetinsort/index.php/Api/getuserdetail?userid="+userid;



                json = RestJsonClient.connect(params[0]);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return  json;

        }

        protected  void onPostExecute(JSONObject params)
        {

            super.onPostExecute(params);

        }


    }

    class api extends AsyncTask<String,String,JSONObject>
    {

        JSONObject json;
        @Override
        protected JSONObject doInBackground(String... params) {

            try
            {

                String url=Constant.BASE_URL+"profile";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                if(params[0]!=null)
                    nameValuePairs.add(new BasicNameValuePair("userid",params[0]));
                if(params[1]!=null)
                    nameValuePairs.add(new BasicNameValuePair("fname", params[1]));
                if(params[2]!=null)
                    nameValuePairs.add(new BasicNameValuePair("email", params[2]));
                if(params[3]!=null)
                    nameValuePairs.add(new BasicNameValuePair("mobile_phone", params[3]));
                if(params[4]!=null)
                    nameValuePairs.add(new BasicNameValuePair("designation", params[4]));
                if(params[5]!=null)
                    nameValuePairs.add(new BasicNameValuePair("company_name", params[5]));
                if(params[6]!=null)
                    nameValuePairs.add(new BasicNameValuePair("company_address", params[6]));
                if(params[7]!=null)
                    nameValuePairs.add(new BasicNameValuePair("dob", params[7]));
                if(params[8]!=null)
                    nameValuePairs.add(new BasicNameValuePair("location", params[8]));
                if(params[9]!=null)
                    nameValuePairs.add(new BasicNameValuePair("country", params[9]));
                if(params[10]!=null)
                    nameValuePairs.add(new BasicNameValuePair("state", params[10]));
                if(params[11]!=null)
                    nameValuePairs.add(new BasicNameValuePair("nature_of_business", params[11]));
                if(otp.getText()!=null)
                    nameValuePairs.add(new BasicNameValuePair("password", otp.getText().toString()));
                if(imageurl!=null)
                    nameValuePairs.add(new BasicNameValuePair("profile_picture", imageurl));
                else
                    nameValuePairs.add(new BasicNameValuePair("profile_picture", ""));

                Log.d("data",nameValuePairs.toString());
                json = RestJsonClient.post2(url,nameValuePairs);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return  json;
        }

        protected  void onPostExecute(JSONObject params)
        {
            super.onPostExecute(params);

        }
    }
}
