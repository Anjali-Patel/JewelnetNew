package com.gss.jbc;//package jbc.gss.com.jewelnet;
//
//import android.graphics.Bitmap;import android.content.Context;
//
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.Html;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import org.w3c.dom.Text;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//
//
//public class ProfileFragment extends Fragment {
//    static int key;
//
//    String pName, pDesig, pCompName, pCompAddr, pCity, pState, pCountry, pType, pSubtype, pMobile, pEmail, pImage;
//
//
//    private OnFragmentInteractionListener mListener;
//
//    public ProfileFragment() {
//
//    }
//
//    public static ProfileFragment newInstance(InfoDirModel infoObj, int position) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString("name", infoObj.getStrName());
//        args.putString("designation", infoObj.getStrDesig());
//        args.putString("company", infoObj.getStrCompName());
//        args.putString("phone", infoObj.getStrMobile());
//        args.putString("email", infoObj.getStrEmail());
//        args.putString("image", infoObj.getImgProfileStr());
//        args.putString("address", infoObj.getStrCompAddr());
//        args.putString("city", infoObj.getStrCity());
//        args.putString("country", infoObj.getStrCountry());
//        args.putString("state", infoObj.getStrState());
//        args.putString("nature", infoObj.getStrType());
//        args.putString("subnature", infoObj.getStrSubtype());
//        args.putInt("key",position);
//        fragment.setArguments(args);
//
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        pImage = getArguments().getString("image");
//
//        pName = getArguments().getString("name");
//        pDesig = getArguments().getString("designation");
//        pCompName = getArguments().getString("company");
//        pCompAddr =getArguments().getString("address");
//
//        pMobile = getArguments().getString("phone");
//        pEmail = getArguments().getString("email");
//
//        pSubtype = getArguments().getString("subnature");
//        pType = getArguments().getString("nature");
//
//        pCity = getArguments().getString("city");
//        pState =getArguments().getString("state");
//        pCountry = getArguments().getString("country");
//
//        key = getArguments().getInt("key");
//
//
//        Log.d("data for ",pName);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        ViewGroup view = (ViewGroup) inflater.inflate(
//                R.layout.fragment_profile, container, false);
//
//        TextView name, comp_name, mobile, email, city, state, country,address, nature_business, subnature, desig;
//        ImageView profile;
//
//        profile = (ImageView) view.findViewById(R.id.profile);
//        name = (TextView) view.findViewById(R.id.tv_name);
//        comp_name = (TextView) view.findViewById(R.id.tv_compname);
//        address = (TextView) view.findViewById(R.id.tv_compaddr);
//        email = (TextView) view.findViewById(R.id.tv_email);
//        city = (TextView) view.findViewById(R.id.tv_city);
//        state = (TextView) view.findViewById(R.id.tv_state);
//        desig = (TextView) view.findViewById(R.id.tv_desig);
//        nature_business = (TextView) view.findViewById(R.id.tv_business);
//        subnature = (TextView) view.findViewById(R.id.tv_subnature);
//        country = (TextView) view.findViewById(R.id.tv_country);
////        mobile = (TextView) view.findViewById(R.id.tv_mobile);
//
//        new DownloadImageTask(profile).execute(pImage);
//        name.setText(pName);
//        desig.setText(pDesig);
//        comp_name.setText(pCompName);
//        address.setText(pCompAddr);
////        mobile.setText(pMobile);
//        email.setText(pEmail);
//        city.setText(pCity);
//        country.setText(pCountry);
//        state.setText(pState);
//        nature_business.setText(pType);
//        subnature.setText(pSubtype);
//
//        return view;
//    }
//
//     public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
//
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//
//    }
//}
