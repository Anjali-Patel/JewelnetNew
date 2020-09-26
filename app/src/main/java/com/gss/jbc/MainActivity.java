package com.gss.jbc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gss.jbc.Adapter.CountryAdapter;
import com.gss.jbc.AsynkTask.CountryListAsynktask;
import com.gss.jbc.Extra.RestJsonClient;
import com.gss.jbc.Model.CountryModel;
import com.gss.jbc.Utility.SharedPreferenceUtils;
import com.gss.jbc.Utility.CommonUtils;

import com.squareup.picasso.Picasso;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.io.IOException;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements LocationListener{

    private Activity activity1;

    EditText mobile;
    Button submit;
    JSONObject json,json2;
    Long mobilenumber;
    String url, response;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView country_flag, Imgflag;
    EditText country_code,country_name;
    private ArrayList<CountryModel> arrayList = new ArrayList<CountryModel>();
    private Context context;
    LocationManager locationManager;
    private String locationText;
    TextView Mylocation;
    Geocoder geocoder;
    SharedPreferenceUtils preferances;
    ArrayList<CountryModel> CountryArrayListTmp = new ArrayList<>();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mobile = findViewById(R.id.mobile);
        submit = findViewById(R.id.submit);
        country_flag = findViewById(R.id.country_flag);
        Imgflag = findViewById(R.id.Imgflag);
        country_name=findViewById(R.id.country_name);
        country_code = findViewById(R.id.country_code);
        preferances = SharedPreferenceUtils.getInstance(this);







        if (checkPermissions()) {
            getLocation();
            loadCountry();//Yes permissions are granted by the user. Go to the next step.
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }



        country_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCountry();

                //showCountryList();

                Intent i = new Intent(MainActivity.this, CountryListActivity.class);
                startActivityForResult(i, 1);
            }
        });


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("userid", "").length() > 0) {

            preferances.setValue(CommonUtils.NEWSCOUNT, "0");

            Intent i = new Intent(getApplicationContext(), ItemDisplay.class);
            startActivity(i);
            finish();
        }
