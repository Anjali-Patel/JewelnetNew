package com.gss.jbc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by geniesuraj on 5/4/18.
 */


public class DisplayData extends RecyclerView.Adapter<DisplayData.MyViewHolder> {
    private ArrayList<InfoDirModel> infoDirModelArrayList;
    Context context;
    Activity activity;
    String statename, cityname, username;

    DisplayData(Activity activity, Context context, ArrayList<InfoDirModel> infoDirList, String statename, String cityname, String username) {
        this.activity = activity;
        this.context = context;
        this.infoDirModelArrayList = infoDirList;
        this.statename = statename;
        this.cityname = cityname;
        this.username = username;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);

        return new MyViewHolder(itemView);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView img;
        TextView name, company;
        LinearLayout layout;

        MyViewHolder(View itemview) {
            super(itemview);
            img = itemview.findViewById(R.id.img);
            name = itemview.findViewById(R.id.name);
            company = itemview.findViewById(R.id.company);
            layout = itemview.findViewById(R.id.layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        String profile_img = infoDirModelArrayList.get(position).getImgProfileStr();
        if (profile_img == null || profile_img.isEmpty() || profile_img.equalsIgnoreCase("null")
                || profile_img.equalsIgnoreCase(" ") ) {
            holder.img.setImageResource(R.drawable.user);
        } else {
            try {
                String loaderimage = infoDirModelArrayList.get(position).getImagebaseurl() + "/" +
                        infoDirModelArrayList.get(position).getStrId() + "/" + infoDirModelArrayList.get(position).getImgProfileStr();
                Picasso.with(context).load(loaderimage).into(holder.img);
            } catch (Exception e) {
                e.printStackTrace();
//                holder.img.setImageResource(R.drawable.user);
            }
        }



        holder.name.setText(infoDirModelArrayList.get(position).getStrName());
        holder.company.setText(infoDirModelArrayList.get(position).getStrCompName());
        holder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Profile2.class);
                i.putExtra("StrType",  infoDirModelArrayList.get(position).getStrType());
                i.putExtra("StrSubtype",  infoDirModelArrayList.get(position).getStrSubtype());
               // i.putParcelableArrayListExtra("info_list", infoDirModelArrayList);
                i.putExtra("pos", holder.getAdapterPosition());
                i.putExtra("statename", statename);
                i.putExtra("cityname", cityname);
                i.putExtra("username", username);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoDirModelArrayList.size();
    }
}