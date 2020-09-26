package com.gss.jbc.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gss.jbc.AllLikeList;
import com.gss.jbc.CalenderEvents;
import com.gss.jbc.Model.ExhibitionEventDataModel;
import com.gss.jbc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExhibitionsAdapter extends RecyclerView.Adapter<ExhibitionsAdapter.MyViewHolder>  {
    private Activity activity1;
    private ArrayList<ExhibitionEventDataModel> ExhibitionsArrayList;

//String file_path = "http://jewelnet.in/assets/upload/update" + "/" + news_file_name;


    public ExhibitionsAdapter(CalenderEvents events, ArrayList<ExhibitionEventDataModel> ExhibitionModelArrayList) {
        activity1 = events;
        ExhibitionsArrayList = ExhibitionModelArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView EventTitle,EventDesc, Event2Title;
        ImageView close, EventImage;
        RelativeLayout event2Relative;

        public MyViewHolder(View v) {
            super(v);
            event2Relative=v.findViewById(R.id.event2Relative);
            EventTitle=v.findViewById(R.id.event_title);
            EventDesc=v.findViewById(R.id.event_note);
            Event2Title=v.findViewById(R.id.event2_title);
            close=v.findViewById(R.id.close);
            EventImage=v.findViewById(R.id.event_img);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_event, parent, false);
        return new MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        String Basepath = "http://jewelnet.in/assets/upload/exhibitions/";
        final ExhibitionEventDataModel Items = ExhibitionsArrayList.get(position);

        final String web = Items.getEventLink();

        holder.close.setVisibility(View.GONE);
        holder.EventTitle.setText(Items.getEventTitle());
        holder.EventDesc.setText(Items.getEventDate() + "\n" + Items.getEventVenue());
        holder.Event2Title.setPaintFlags(holder.Event2Title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Picasso.with(activity1).load(Basepath + Items.getEventLogo()).error(R.drawable.default_image).into(holder.EventImage);

        if (!web.equalsIgnoreCase("")) {
            holder.EventTitle.setPaintFlags(holder.EventTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.EventTitle.setMovementMethod(LinkMovementMethod.getInstance());
            holder.EventTitle.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(web));
                    activity1.startActivity(browserIntent);
                }
            });
        }


        holder.Event2Title.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://artofjewellery.com/IndianEventDetails.aspx?id=10237"));
                activity1.startActivity(browserIntent);
            }
        });



    }

    @Override
    public int getItemCount() { return ExhibitionsArrayList.size(); }
}
