package com.gss.jbc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.gss.jbc.Adapter.ExhibitionsAdapter;
import com.gss.jbc.Extra.MyEventDay;
import com.gss.jbc.Model.ExhibitionEventDataModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class CalenderEvents extends AppCompatActivity {

    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;
    String month;
    ArrayList<ExhibitionEventDataModel> CalenderEvents = new ArrayList<ExhibitionEventDataModel>();
    private CalendarView mCalendarView;
    private List<EventDay> mEventDays = new ArrayList<>();

    String MonthSelected;

    Context activityContext;
    ExhibitionsAdapter adapter;
    RecyclerView.LayoutManager layoutmanager;

    private static final String TAG = "CalenderEvents";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_events);


        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        //mCalendarView.setDate(new Date(1546281000));
        month = getIntent().getExtras().getString("month");

        //GetAllEvents();
        //SetEvents();
        mCalendarView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Log.i(TAG, "onLayoutChange: ");
            }
        });

        if (month.equalsIgnoreCase("jan")) {

            GetAllEvents("01");
            MonthSelected = "01";

            Calendar c = Calendar.getInstance();
            c.set(2019, 0, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);
        } else if (month.equalsIgnoreCase("feb")) {

            GetAllEvents("02");
            MonthSelected = "02";

            Calendar c = Calendar.getInstance();
            c.set(2019, 1, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);

        } else if (month.equalsIgnoreCase("mar")) {

            GetAllEvents("03");
            MonthSelected = "03";

            Calendar c = Calendar.getInstance();
            c.set(2019, 2, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);
        } else if (month.equalsIgnoreCase("apr")) {

            GetAllEvents("04");
            MonthSelected = "04";

            Calendar c = Calendar.getInstance();
            c.set(2019, 3, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);

        } else if (month.equalsIgnoreCase("may")) {

            GetAllEvents("05");
            MonthSelected = "05";

            Calendar c = Calendar.getInstance();
            c.set(2019, 4, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);


        } else if (month.equalsIgnoreCase("jun")) {

            GetAllEvents("06");
            MonthSelected = "06";

            Calendar c = Calendar.getInstance();
            c.set(2019, 5, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);


        } else if (month.equalsIgnoreCase("jul")) {

            GetAllEvents("07");
            MonthSelected = "07";

            Calendar c = Calendar.getInstance();
            c.set(2019, 6, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);

        } else if (month.equalsIgnoreCase("aug")) {

            GetAllEvents("08");
            MonthSelected = "08";

            Calendar c = Calendar.getInstance();
            c.set(2019, 7, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);


        } else if (month.equalsIgnoreCase("sept")) {

            GetAllEvents("09");
            MonthSelected = "09";

            Calendar c = Calendar.getInstance();
            c.set(2019, 8, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);

        } else if (month.equalsIgnoreCase("oct")) {

            GetAllEvents("10");
            MonthSelected = "10";

            Calendar c = Calendar.getInstance();
            c.set(2019, 9, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);

        } else if (month.equalsIgnoreCase("nov")) {

            GetAllEvents("11");
            MonthSelected = "11";

            Calendar c = Calendar.getInstance();
            c.set(2019, 10, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);


        } else if (month.equalsIgnoreCase("dec")) {

            GetAllEvents("12");
            MonthSelected = "12";

            Calendar c = Calendar.getInstance();
            c.set(2019, 11, 1);
            Date myEventDay = c.getTime();
            mCalendarView.setDate(myEventDay);


        }

        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                if (eventDay.getImageResource() == 0) {

                } else {

                    Calendar c = eventDay.getCalendar();
                    Date d = c.getTime();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String strDate = dateFormat.format(d);

                    boolean isMultipleEvent = false;
                    ArrayList<ExhibitionEventDataModel> eventDays = new ArrayList<>();

                    for (int i = 0; i < CalenderEvents.size(); i++) {
                        if (strDate.equals(CalenderEvents.get(i).getEventDate())) {
                            eventDays.add(CalenderEvents.get(i));
                            isMultipleEvent = true;
                            Log.i("Event", "onDayClick: ");
                        }

                    }
                    if (!isMultipleEvent)

                        previewNote(eventDay);
                    else {
                        previewNotes(eventDays);
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            MyEventDay myEventDay = data.getParcelableExtra(RESULT);
            mCalendarView.setDate(myEventDay.getCalendar());
            mEventDays.add(myEventDay);
            mCalendarView.setEvents(mEventDays);
        }
    }


    private void previewNote(EventDay eventDay) {

        MyEventDay myEventDay = (MyEventDay) eventDay;

        String Basepath = "http://jewelnet.in/assets/upload/exhibitions/";

        String n = myEventDay.getNote();
        String d = myEventDay.getDescription();
        String logo = myEventDay.getEventLogo();
        final String web = myEventDay.getWebLink();


        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(CalenderEvents.this);

        LayoutInflater inflater = LayoutInflater.from(CalenderEvents.this);
        View dialogView = inflater.inflate(R.layout.today_event, null);

        RelativeLayout event2Relative = (RelativeLayout) dialogView.findViewById(R.id.event2Relative);
        final TextView EventTitle = (TextView) dialogView.findViewById(R.id.event_title);
        final TextView EventDesc = (TextView) dialogView.findViewById(R.id.event_note);
        final TextView Event2Title = (TextView) dialogView.findViewById(R.id.event2_title);
        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        ImageView EventImage = (ImageView) dialogView.findViewById(R.id.event_img);


        dialog.setView(dialogView);

        event2Relative.setVisibility(View.GONE);
        if (n.equalsIgnoreCase("India Jewellery Show")) {
            event2Relative.setVisibility(View.VISIBLE);
        }

        EventTitle.setText(n);
        EventDesc.setText(d);

        Picasso.with(CalenderEvents.this).load(Basepath + logo).error(R.drawable.default_image).into(EventImage);
//            EventImage.setImageResource(myEventDay.getImageResource());
        Event2Title.setPaintFlags(Event2Title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        if (!web.equalsIgnoreCase("")) {
            EventTitle.setPaintFlags(EventTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            EventTitle.setMovementMethod(LinkMovementMethod.getInstance());
            EventTitle.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(web));
                    startActivity(browserIntent);
                }
            });
        }

        Event2Title.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://artofjewellery.com/IndianEventDetails.aspx?id=10237"));
                startActivity(browserIntent);
            }
        });


        final android.app.AlertDialog alertdialog = dialog.create();
        alertdialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.dismiss();
            }
        });


    }

    private void previewNotes(ArrayList<ExhibitionEventDataModel> eventDays) {

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(CalenderEvents.this);

        LayoutInflater inflater = LayoutInflater.from(CalenderEvents.this);
        View dialogView = inflater.inflate(R.layout.all_exhibitions_list, null);


        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        RecyclerView EventList = (RecyclerView) dialogView.findViewById(R.id.ExhibitionList);

        adapter = new ExhibitionsAdapter(CalenderEvents.this, eventDays);
        layoutmanager = new LinearLayoutManager(CalenderEvents.this);
        EventList.setLayoutManager(layoutmanager);
        EventList.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        dialog.setView(dialogView);

        final android.app.AlertDialog alertdialog = dialog.create();
        alertdialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.dismiss();
            }
        });


    }

