package com.bgi.mairak.offerlisting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.bgi.mairak.mairak.DashBoard;
import com.bgi.mairak.mairak.NotificationList;
import com.bgi.mairak.mairak.Order;
import com.bgi.mairak.mairak.ProgressDialog;
import com.bgi.mairak.mairak.R;
import com.bgi.mairak.util.DatabaseHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OfferOrderListing extends AppCompatActivity {

    TextView tvDate, tvTime, tvOid, tvStatus, tvPrice,tvCode,tvHeader;
    ListView liOrder;
    ImageView imbackarrow;
    String userid;
    ProgressDialog progressDialog;
    private String product_id, product_name, quantity1, description, price, special_price, discount_price, images, quantityV;
    Dialog dialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private JSONArray result, object2;
    private String message, statusCode;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();

    Typeface Montserrat_Medium;
    String language;

    LinearLayout homelay, offerlay, orderlay, profilelay, morelay;
    private String oId;
    private JSONArray object3;
    private ArrayList<JSONObject> arrylist1;
    DatabaseHelper Dhb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);

        initwidgets();


        order();

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
                Intent i = new Intent(OfferOrderListing.this, Order.class);
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


    }
    @Override
    protected void onResume() {
        super.onResume();

        morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        orderlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void initwidgets() {

        getSupportActionBar().hide();
        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);

        language = pref.getString("language", "e");

        Dhb = new DatabaseHelper(OfferOrderListing.this);

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        tvHeader = findViewById(R.id.tvHeader);

        tvHeader.setTypeface(Montserrat_Medium);

        if (language.equals("a")){

            tvHeader.setText("عرض الطلبات");
        }


        imbackarrow = findViewById(R.id.imbackarrow);
        liOrder = findViewById(R.id.liOrder);


        homelay = findViewById(R.id.homeLay);
        offerlay = findViewById(R.id.offerLay);
        orderlay = findViewById(R.id.orderLay);
        profilelay = findViewById(R.id.profileLay);
        morelay = findViewById(R.id.moreLay);

        offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


    }


    private void order() {

        Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(OfferOrderListing.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }
        final boolean finalIsshow = isshow;

        String url = "http://inviewmart.com/mairak_api/Myorder_offer.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                      //  Log.d("Response", response);

                        if (finalIsshow){
                            progressDialog.dismiss();

                        }

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


                            liOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    try {


                                     //   System.out.println("Position++++ " + position);
                                        oId = object2.getJSONObject(position).getString("order_id");

                                     //   System.out.println("Position++++ " + position);

                                        getDetails(oId);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                      //  System.out.println("Exceptionnnnn///" + e);
                                    }


                                }
                            });


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
                        showAlert();
                     //   System.out.println(error);
                     //   Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");

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
                v = inflater.inflate(R.layout.order_rowone, null);

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


                if (language.equals("a")){

                    tvCode.setText("رقم الطلب");



                }



                tvTime.setText(settings.get(position).getString("created_time"));
                tvStatus.setText(settings.get(position).getString("delivered_status"));
                tvOid.setText("ID " + settings.get(position).getString("order_id"));
                tvDate.setText(settings.get(position).getString("created_date"));
                tvPrice.setText(settings.get(position).getString("order_code"));


                //   System.out.println("Priceeeee"+ settings.get(position).getString("total_price"));


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return v;
        }
    }


    private void getDetails(final String order_id) {


        Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(OfferOrderListing.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }
        final boolean finalIsshow = isshow;


        String url = "http://inviewmart.com/mairak_api/Myorder_details_offer.php";
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
                          //  System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");


                            if (statusCode.equals("1")) {


                                object3 = new JSONArray();
                                object3 = result.getJSONArray(1);


                                int i;

                                for (i = 0; i < object3.length(); i++) {

                                    product_name = object3.getJSONObject(i).getString("heading");
                                    quantity1 = object3.getJSONObject(i).getString("quantity");
                                    description = object3.getJSONObject(i).getString("description");
                                    //   product_id= object3.getJSONObject(i).getString("product_id");
                                    price = object3.getJSONObject(i).getString("price");
                                    special_price = object3.getJSONObject(i).getString("order_id");
                                    discount_price = object3.getJSONObject(i).getString("offer_id");

//                                    images = object3.getJSONObject(i).getString("images");

                                    JSONArray imagearray = new JSONArray();
                                    imagearray = object3.getJSONObject(i).getJSONArray("images");
                                  //  System.out.println("arrrrrr" + imagearray);
                                    if (imagearray.length() != 0) {
//                                            for (int j = 0; j < imagearray.length(); i++) {


                                        images = imagearray.getString(0);


//                                            }


                                    }


                                  //  System.out.println("VAluesssss1111" + product_name + " " + description);

                                   // System.out.println("Quanttttttt" + quantityV);
                                }


                                if (arrylist.isEmpty())
                                    Toast.makeText(getApplicationContext(), "No Order Found", Toast.LENGTH_SHORT).show();

                                showDetail(special_price);


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
                       // System.out.println(error);
                        showAlert();
                       // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", order_id);
                params.put("language",language);
                params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");
                Log.d("params", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }

    private void showDetail(final String offerid) {

        dialog = new Dialog(OfferOrderListing.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_order_details);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


        TextView prodName1, prodDiscription, aedHead, saedHead, daedHead, aed, aedValue, saed, saedValue, daed, daedValue, quantity, quantityValue, reorder;

        ImageView close_btn = dialog.findViewById(R.id.close_btn);
        ImageView productImg = dialog.findViewById(R.id.productImg);

        prodName1 = dialog.findViewById(R.id.prodName1);
        prodDiscription = dialog.findViewById(R.id.prodDiscription);
        aedHead = dialog.findViewById(R.id.aedHead);
        saedHead = dialog.findViewById(R.id.saedHead);
        daedHead = dialog.findViewById(R.id.daedHead);
        aed = dialog.findViewById(R.id.aed);
        aedValue = dialog.findViewById(R.id.aedValue);
        saed = dialog.findViewById(R.id.saed);
        saedValue = dialog.findViewById(R.id.saedValue);
        daed = dialog.findViewById(R.id.daed);
        daedValue = dialog.findViewById(R.id.daedValue);
        quantity = dialog.findViewById(R.id.quantity);
        quantityValue = dialog.findViewById(R.id.quantityValue);
       // reorder = dialog.findViewById(R.id.reorder);


        Picasso.with(OfferOrderListing.this).load(images).placeholder(R.drawable.can).error(R.drawable.can).into(productImg);


        prodName1.setTypeface(Montserrat_Medium);
        prodDiscription.setTypeface(Montserrat_Medium);
        aedHead.setTypeface(Montserrat_Medium);
        saedHead.setTypeface(Montserrat_Medium);
        daedHead.setTypeface(Montserrat_Medium);
        aed.setTypeface(Montserrat_Medium);
        aedValue.setTypeface(Montserrat_Medium);
        saed.setTypeface(Montserrat_Medium);
        saedValue.setTypeface(Montserrat_Medium);
        daed.setTypeface(Montserrat_Medium);
        daedValue.setTypeface(Montserrat_Medium);
        quantity.setTypeface(Montserrat_Medium);
        quantityValue.setTypeface(Montserrat_Medium);
       // reorder.setTypeface(Montserrat_Medium);

      //  System.out.println("VAluesssss" + product_name + " " + description);

        prodName1.setText(product_name);
        prodDiscription.setText(description);
        aedValue.setText(price);
        saedValue.setText(special_price);
        daedValue.setText(this.discount_price);
        quantityValue.setText(quantity1);


        if (language.equals("a")){

          /*  aedHead.setText("");
            daedHead.setText("");
            saedHead.setText("");*/
          quantity.setText("الجودة");

        }else {

            daedHead.setText("Offer ID");
            saedHead.setText("Order ID");


        }





        saed.setVisibility(View.GONE);
        daed.setVisibility(View.GONE);


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

      /*  reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  getreorder(offerid);
            }
        });*/
    }

    private void getreorder(final String product_id) {
        progressDialog = new ProgressDialog(OfferOrderListing.this);
        progressDialog.show();
        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();
        userid = pref.getString("userid", "0");

        String url = "http://inviewmart.com/mairak_api/Offer_reorder.php";
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
                          //  System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");
                            dialog.dismiss();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(OfferOrderListing.this, OfferOrderListing.class);
                            startActivity(i);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                           // System.out.println("eccce0" + e);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //  Log.d("Error.Response", response);
                       // System.out.println(error);
                        showAlert();
                      //  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                params.put("order_id", product_id);

                params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(postRequest);


    }

    public void homel() {


        Intent intent = new Intent(getApplicationContext(), DashBoard.class);
        startActivity(intent);
        finish();


    }


    public void offer() {

        Intent i = new Intent(OfferOrderListing.this, NewOfferlisting.class);
        startActivity(i);
        finish();
    }


    public void profile() {


        Intent intent = new Intent(getApplicationContext(), NotificationList.class);
        startActivity(intent);
        finish();


    }


    public void more() {


    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void showAlert(){
        final Dialog dialog = new Dialog(OfferOrderListing.this);
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


    private void getNotifivcationBadge() {

        LinearLayout badge = findViewById(R.id.badge);
        TextView notification_count = findViewById(R.id.notification_count);

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


    }


}
