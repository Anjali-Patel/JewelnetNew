package com.gss.jbc.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gss.jbc.NewsItemStructure;
import com.gss.jbc.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context context;
    private ArrayList<NewsItemStructure> NewsArrayList;

    public NewsAdapter(Context context, ArrayList<NewsItemStructure> NewsModelArrayList)
    {
        this.context=context;
        NewsArrayList = NewsModelArrayList;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView, description, profile_name, read, address, comp_name,Countviews,like;
        TextView CommentButton,likecount, CommentCount, Share;



        public MyViewHolder(RelativeLayout view) {
            super(view);

            textView = view.findViewById(R.id.textView);
            description = view.findViewById(R.id.description);
            address = view.findViewById(R.id.address);
            comp_name = view.findViewById(R.id.tvcompny);
            profile_name = view.findViewById(R.id.profile_name);
            read = view.findViewById(R.id.readmore);
            like= view.findViewById(R.id.Like);
            CommentButton = view.findViewById(R.id.CommentButton);
            Countviews= view.findViewById(R.id.Countviews);
            likecount = view.findViewById(R.id.LikeCount);
            CommentCount = view.findViewById(R.id.CommentCount);
            Share = view.findViewById(R.id.Share);


        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        final NewsItemStructure Items = NewsArrayList.get(position);

        holder.textView.setText(Items.getnTitle());
        holder.description.setText(Items.getnMessage());
        holder.address.setText(Items.getnCompanyAddress().replace("null", ""));
        holder.comp_name.setText(Items.getnCompanyName().replace("null", ""));
        holder.profile_name.setText(Items.getnFname());
        holder.likecount.setText(Items.getIsLiked());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewgroup, final int position)
    {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.drawable,viewgroup,false);

        MyViewHolder holder = new MyViewHolder(layout);




        return holder;

    }

    @Override
    public int getItemCount()
    {

        return NewsArrayList.size();
    }
}
