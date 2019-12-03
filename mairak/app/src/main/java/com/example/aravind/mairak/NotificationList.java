package com.example.aravind.mairak;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aravind.offerlisting.NewOfferlisting;
import com.example.aravind.offerlisting.OfferOrderListing;
import com.example.aravind.util.DatabaseHelper;
import com.example.aravind.util.OnSwipeTouchListener;
import com.example.aravind.util.SwipeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationList extends AppCompatActivity {


    LinearLayout homelay, offerlay, orderlay, profilelay, morelay;
    ImageView imbackarrow;
    DatabaseHelper Dhb;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrylist2 = new ArrayList<JSONObject>();
    ListView NotificationList;
    TextView tvWarn;
    private Typeface Montserrat_Medium;
    private ArrayList<JSONObject> arrylist1;
    String readCheck = "ur";
    private SwipeDetector swipeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        initwidgets();

        inflateNotification();


        getNotifivcationBadge();

        homelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                orderlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                 homel();


            }
        });

        offerlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                orderlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                 offer();
            }
        });

        orderlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderlay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                order();
            }
        });

        morelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                orderlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                 more();
            }
        });

        profilelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                orderlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
        });


        imbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void inflateNotification() {

        arrylist.clear();


        arrylist = (ArrayList<JSONObject>)Dhb.getNotificationList().clone();





        if (arrylist.size()>0){
            NotificationList.setVisibility(View.VISIBLE);
            tvWarn.setVisibility(View.GONE);

        }else {

            NotificationList.setVisibility(View.GONE);
            tvWarn.setVisibility(View.VISIBLE);
        }

        Collections.reverse(arrylist);


        NotificationListAdapter adapter = new NotificationListAdapter(NotificationList.this, arrylist);
        NotificationList.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        swipeDetector = new SwipeDetector();

        NotificationList.setOnTouchListener(swipeDetector);



        NotificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR) {

                            Dhb.deleteFromNotificationList(arrylist.get(position).optString("id"));
                            getNotifivcationBadge();
                            inflateNotification();

                    }

                }






            }
        });


    }

    private void initwidgets() {

        Dhb = new DatabaseHelper(NotificationList.this);
        homelay = findViewById(R.id.homeLay);
        offerlay= findViewById(R.id.offerLay);
        orderlay= findViewById(R.id.orderLay);
        profilelay= findViewById(R.id.profileLay);
        morelay= findViewById(R.id.moreLay);
        tvWarn=findViewById(R.id.tvWarn);


        NotificationList = findViewById(R.id.NotificationList);

        imbackarrow=findViewById(R.id.imbackarrow);



    }





    public void homel() {


        Intent intent = new Intent(getApplicationContext(), DashBoard.class);
        startActivity(intent);
        finish();


    }

    public void order() {


        Intent intent = new Intent(getApplicationContext(), Order.class);
        startActivity(intent);
        finish();


    }


    public void offer() {
        Intent offerintent = new Intent(getApplicationContext(), NewOfferlisting.class);
        startActivity(offerintent);
        finish();


    }


    public void profile() {


        Intent intent = new Intent(getApplicationContext(), Profile.class);
        startActivity(intent);
        finish();


    }


    public void more() {

        Intent intent = new Intent(getApplicationContext(),OfferOrderListing.class);
        startActivity(intent);
        finish();



    }


    @Override
    protected void onResume() {
        super.onResume();
        getNotifivcationBadge();
    }

    public class NotificationListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<JSONObject> settings;
        String url;

        public NotificationListAdapter(Context context, ArrayList<JSONObject> settings) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.settings = settings;

            System.out.println("arrattatanot"+ settings);

        }

        @Override
        public int getCount() {

            return settings.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null)
                v = inflater.inflate(R.layout.notification_list, null);



            Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


            TextView tvTitle = v.findViewById(R.id.tvTitle);
            TextView tvMessage = v.findViewById(R.id.tvMessage);
            ImageView deleteNot = v.findViewById(R.id.deleteNot);
            final ImageView openIcon = v.findViewById(R.id.openIcon);
            final CardView mainCard = v.findViewById(R.id.mainCard);
            final ImageView imExpand = v.findViewById(R.id.imExpand);

            LinearLayout mainLay = v.findViewById(R.id.mainLay);
            final LinearLayout messageLay = v.findViewById(R.id.messageLay);

            tvTitle.setTypeface(Montserrat_Medium);
            tvMessage.setTypeface(Montserrat_Medium);




            tvTitle.setText(settings.get(position).optString("title"));
            tvMessage.setText(settings.get(position).optString("message"));

            if (settings.get(position).optString("read").equals("r")){

                mainCard.setCardBackgroundColor(Color.parseColor("#797979"));
                openIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drafts_black_24dp));


            }else {

                mainCard.setCardBackgroundColor(Color.parseColor("#202020"));
                openIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp));

            }

            deleteNot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dhb.deleteFromNotificationList(settings.get(position).optString("id"));
                    getNotifivcationBadge();
                    inflateNotification();

                }
            });


            imExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainCard.setCardBackgroundColor(Color.parseColor("#797979"));
                    openIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drafts_black_24dp));

                    readCheck="r";
                    Dhb.updateNotification(settings.get(position).optString("id"),readCheck);


                    getNotifivcationBadge();

                    if (messageLay.getVisibility()==View.VISIBLE){

                        messageLay.setVisibility(View.GONE);
                        imExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp));

                    }else {


                        messageLay.setVisibility(View.VISIBLE);
                        imExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));

                    }



                }
            });






            return v;
        }
    }



    private void getNotifivcationBadge() {

        LinearLayout badge = findViewById(R.id.badge);
        TextView notification_count = findViewById(R.id.notification_count);

        int count=0;
//        arrylist1 = (ArrayList<JSONObject>)Dhb.getNotificationList().clone();
        count = Dhb.getNotifiCount();

        if (count==0){
            badge.setVisibility(View.GONE);
            notification_count.setVisibility(View.GONE);
        }else {
            badge.setVisibility(View.VISIBLE);
            notification_count.setVisibility(View.VISIBLE);
            notification_count.setText(String.valueOf(count));
        }


    }

}
