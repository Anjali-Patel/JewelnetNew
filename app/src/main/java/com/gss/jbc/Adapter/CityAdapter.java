package com.gss.jbc.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gss.jbc.Model.CityModel;
import com.gss.jbc.R;

import java.util.ArrayList;

public class CityAdapter  extends ArrayAdapter<CityModel> {

    private ArrayList<CityModel> arrayList = new ArrayList<CityModel>();
    private Context context;
    private Activity activity;
    public CityAdapter (Context context, ArrayList<CityModel> arrayList) {
        super(context, R.layout.city_listitems, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public CityModel getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
           /* LayoutInflater in = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = in.inflate(R.layout.country_listitems, null);*/

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.city_listitems, parent, false);
        }
        TextView city_name = convertView.findViewById(R.id.city_name);
        city_name.setText(arrayList.get(position).getCity_name());

        return convertView;
    }
}
