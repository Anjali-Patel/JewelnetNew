package com.gss.jbc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gss.jbc.Extra.RestJsonClient;

import org.json.JSONObject;

/**
 * Created by Admin on 31-05-2017.
 */


public class MenuFragment extends Fragment implements View.OnTouchListener {

    GestureDetector gestureDetector;
    TextView Post;
    RelativeLayout PostSlide, InviteLayout,chat;
    JSONObject json;
    String profile_base_path, news_base_path, userid, advertisement_base_path;
    SharedPreferences sharedPreferences;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slide_menu, container, false);
        RelativeLayout root = (RelativeLayout) rootView.findViewById(R.id.rootLayout);

        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", "");


        PostSlide = (RelativeLayout) rootView.findViewById(R.id.postslideLayout);
        InviteLayout = (RelativeLayout) rootView.findViewById(R.id.InviteLayout);
        chat=(RelativeLayout)rootView.findViewById(R.id.commentslideLayout1);

        PostSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = "";
                try {
                    json = new post_status().execute().get();
                    status = json.getString("status");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (status.equalsIgnoreCase("1")) {
                    Intent i = new Intent(getActivity(), PostActivity.class);
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("You need to subscribe for this services. Contact Vikas Chudasama at +91-9324254765.");
                    builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String uri = "tel:9324254765";
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(uri));
                            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                            }
                            else
                                startActivity(intent);
//                            Intent mintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:9324254765"));
//                            startActivity(mintent);
                        }
                    });
                    AlertDialog alertdialog = builder.create();
                    alertdialog.show();
                }

            }
        });

chat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), ChatActivity.class);
        startActivity(i);
    }
});
        InviteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.tellFriend));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });



        return rootView;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }


    private class post_status extends AsyncTask<String, String, JSONObject> {

        String http = Constant.BASE_URL+"getuserdetail?userid=" + userid;

        @Override
        protected JSONObject doInBackground(String... url) {

            try {
                json = RestJsonClient.connect(http);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        protected void onPostExecute(JSONObject params) {
            super.onPostExecute(params);
        }
    }
}

