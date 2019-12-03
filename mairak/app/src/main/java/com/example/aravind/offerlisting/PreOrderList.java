package com.example.aravind.offerlisting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.example.aravind.mairak.DashBoard;
import com.example.aravind.mairak.Order;
import com.example.aravind.mairak.Profile;
import com.example.aravind.mairak.ProgressDialog;
import com.example.aravind.mairak.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PreOrderList extends AppCompatActivity {



    TextView tvDate, tvTime, tvOid, tvStatus, tvPrice,tvCode,tvHeader;
    ListView liOrder;
    ImageView imbackarrow;
    String userid,language;
    ProgressDialog progressDialog;
    private String product_id, product_name, quantity1, description, price, special_price, discount_price, images, quantityV;
    Dialog dialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private JSONArray result, object2;
    private String message, statusCode;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();

    Typeface Montserrat_Medium;

    LinearLayout homelay, offerlay, orderlay, profilelay, morelay;
    private String oId;
    private JSONArray object3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order_list);

        initwidgets();


        if (language.equals("a")){


             tvHeader.setText("الطلبات المقدمة");


        }

        preOrderList();

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
                Intent i = new Intent(PreOrderList.this, Order.class);
                startActivity(i);
                finish();
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
                profile();
            }
        });


        imbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        //http://inviewmart.com/mairak_api/Mypreorder.php


    }

    private void preOrderList() {




        String url = "http://inviewmart.com/mairak_api/Mypreorder.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        try {

                            JSONObject response1 = new JSONObject(response);

                            result = new JSONArray();
                            result = response1.getJSONArray("Response");
                           // System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");


                            if (statusCode.equals("1")) {


                                object2 = new JSONArray();
                                object2 = result.getJSONArray(1);


                                int i;

                                for (i = 0; i < object2.length(); i++) {

                                    arrylist.add(object2.getJSONObject(i));

                                }


                                if (arrylist.isEmpty())
                                    Toast.makeText(getApplicationContext(), "No Order Found", Toast.LENGTH_SHORT).show();

                                OrderListAdapter adapter = new OrderListAdapter(getApplicationContext(), arrylist);
                                liOrder.setAdapter(adapter);
                                adapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //  Log.d("Error.Response", response);
                      //  System.out.println(error);
                        showAlert();
                      //  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);

                Log.d("params11", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);




    }






    public class OrderListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<JSONObject> settings;
        String status;
        JSONObject object;

        public OrderListAdapter(Context context, ArrayList<JSONObject> settings) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.settings = settings;
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
                v = inflater.inflate(R.layout.preorder_row, null);

            try {

                Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


                tvDate = v.findViewById(R.id.tvDate);
                tvTime = v.findViewById(R.id.tvTime);
                tvOid = v.findViewById(R.id.tvOid);
                tvStatus = v.findViewById(R.id.tvStatus);
                tvPrice = v.findViewById(R.id.tvPrice);
                tvCode = v.findViewById(R.id.tvCode);


                tvStatus.setTypeface(Montserrat_Medium);
                tvOid.setTypeface(Montserrat_Medium);
                tvTime.setTypeface(Montserrat_Medium);
                tvDate.setTypeface(Montserrat_Medium);
                tvPrice.setTypeface(Montserrat_Medium);
                tvCode.setTypeface(Montserrat_Medium);


                tvCode.setVisibility(View.GONE);


                tvTime.setText(settings.get(position).getString("preorder_time"));
                tvStatus.setText(settings.get(position).getString("delivered_status"));
                tvOid.setText(settings.get(position).getString("order_code"));
                tvDate.setText(settings.get(position).getString("preorder_date"));
                tvPrice.setText("AED. "+settings.get(position).getString("total_price"));


                //   System.out.println("Priceeeee"+ settings.get(position).getString("total_price"));




               /* "order_id": "129",
                        "order_code": "100118",
                        "delivered_status": "Ordered",
                        "total_price": "30",
                        "preorder_date": "7-9-2018",
                        "preorder_time": "7:13 PM",
                        "created_date": "05-09-2018",
                        "created_time": "17:14 PM"*/





            } catch (JSONException e) {
                e.printStackTrace();
            }


            return v;
        }
    }









    private void initwidgets() {

        getSupportActionBar().hide();
        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);


        language = pref.getString("language", "e");






        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        tvHeader = findViewById(R.id.tvHeader);

        tvHeader.setTypeface(Montserrat_Medium);

        imbackarrow = findViewById(R.id.imbackarrow);
        liOrder = findViewById(R.id.liOrder);


        homelay = findViewById(R.id.homeLay);
        offerlay = findViewById(R.id.offerLay);
        orderlay = findViewById(R.id.orderLay);
        profilelay = findViewById(R.id.profileLay);
        morelay = findViewById(R.id.moreLay);

      //  offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));



    }








    public void homel() {


        Intent intent = new Intent(getApplicationContext(), DashBoard.class);
        startActivity(intent);
        finish();


    }


    public void offer() {

        Intent i = new Intent(getApplicationContext(), NewOfferlisting.class);
        startActivity(i);
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





    private void showAlert(){
        final Dialog dialog = new Dialog(PreOrderList.this);
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



}
