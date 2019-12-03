package com.mairak.bgi.mairak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mairak.bgi.offerlisting.NewOfferlisting;
import com.mairak.bgi.offerlisting.OfferOrderListing;
import com.mairak.bgi.util.DatabaseHelper;
import com.mairak.bgi.util.SwipeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationList extends AppCompatActivity {


    LinearLayout homelay, offerlay, orderlay, profilelay, morelay;
    ImageView imbackarrow;
    DatabaseHelper Dhb;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrylist2 = new ArrayList<JSONObject>();
    ListView NotificationList;
    TextView tvWarn;
    TextView tvNotifiHead;
    JSONObject result, object2;
    JSONArray result2, object3;
    private Typeface Montserrat_Medium;
    private ArrayList<JSONObject> arrylist1;
    String readCheck = "ur";
    private SwipeDetector swipeDetector;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid, language;

    String message, statusCode, prodID, imagefield, totalUnread = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        initwidgets();

        getNotifications();

        // inflateNotification();

       // getBadgeCount();

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

    private void getBadgeCount() {
        String url = "http://inviewmart.com/mairak_api/Pushnotification.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject response1 = new JSONObject(response);
                            result = new JSONObject();
                            result = response1.getJSONObject("Response");
                            totalUnread = result.getString("unread_count");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "No Network! You are working offline now ", Toast.LENGTH_SHORT).show();
                    }})
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");
                Log.d("params", params.toString());
                return params;
            }};
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }



    private void getNotifications() {

        {


            String url = "http://inviewmart.com/mairak_api/Pushnotification.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            // Log.d("Response", response);

                            try {

                                arrylist.clear();
                                JSONObject response1 = new JSONObject(response);

                                result = new JSONObject();
                                result = response1.getJSONObject("Response");
                                System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                                JSONObject object = new JSONObject();

                                //     object = result.getJSONArray(0).getJSONObject(0);

                                message = result.getString("message");
                                statusCode = result.getString("status");
                                totalUnread = result.getString("unread_count");

                                if (statusCode.equals("1")) {

                                    result2 = new JSONArray();
                                    result2 = response1.getJSONArray("Notification");
                                    //      object2 = new JSONArray();
                                    System.out.println("Nottiiiiiii" + result2);

                                    for (int i = 0; i < result2.length(); i++) {
                                        arrylist.add(result2.getJSONObject(i));
                                    }

                                 getNotifivcationBadge();

                                    if (arrylist.size() > 0) {
                                        NotificationList.setVisibility(View.VISIBLE);
                                        tvWarn.setVisibility(View.GONE);

                                    } else {

                                        NotificationList.setVisibility(View.GONE);
                                        tvWarn.setVisibility(View.VISIBLE);
                                    }


                                    NotificationListAdapter adapter = new NotificationListAdapter(NotificationList.this, arrylist);
                                    NotificationList.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    adapter.notifyDataSetInvalidated();


                                } else {
                                    if (arrylist.size() > 0) {
                                        NotificationList.setVisibility(View.VISIBLE);
                                        tvWarn.setVisibility(View.GONE);

                                    } else {

                                        NotificationList.setVisibility(View.GONE);
                                        tvWarn.setVisibility(View.VISIBLE);
                                    }
                                  getNotifivcationBadge();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            //    System.out.println(error);

                            Toast.makeText(getApplicationContext(), "No Network! You are working offline now ", Toast.LENGTH_SHORT).show();


                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", userid);
                    params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");
                    Log.d("params", params.toString());


                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);


        }


    }

/*    private void inflateNotification() {

        arrylist.clear();


        arrylist = (ArrayList<JSONObject>) Dhb.getNotificationList().clone();


        if (arrylist.size() > 0) {
            NotificationList.setVisibility(View.VISIBLE);
            tvWarn.setVisibility(View.GONE);

        } else {

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


                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR) {

                        Dhb.deleteFromNotificationList(arrylist.get(position).optString("id"));
                        //  getNotifivcationBadge();
                        inflateNotification();

                    }

                }


            }
        });


    }*/

    private void initwidgets() {

        Dhb = new DatabaseHelper(NotificationList.this);
        homelay = findViewById(R.id.homeLay);
        offerlay = findViewById(R.id.offerLay);
        orderlay = findViewById(R.id.orderLay);
        profilelay = findViewById(R.id.profileLay);
        morelay = findViewById(R.id.moreLay);
        tvWarn = findViewById(R.id.tvWarn);
        tvNotifiHead = findViewById(R.id.tvNotifiHead);
        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);
        language = pref.getString("language", "e");


        tvWarn.setTypeface(Montserrat_Medium);
        tvNotifiHead.setTypeface(Montserrat_Medium);

        if (language.equals("a")) {

            tvWarn.setText("لا يوجد إشعارات – لا إخطار");
            tvNotifiHead.setText("إعلام");

        }

        NotificationList = findViewById(R.id.NotificationList);

        imbackarrow = findViewById(R.id.imbackarrow);


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

        Intent intent = new Intent(getApplicationContext(), OfferOrderListing.class);
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

            System.out.println("arrattatanot" + settings);

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

         final String iD = arrylist.get(position).optString("id");

            if (settings.get(position).optString("read_status").equals("1")){

                mainCard.setCardBackgroundColor(Color.parseColor("#797979"));
                openIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drafts_black_24dp));


            }else {

                mainCard.setCardBackgroundColor(Color.parseColor("#202020"));
                openIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_black_24dp));

            }

            deleteNot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteNotification(settings.get(position).optString("id"));
                    //    Dhb.deleteFromNotificationList(settings.get(position).optString("id"));
                    getNotifications();
                    getNotifivcationBadge();

                    //   inflateNotification();

                }
            });


            imExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainCard.setCardBackgroundColor(Color.parseColor("#797979"));
                    openIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_drafts_black_24dp));

                    //    readCheck="r";
                    //    Dhb.updateNotification(settings.get(position).optString("id"),readCheck);


              updateNotStat(iD);


                    // getNotifications();
                 getNotifivcationBadge();

                    if (messageLay.getVisibility() == View.VISIBLE) {

                        messageLay.setVisibility(View.GONE);
                        imExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp));

                    } else {


                        messageLay.setVisibility(View.VISIBLE);
                        imExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));

                    }


                }
            });


            return v;
        }
    }

    private void updateNotStat(final String id) {


        String url = "http://inviewmart.com/mairak_api/Notification_read.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        // Log.d("Response", response);

                        try {

                            JSONObject response1 = new JSONObject(response);

                            result = new JSONObject();
                            result = response1.getJSONObject("Response");
                            //    System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            message = result.getString("message");
                            statusCode = result.getString("status");


                            //      Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            getNotifivcationBadge();
                         //   getNotifications();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //    System.out.println(error);

                        Toast.makeText(getApplicationContext(), "No Network! You are working offline now ", Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("notification_id", id);
                params.put("read_status", "1");
                Log.d("params", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }

    private void deleteNotification(final String id) {


        String url = "http://inviewmart.com/mairak_api/Pushnotification_delete.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        // Log.d("Response", response);

                        try {

                            JSONObject response1 = new JSONObject(response);

                            result = new JSONObject();
                            result = response1.getJSONObject("Response");
                            //    System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            message = result.getString("message");
                            statusCode = result.getString("status");


                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            getNotifications();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //    System.out.println(error);

                        Toast.makeText(getApplicationContext(), "No Network! You are working offline now ", Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("notification_id", id);
                Log.d("params", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }


    private void getNotifivcationBadge() {

       getBadgeCount();

        LinearLayout badge = findViewById(R.id.badge);
        TextView notification_count = findViewById(R.id.notification_count);

//        badge.setVisibility(View.GONE);
//        notification_count.setVisibility(View.GONE);
//
        if (totalUnread.equals("0")) {
            badge.setVisibility(View.GONE);
            notification_count.setVisibility(View.GONE);
        } else {
            badge.setVisibility(View.VISIBLE);
            notification_count.setVisibility(View.VISIBLE);
            notification_count.setText(totalUnread);
        }

  /*      int count=0;
//        arrylist1 = (ArrayList<JSONObject>)Dhb.getNotificationList().clone();
       count = Dhb.getNotifiCount();

        if (arrylist.size()==0){
            badge.setVisibility(View.GONE);
            notification_count.setVisibility(View.GONE);
        }else {
            badge.setVisibility(View.VISIBLE);
            notification_count.setVisibility(View.VISIBLE);
            notification_count.setText(String.valueOf(arrylist.size()));
        }
*/

    }

}
