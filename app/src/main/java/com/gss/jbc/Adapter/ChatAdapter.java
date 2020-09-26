package com.gss.jbc.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gss.jbc.Model.ChatModal;
import com.gss.jbc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends  RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private ArrayList<ChatModal> CommentsArrayList;
    private Activity activity1;


    public ChatAdapter(FragmentActivity detailComments, ArrayList<ChatModal> commentModelArrayList) {
        activity1 = detailComments;
        CommentsArrayList = commentModelArrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView adapterCommentorName, adapterCommentContent;
        ImageView iv1;

        public MyViewHolder(View v) {
            super(v);
            adapterCommentorName = (TextView) v.findViewById(R.id.tv1);
            adapterCommentContent = (TextView) v.findViewById(R.id.tv2);
            iv1=(ImageView)v.findViewById(R.id.iv);

        }
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view, parent, false);
        return new ChatAdapter.MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {

        final ChatModal Items = CommentsArrayList.get(position);

        holder.adapterCommentorName.setText(Items.getFname());
        holder.adapterCommentContent.setText(Items.getMsg());
     // String  image_file_path ="http://jewelnet.in/assets/upload/update" + "/" ;

        Picasso.with(activity1).load(Items.getBaseUrl()).into(holder.iv1);
    }

    @Override
    public int getItemCount() {
        return CommentsArrayList.size();
    }

}