//        AlertDialog.Builder builder = new AlertDialog.Builder(CalenderEvents.this);
//        builder.create();
//        builder.setMessage(n);
//
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//            }
//        });
//
//        builder.show();

    private void SetEvents() {


//        String sDate1="2019-05-07";
//
//        String[] items = sDate1.split("-");
//        List<String> DateList = new ArrayList<String>(Arrays.asList(items));
//        int month = Integer.parseInt(DateList.get(1)) - 1;
//        int date = Integer.parseInt(DateList.get(2));
//        int year = Integer.parseInt(DateList.get(0));
//
//        Calendar c = Calendar.getInstance();
//        c.set(year,month,date);
//
//        Date akshayDay = c.getTime();
//        mCalendarView.setDate(akshayDay);
//        MyEventDay akshay = new MyEventDay(c, R.drawable.akshayjpg, "Akshaya Tritiya 2019","May 7-7-2019","");
//
//        mEventDays.add(akshay);
//        mCalendarView.setEvents(mEventDays);


//        Calendar c1 = Calendar.getInstance();
//        c1.set(2019,4,7);
//
//        Calendar c2 = Calendar.getInstance();
//        c2.set(2019,4,25);
//
//        Date akshayDay = c1.getTime();
//        mCalendarView.setDate(akshayDay);
//        MyEventDay akshay = new MyEventDay(c1, R.drawable.akshayjpg, "Akshaya Tritiya 2019","May 7-7-2019","");
//
//        Date rajkotDay1 = c2.getTime();
//        mCalendarView.setDate(rajkotDay1);
//        MyEventDay rajkot1 = new MyEventDay(c2, R.drawable.rajkot, "Rajkot Gems & Jewellery Show", "May 25-27-2019", "http://artofjewellery.com/IndianEventDetails.aspx?id=10218");
//
//        mEventDays.add(akshay);
//        mEventDays.add(rajkot1);
//        mCalendarView.setEvents(mEventDays);
//
//
//        Calendar c3 = Calendar.getInstance();
//        c3.set(2019,5,11);
//
//
//        Calendar c4 = Calendar.getInstance();
//        c4.set(2019,5,14);
//
//        Calendar c5 = Calendar.getInstance();
//        c5.set(2019,5,15);
//
//        Calendar c6 = Calendar.getInstance();
//        c6.set(2019,5,28);
//
//
//        Date igi1 = c3.getTime();
//        mCalendarView.setDate(igi1);
//        MyEventDay igiDay1 = new MyEventDay(c3, R.drawable.igishow, "IGI D Show", "June 11-13-2019 | Goa","http://artofjewellery.com/IndianEventDetails.aspx?id=10219");
//
//
//        Date hyd1 = c4.getTime();
//        mCalendarView.setDate(hyd1);
//        MyEventDay hydDay1 = new MyEventDay(c4, R.drawable.hydrabadjwellary, "Hyderabad Jewellery Pearl & Gem Fair","June 14-16-2019 | Hyderabad","http://artofjewellery.com/IndianEventDetails.aspx?id=10220");
//
//        Date unique = c5.getTime();
//        mCalendarView.setDate(unique);
//        MyEventDay uniqueDay = new MyEventDay(c5, R.drawable.unique, "Unique Gems and Jewellery International Show","June 15-17-2019 | Pune","http://artofjewellery.com/IndianEventDetails.aspx?id=10221");
//
//        Date coimb = c6.getTime();
//        mCalendarView.setDate(coimb);
//        MyEventDay coimbDay = new MyEventDay(c6, R.drawable.coinbator, "Coimbatore Jewellery Show","June 28-30-2019 | Coimbatore","http://artofjewellery.com/IndianEventDetails.aspx?id=10222");
//
//
//        mEventDays.add(igiDay1);
//        mEventDays.add(hydDay1);
//        mEventDays.add(uniqueDay);
//        mEventDays.add(coimbDay);
//
//
//        Calendar c7 = Calendar.getInstance();
//        c7.set(2019,6,3);
//
//        Calendar c8 = Calendar.getInstance();
//        c8.set(2019,6,9);
//
//        Calendar c9 = Calendar.getInstance();
//        c9.set(2019,6,19);
//
//        Calendar c10 = Calendar.getInstance();
//        c10.set(2019,6,27);
//
//        Date myEventDay = c7.getTime();
//        mCalendarView.setDate(myEventDay);
//        MyEventDay eventdate = new MyEventDay(c7, R.drawable.manhaton, "Manthan 2019", "July 2-3-2019 | Mumbai","http://artofjewellery.com/IndianEventDetails.aspx?id=10223");
//
//        Date forever = c8.getTime();
//        mCalendarView.setDate(forever);
//        MyEventDay foreverDay = new MyEventDay(c8, R.drawable.forevermark, "Forevermark Forum 2019","July 9-11-2019 | Bangalore","http://artofjewellery.com/IndianEventDetails.aspx?id=10225");
//
//        Date zak = c9.getTime();
//        mCalendarView.setDate(zak);
//        MyEventDay zakDay = new MyEventDay(c9, R.drawable.zak, "Zak Jewels Expo","July 19-21-2019 | Chennai","http://artofjewellery.com/IndianEventDetails.aspx?id=10226");
//
//        Date times = c10.getTime();
//        mCalendarView.setDate(times);
//        MyEventDay timesDay = new MyEventDay(c10, R.drawable.timesglam, "Times Glamour","July 27-29-2019 | Mumbai","http://artofjewellery.com/IndianEventDetails.aspx?id=10227");
//
//        mEventDays.add(eventdate);
//        mEventDays.add(foreverDay);
//        mEventDays.add(zakDay);
//        mEventDays.add(timesDay);
//
//        Calendar c12 = Calendar.getInstance();
//        c12.set(2019,7,3);
//
//        Calendar c13 = Calendar.getInstance();
//        c13.set(2019,7,8);
//
//        Calendar c14 = Calendar.getInstance();
//        c14.set(2019,7,9);
//
//        Calendar c15 = Calendar.getInstance();
//        c15.set(2019,7,20);
//
//        Date carat = c12.getTime();
//        mCalendarView.setDate(carat);
//        MyEventDay caratDay = new MyEventDay(c12, R.drawable.carats, "Carats Surat Diamond Expo", "August 3-5-2019 | Surat","http://artofjewellery.com/IndianEventDetails.aspx?id=10228");
//
//        Date iij = c13.getTime();
//        mCalendarView.setDate(iij);
//        MyEventDay iijDay = new MyEventDay(c13, R.drawable.iijsshow, "India International Jewellery Show (IIJS 2019)","August 8-12-2019 | Mumbai","http://artofjewellery.com/IndianEventDetails.aspx?id=10229");
//
//        Date india = c14.getTime();
//        mCalendarView.setDate(india);
//        MyEventDay indiaDay = new MyEventDay(c14, R.drawable.indiagemshow, "India Gem & Jewellery Machinery Expo (IGJME)","August 9-12-2019 | Mumbai","http://artofjewellery.com/IndianEventDetails.aspx?id=10230");
//
//        Date fashion = c15.getTime();
//        mCalendarView.setDate(fashion);
//        MyEventDay fashionday = new MyEventDay(c15, R.drawable.fashionjwellary, "13th India International Fashion Jewellery & Accessories Show 2019","August 20-23-2019 | Mumbai","http://artofjewellery.com/IndianEventDetails.aspx?id=10224");
//
//        mEventDays.add(caratDay);
//        mEventDays.add(iijDay);
//        mEventDays.add(indiaDay);
//        mEventDays.add(fashionday);
//
//        Calendar c16 = Calendar.getInstance();
//        c16.set(2019,8,13);
//
//        Calendar c17 = Calendar.getInstance();
//        c17.set(2019,8,20);
//
//        Calendar c18 = Calendar.getInstance();
//        c18.set(2019,8,28);
//
//
//        Date gem = c16.getTime();
//        mCalendarView.setDate(gem);
//        MyEventDay gemDay = new MyEventDay(c16, R.drawable.gemjwellary, "Gem and Jewellery India International Fair", "September 13-15-2019 | Chennai","http://artofjewellery.com/IndianEventDetails.aspx?id=10231");
//
//        Date jewel = c17.getTime();
//        mCalendarView.setDate(jewel);
//        MyEventDay jewelDay = new MyEventDay(c17, R.drawable.jewelnetahmedabad, "JewelNet","September 20-21-2019 | Novotel Hotel, Ahmedabad","");
//
//        Date delhi = c18.getTime();
//        mCalendarView.setDate(delhi);
//        MyEventDay delhiDay = new MyEventDay(c18, R.drawable.delhijwellary, "Delhi Jewellery & Gem Fair","September 28-30-2019 | New Delhi","http://artofjewellery.com/IndianEventDetails.aspx?id=10232");
//
//
//        mEventDays.add(gemDay);
//        mEventDays.add(jewelDay);
//        mEventDays.add(delhiDay);
//
//        Calendar c19 = Calendar.getInstance();
//        c19.set(2019,9,4);
//
//        Calendar c20 = Calendar.getInstance();
//        c20.set(2019,9,11);
//
//
//
//        Date chennai = c19.getTime();
//        mCalendarView.setDate(chennai);
//        MyEventDay chennaiDay = new MyEventDay(c19, R.drawable.chennaijwellary, "Chennai Jewellery & Gem Fair", "October 4-6-2019 | Chennai","http://artofjewellery.com/IndianEventDetails.aspx?id=10233");
//
//        Date zakex = c20.getTime();
//        mCalendarView.setDate(zakex);
//        MyEventDay zakexDay = new MyEventDay(c20, R.drawable.zakexpo, "Zak Jewels Expo","October 11-13-2019 | Chennai","http://artofjewellery.com/IndianEventDetails.aspx?id=10234");
//
//        mEventDays.add(chennaiDay);
//        mEventDays.add(zakexDay);
//
//        Calendar c21 = Calendar.getInstance();
//        c21.set(2019,11,7);
//
//        Calendar c22 = Calendar.getInstance();
//        c22.set(2019,11,20);
//
//        Calendar c23 = Calendar.getInstance();
//        c23.set(2019,11,20);
//
//        Calendar c24 = Calendar.getInstance();
//        c24.set(2019,11,21);
//
//        Date kerla = c21.getTime();
//        mCalendarView.setDate(kerla);
//        MyEventDay kerlaDay = new MyEventDay(c21, R.drawable.kerlashow, "10th edition of Kerala Gem & Jewellery Show (KGJS)", "December 7-9-2019 | Cochin","http://artofjewellery.com/IndianEventDetails.aspx?id=10235  ");
//
//        Date indiajwell = c22.getTime();
//        mCalendarView.setDate(indiajwell);
//        MyEventDay indiajwellDay = new MyEventDay(c22, R.drawable.indiajwellary, "India Jewellery Show","December 20-22-2019 | Rajkot","http://artofjewellery.com/IndianEventDetails.aspx?id=10236");
//
//        Date jaipur = c23.getTime();
//        mCalendarView.setDate(jaipur);
//        MyEventDay jaipurDay = new MyEventDay(c23, R.drawable.jaipurshow, "Jaipur Jewellery Show","December 20-23-2019 | Jaipur","http://artofjewellery.com/IndianEventDetails.aspx?id=10237");
//
//        Date expo = c24.getTime();
//        mCalendarView.setDate(expo);
//        MyEventDay expoDay = new MyEventDay(c24, R.drawable.jwelnetexpo, "JewelNet Expo","December 21-23-2019 | AKM Resort, Chandigarh","");
//
//        mEventDays.add(kerlaDay);
//        mEventDays.add(indiajwellDay);
//        mEventDays.add(jaipurDay);
//        mEventDays.add(expoDay);
//
//
//        mCalendarView.setEvents(mEventDays);


    }


    private void GetAllEvents(String Month) {

        CalenderEvents.clear();

        String urlString = "http://jewelnet.in/index.php/Api/exhibitions";

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("month", Month)
                .build();

        Request request = new Request.Builder()
                .url(urlString)
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                final String myResponse = responseBody.string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(myResponse);
                            String success = json.getString("response");
                            if (success.equalsIgnoreCase("success")) {
                                JSONArray jsonArray = null;
                                jsonArray = json.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject newJson = jsonArray.getJSONObject(i);
                                    ExhibitionEventDataModel model = new ExhibitionEventDataModel();
                                    model.setEventTitle(newJson.getString("title"));
                                    model.setEventVenue(newJson.getString("venue"));
                                    model.setEventDate(newJson.getString("event_date"));
                                    model.setEventLink(newJson.getString("link"));
                                    model.setEventLogo(newJson.getString("logo"));

                                    CalenderEvents.add(model);


                                    String sDate1 = (newJson.getString("event_date"));
                                    String[] items = sDate1.split("-");
                                    List<String> DateList = new ArrayList<String>(Arrays.asList(items));
                                    int month = Integer.parseInt(DateList.get(1)) - 1;
                                    int date = Integer.parseInt(DateList.get(2));
                                    int year = Integer.parseInt(DateList.get(0));

                                    Calendar c = Calendar.getInstance();
                                    c.set(year, month, date);

                                    Date akshayDay = c.getTime();
                                    mCalendarView.setDate(akshayDay);
                                    MyEventDay akshay = new MyEventDay(c, R.drawable.calender_pointer, (newJson.getString("title")), ((newJson.getString("event_date")) + " \n " + (newJson.getString("venue"))), (newJson.getString("link")), (newJson.getString("logo")));


                                    mEventDays.add(akshay);
                                    mCalendarView.setEvents(mEventDays);


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }


    private void GetAllExhibitions() {

        CalenderEvents.clear();
        String urlString = "http://demo1.geniesoftsystem.com/newweb/jewelnetnewone/index.php/Api/all_exhibitions";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString).get().build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                final String myResponse = responseBody.string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(myResponse);
                            String success = json.getString("response");
                            if (success.equalsIgnoreCase("success")) {
                                JSONArray jsonArray = null;
                                jsonArray = json.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject newJson = jsonArray.getJSONObject(i);
                                    ExhibitionEventDataModel model = new ExhibitionEventDataModel();
                                    model.setEventTitle(newJson.getString("title"));
                                    model.setEventVenue(newJson.getString("venue"));
                                    model.setEventDate(newJson.getString("event_date"));
                                    model.setEventLink(newJson.getString("link"));
                                    model.setEventLogo(newJson.getString("logo"));

                                    CalenderEvents.add(model);


                                    String sDate1 = (newJson.getString("event_date"));
                                    String[] items = sDate1.split("-");
                                    List<String> DateList = new ArrayList<String>(Arrays.asList(items));
                                    int month = Integer.parseInt(DateList.get(1)) - 1;
                                    int date = Integer.parseInt(DateList.get(2));
                                    int year = Integer.parseInt(DateList.get(0));

                                    Calendar c = Calendar.getInstance();
                                    c.set(year, month, date);

                                    Date akshayDay = c.getTime();
                                    mCalendarView.setDate(akshayDay);
                                    MyEventDay akshay = new MyEventDay(c, R.drawable.calender_pointer, (newJson.getString("title")), ((newJson.getString("event_date")) + " \n " + (newJson.getString("venue"))), (newJson.getString("link")), (newJson.getString("logo")));


                                    mEventDays.add(akshay);
                                    mCalendarView.setEvents(mEventDays);


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }

}
