package com.mairak.bgi.offerlisting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mairak.bgi.mairak.NotificationList;
import com.mairak.bgi.offerlisting.Adapter.OfferAdapter;
import com.mairak.bgi.offerlisting.Model.OfferModel;
import com.mairak.bgi.mairak.DashBoard;
import com.mairak.bgi.mairak.Order;
import com.mairak.bgi.mairak.ProgressDialog;
import com.mairak.bgi.mairak.R;
import com.mairak.bgi.util.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewOfferlisting extends AppCompatActivity {
    RecyclerView recyclerView;
    OfferAdapter offerAdapter;
    ArrayList<OfferModel> models = new ArrayList<OfferModel>();
    private int counter = 0;
    private Typeface Montserrat_Medium;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private JSONArray result, object2;
    private JSONObject resultN;
    private String message, statusCode;
    String userid;
    LinearLayout order, homelay, profilelay, morelay, offerlay;
    ImageView back, list;
    TextView tvHeader;
    String language;
    private ProgressDialog progressDialog;
    private ArrayList<JSONObject> arrylist1;
    DatabaseHelper Dhb;
    private String totalUnread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offerlisting);



        initwidgets();
        setrecycler();

        getNotBadgeCount();
   //   getNotifivcationBadge();

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderintrent = new Intent(NewOfferlisting.this, OfferOrderListing.class);
                startActivity(orderintrent);
                finish();
            }
        });


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                order.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                Intent orderintrent = new Intent(NewOfferlisting.this, Order.class);
                startActivity(orderintrent);
                finish();
            }
        });


        homelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                order.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                Intent i = new Intent(NewOfferlisting.this, DashBoard.class);
                startActivity(i);
                finish();


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        offerlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                order.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


            }
        });


        morelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                order.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
                order.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                Intent i = new Intent(NewOfferlisting.this, NotificationList.class);
                startActivity(i);
                finish();

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        order.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    public void more() {

        Intent intent = new Intent(getApplicationContext(),OfferOrderListing.class);
        startActivity(intent);



    }

    private void setrecycler() {
        models.clear();
        Offerlisting();
    }

    private void Offerlisting() {

        final Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(NewOfferlisting.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }
        final boolean finalIsshow = isshow;


        String url = "http://inviewmart.com/mairak_api/Offer.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        if (finalIsshow){

                            progressDialog.dismiss();
                        }

                        try {

                            JSONObject response1 = new JSONObject(response);

                            result = new JSONArray();
                            result = response1.getJSONArray("Response");
                         //   System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");


                            if (statusCode.equals("1")) {


                                object2 = new JSONArray();
                                object2 = result.getJSONArray(1);


                                if (object2.length() != 0) {

                                    for (int i = 0; i < object2.length(); i++) {
                                        String image = "";
                                        JSONObject innerobject = object2.getJSONObject(i);
                                        String id = innerobject.optString("id");
                                        String heading = innerobject.optString("heading");
                                        String description = innerobject.optString("description");
                                        String special_price = innerobject.optString("special_price");
                                        String quantity = innerobject.optString("quantity");
                                        String offer_start_date = innerobject.optString("offer_start_date");
                                        String offer_last_date = innerobject.optString("offer_last_date");
                                        JSONArray imagearray = new JSONArray();
                                        imagearray = innerobject.getJSONArray("images");
                                      //  System.out.println("arrrrrr" + imagearray);
                                        if (imagearray.length() != 0) {
//                                            for (int j = 0; j < imagearray.length(); i++) {


                                            image = imagearray.getString(0);


//                                            }


                                        }

                                        models.add(new OfferModel(id, heading, description, special_price, quantity, offer_start_date, offer_last_date, image, Montserrat_Medium));
                                    }
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(layoutManager);
                                    offerAdapter = new OfferAdapter(NewOfferlisting.this, models, getApplicationContext());
                                    recyclerView.setAdapter(offerAdapter);
                                    counter = recyclerView.getAdapter().getItemCount();
                                } else {
                                    Toast.makeText(getApplicationContext(), "No Data to display", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                          //  System.out.println("eccce0" + e);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //  Log.d("Error.Response", response);

                        showAlert();
                      //  System.out.println(error);
                      //  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                params.put("language",language);
                params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }

    private void initwidgets() {
        back = (ImageView) findViewById(R.id.back);
        list = (ImageView) findViewById(R.id.list);
        getSupportActionBar().hide();
        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();
//        order = (LinearLayout) findViewById(R.id.order);

        Dhb = new DatabaseHelper(NewOfferlisting.this);

        userid = pref.getString("userid", "0");
        language = pref.getString("language","e");


        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setTypeface(Montserrat_Medium);

        if (language.equals("a")){
            tvHeader.setText("العروض");
        }



        homelay = findViewById(R.id.homeLay);
        offerlay = findViewById(R.id.offerLay);
        order = findViewById(R.id.orderLay);
        profilelay = findViewById(R.id.profileLay);
        morelay = findViewById(R.id.moreLay);

//        order.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


    }



    private void showAlert(){
        final Dialog dialog = new Dialog(NewOfferlisting.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView retry = (TextView) dialog.findViewById(R.id.retry);
        final TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }







    private void getNotBadgeCount() {
        String url = "http://inviewmart.com/mairak_api/Pushnotification.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject response1 = new JSONObject(response);
                            resultN = new JSONObject();
                            resultN = response1.getJSONObject("Response");
                            totalUnread = resultN.getString("unread_count");
                            getNotifivcationBadge();

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

    private void getNotifivcationBadge() {

        LinearLayout badge = findViewById(R.id.badge);
        TextView notification_count = findViewById(R.id.notification_count);

        badge.setVisibility(View.GONE);
        notification_count.setVisibility(View.GONE);

        if (totalUnread.equals("0")) {
            badge.setVisibility(View.GONE);
            notification_count.setVisibility(View.GONE);
        } else {
            badge.setVisibility(View.VISIBLE);
            notification_count.setVisibility(View.VISIBLE);
            notification_count.setText(totalUnread);
        }

/*
        int count=0;
        count = Dhb.getNotifiCount();

        if (count==0){
            badge.setVisibility(View.GONE);
            notification_count.setVisibility(View.GONE);
        }else {
            badge.setVisibility(View.VISIBLE);
            notification_count.setVisibility(View.VISIBLE);
            notification_count.setText(String.valueOf(count));
        }
*/


    }



}
