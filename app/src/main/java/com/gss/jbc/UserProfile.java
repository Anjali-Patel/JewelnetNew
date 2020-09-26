package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    JSONObject json;
    JSONArray json2;
    //ImageView profile;
    ImageView post;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ViewPager pager;
    PagerAdapter adapter;
    ArrayList<InfoDirModel> infoDirList = new ArrayList<InfoDirModel>();
    int position_pager;
    String statename, cityname, username, StrType, StrSubtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.infoDirList = getIntent().getExtras().getParcelableArrayList("info_list");
        this.position_pager = getIntent().getIntExtra("pos", 0);
        statename = (getIntent().getStringExtra("statename"));
        cityname = (getIntent().getStringExtra("cityname"));
        username = (getIntent().getStringExtra("username"));

        post = (ImageView) findViewById(R.id.share);
        pager = (ViewPager) findViewById(R.id.pager);


        pager.setAdapter(new PagerAdapter(getApplicationContext(), infoDirList));
        pager.setCurrentItem(getIntent().getExtras().getInt("pos", 0));

    }

    public class PagerAdapter extends android.support.v4.view.PagerAdapter {
        ArrayList<InfoDirModel> infoDirArrayList;

        RelativeLayout profileselect;
        Context context;
        LayoutInflater inflater;
        View item;
        BitmapDrawable drawable;

        PagerAdapter(Context context, ArrayList<InfoDirModel> infoDirArray) {
            this.context = context;
            this.infoDirArrayList = infoDirArray;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //this.profile_img=pro;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @NonNull
        public Object instantiateItem(ViewGroup container, final int position) {
            item = inflater.inflate(R.layout.fragment_profile, container, false);
            container.addView(item);
            TextView name, comp_name, mobile, email, city, state, country, address, nature_business, subnature, desig, geolocation, tv_landline, tv_website;
            ImageView profileImg;
            final String CompanyAddr;

            name = (TextView) item.findViewById(R.id.tv_name);
            comp_name = (TextView) item.findViewById(R.id.tv_compname);
            address = (TextView) item.findViewById(R.id.tv_compaddr);
            email = (TextView) item.findViewById(R.id.tv_email);
            city = (TextView) item.findViewById(R.id.tv_city);
            state = (TextView) item.findViewById(R.id.tv_state);
            desig = (TextView) item.findViewById(R.id.tv_desig);
            nature_business = (TextView) item.findViewById(R.id.tv_business);
            subnature = (TextView) item.findViewById(R.id.tv_subnature);
            country = (TextView) item.findViewById(R.id.tv_country);
//            mobile = (TextView) item.findViewById(R.id.tv_mobile);
            profileImg = (ImageView) item.findViewById(R.id.profile);
            geolocation= (TextView) item.findViewById(R.id.geolocation);
            tv_landline = (TextView) item.findViewById(R.id.tv_landline);
            tv_website = (TextView) item.findViewById(R.id.tv_website);


            name.setText(infoDirArrayList.get(position).getStrName());
            desig.setText(infoDirArrayList.get(position).getStrDesig());
            comp_name.setText(infoDirArrayList.get(position).getStrCompName());
            address.setText(infoDirArrayList.get(position).getStrCompAddr());
            tv_landline.setText(infoDirArrayList.get(position).getLandlineStr());
            tv_website.setText(infoDirArrayList.get(position).getWebsiteStr());
//            mobile.setText(infoDirArrayList.get(position).getStrMobile());
            email.setText(infoDirArrayList.get(position).getStrEmail());
            city.setText(infoDirArrayList.get(position).getStrCity());
            country.setText(infoDirArrayList.get(position).getStrCountry());
            state.setText(infoDirArrayList.get(position).getStrState());
            nature_business.setText(infoDirArrayList.get(position).getStrType());
            CompanyAddr = infoDirArrayList.get(position).getStrCompAddr();


            if (infoDirArrayList.get(position).getStrType().equalsIgnoreCase("ALLIED")){
                subnature.setText(infoDirArrayList.get(position).getStrSubtype()+"("
                        +infoDirArrayList.get(position).getStrallied()+")");
            }else {
                subnature.setText(infoDirArrayList.get(position).getStrSubtype());
            }
            String profile_img = infoDirArrayList.get(position).getImgProfileStr();

            if (profile_img == null || profile_img.isEmpty() || profile_img.equalsIgnoreCase("null")
                    || profile_img.equalsIgnoreCase(" ") ) {
                profileImg.setImageResource(R.drawable.user);
            } else {
                try {
                    String loaderimage = infoDirArrayList.get(position).getImagebaseurl() + "/" +
                            infoDirArrayList.get(position).getStrId() + "/" + infoDirArrayList.get(position).getImgProfileStr();

                    Picasso.with(UserProfile.this).load(loaderimage).into(profileImg);
                } catch (Exception e) {
                    e.printStackTrace();
//                holder.img.setImageResource(R.drawable.user);
                }
            }

            geolocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+CompanyAddr));
                    startActivity(intent);
                }
            });

            return item;
        }

        @Override
        public int getCount() {
            return infoDirArrayList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int x = item.getItemId();

        switch (item.getItemId()) {

            case R.id.back:
                this.onBackPressed();
                break;
            case R.id.homeAsUp:
                this.onBackPressed();
                break;
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(!statename.equalsIgnoreCase("0")){
            Intent intent = new Intent(UserProfile.this, SearchByStateDirectory.class);
            intent.putExtra("statename", statename);
            startActivity(intent);
        }
        else if(!cityname.equalsIgnoreCase("0")){

            Intent intent = new Intent(UserProfile.this, SearchchBycityDirectory.class);
            intent.putExtra("cityname", cityname);
            startActivity(intent);
        }
        else if(!username.equalsIgnoreCase("0")){
            Intent intent = new Intent(UserProfile.this, SearchByNameDirectory.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else{
            Intent intent = new Intent(UserProfile.this, Directory.class);
            intent.putExtra("type", StrType);
            intent.putExtra("subtype", StrSubtype);
            startActivity(intent);


        }


    }
}
