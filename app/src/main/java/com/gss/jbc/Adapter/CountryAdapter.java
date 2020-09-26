package com.gss.jbc.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gss.jbc.Constant;
import com.gss.jbc.Model.CountryModel;
import com.gss.jbc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<CountryModel> {
    private ArrayList<CountryModel> arrayList = new ArrayList<CountryModel>();
    private Context context;
    private Activity activity;
    public CountryAdapter(Context context, ArrayList<CountryModel> arrayList) {
        super(context, R.layout.country_listitems, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public CountryModel getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

  /*  @Override
    public long getItemId(int position) {
        return null;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
           /* LayoutInflater in = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = in.inflate(R.layout.country_listitems, null);*/

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.country_listitems, parent, false);
        }
            ImageView flag = convertView.findViewById(R.id.country_image);
            TextView country_code = convertView.findViewById(R.id.country_code);
            TextView country_name = convertView.findViewById(R.id.country_name);
            country_code.setText(arrayList.get(position).getMobile_code());
            country_name.setText(arrayList.get(position).getCountry_name());
            Picasso.with(context).
                    load(Constant.FLAG_URL+
                            arrayList.get(position).getFlag()).into(flag);

        //country_name.setText(arrayList.get(position).getCountry_name());


        String a = arrayList.get(position).getMobile_code();
        String a1 = arrayList.get(position).getCountry_name();



        //}
        return convertView;
    }
}