//        if (sharedPreferences.getString("userid", "").length() > 0) {
//            Intent i = new Intent(getApplicationContext(), DemoActivity.class);
//            startActivity(i);
//            finish();
//        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isOnline() == true) {

                    getLocation();

                    if (mobile.getText().toString().length() < 10
                            || mobile.getText().toString().length() > 11) {
                        Toast.makeText(getApplicationContext(), "Please enter correct mobile number", Toast.LENGTH_SHORT).show();
                    } else {

                        url = Constant.BASE_URL+"register";
                        try {
                            json = new JSONObject();
                            json = new api().execute(url, mobile.getText().toString()).get();
                            response = json.getString("message");

                            Log.d("Register Mobile", json.toString());
                            if (response.equalsIgnoreCase("Mobile number already exist")) {
                                preferances.setValue(CommonUtils.PHONE, mobile.getText().toString());

                                Intent i = new Intent(getApplicationContext(), register.class);
                           /* sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("userid", json.getString("userid"));
                            editor.apply(); */

                                i.putExtra("pword",json.getString("pword"));
                                i.putExtra("mobileno",mobile.getText().toString());
                                i.putExtra("userid", json.getString("userid"));
                                startActivity(i);
//                            finish();
                            } else if (response.equalsIgnoreCase("OTP is send to your mobile number!")) {
                                preferances.setValue(CommonUtils.PHONE, mobile.getText().toString());

                                Intent i = new Intent(getApplicationContext(), register.class);
                                i.putExtra("userid", json.getString("userid"));
                                i.putExtra("mobileno",mobile.getText().toString());
                                i.putExtra("pword",json.getString("pword"));
                            /* sharedPreferences = getSharedPreferences("ID", Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("userid", json.getString("userid"));
                            editor.apply();*/
                                startActivity(i);
//                            finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                else{
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    builder.create();
                    builder.setMessage("Internet connectivity is not available!");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    });

                    builder.show();
                }

            }
        });
    }


    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public void onLocationChanged(Location location) {
        // locationText = ("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        //locationText = ("Latitude: " + location.getLatitude() + "Longitude: " + location.getLongitude());
        //Mylocation.setText(locationText);
        double a = location.getLatitude();
        double b = location.getLongitude();
        String n = a+"";
        String l = b+"";

        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            String countryCode = addresses.get(0).getCountryCode();


            preferances.setValue(CommonUtils.LATTITUTE, a);
            preferances.setValue(CommonUtils.LONGITUDE, b);

            preferances.setValue(CommonUtils.COUNTRY, country);
            preferances.setValue(CommonUtils.STATE, state);
            preferances.setValue(CommonUtils.CITY, city);
            preferances.setValue(CommonUtils.PINCODE, postalCode);
            preferances.setValue(CommonUtils.COUNTRYCODE, countryCode);


            String id = preferances.getStringValue(CommonUtils.STATE,"");






        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();

    }


    private void loadCountry() {
        try {
            String itemToFind = preferances.getStringValue(CommonUtils.COUNTRYCODE,"");

            // String itemToFind = "AS";
            arrayList.clear();
            json2 = new CountryListAsynktask().execute().get();
            int success = json2.getInt("status");
            if (success == 1) {
                JSONArray jsonArray = json2.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newJson = jsonArray.getJSONObject(i);
                    CountryModel model = new CountryModel();
                    String Match = newJson.getString("code");

                    if(Match.equals(itemToFind)){
                        country_code.setText(newJson.getString("mob_code"));
                        country_name.setText("("+newJson.getString("name")+")");

                        String img=newJson.getString("image");

                        // Picasso.with(context).load(Constant.FLAG_URL+newJson.getString("image")).into(Imgflag);

                        //Picasso.with(context).load(Constant.FLAG_URL+img).into(country_flag);

                        String a = "d";

                    }

                    model.setMobile_code(newJson.getString("mob_code"));
                    model.setCountry_name(newJson.getString("name"));
                    model.setCountry_code(newJson.getString("code"));
                    model.setFlag(newJson.getString("image"));
                    arrayList.add(model);
                    CountryArrayListTmp.add(model);

                }

            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void CompareCountries() {

    }

    private class api extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... url) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("mobile_phone", url[1]));
            try {
                json = RestJsonClient.post(url[0], nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject params) {
            super.onPostExecute(params);
        }
    }


    public void showCountryList(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view1 = inflater.inflate(R.layout.layout_countrylist, null);
        builder.setView(view1);
        final ListView countryListview = view1.findViewById(R.id.countryListview);
        //CountryAdapter adapter = new CountryAdapter(getApplicationContext(),arrayList, MainActivity.this);
        CountryAdapter adapter = new CountryAdapter(getApplicationContext(),arrayList);
        countryListview.setAdapter(adapter);
        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String flag = data.getStringExtra("flag");
                String mobilecode = data.getStringExtra("mobilecode");
                String country=data.getStringExtra("cname");
                Picasso.with(getApplicationContext()).
                        load("http://demo1.geniesoftsystem.com/newweb/jewelnetnew/assets/flags/"+
                                flag).into(country_flag);
                country_code.setText(mobilecode);
                country_name.setText("("+country+")");

            }
        }
    }

    public void requestForCameraPermission() {
        final String permission = android.Manifest.permission.CAMERA;
        final String permissionreadstorage = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else if (ContextCompat.checkSelfPermission(MainActivity.this, permissionreadstorage) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionreadstorage)) {
                showPermissionRationaleDialog("Test", permissionreadstorage);
            } else {
                requestForPermission(permissionreadstorage);
            }
        } else {

        }
    }
    private void showPermissionRationaleDialog(final String message, final String permission) {
        MainActivity.this.requestForPermission(permission);
    }
    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
    }


    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale || shouldProvideRationale2) {

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                getLocation();
                loadCountry();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
                loadCountry();
//                requestForCameraPermission();

            } else {
                getLocation();
                loadCountry();
                requestForCameraPermission();

            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
