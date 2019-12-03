package com.example.aravind.mairak;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
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
import com.example.aravind.offerlisting.NewOfferlisting;
import com.example.aravind.offerlisting.OfferOrderListing;
import com.example.aravind.util.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Order extends AppCompatActivity {

    TextView tvDate, tvTime, tvOid, tvStatus, tvPrice,tvHeader;
    ListView liOrder;
    ImageView imbackarrow;
    String userid;
    Dialog dialog;
    private String product_id, product_name, quantity1, description, price, special_price, discount_price, images, quantityV;
    private ProgressDialog progressDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private JSONArray result, object2;
    private String message, statusCode;

    private String startDate ="",endDate ="";

    String language ;

    FloatingActionButton fabDateFilter;

    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();
    ArrayList<JSONObject> arrylist2 = new ArrayList<JSONObject>();

    Typeface Montserrat_Medium;

    LinearLayout homelay, offerlay, orderlay, profilelay, morelay;
    private String oId;
    private JSONArray object3;
    private String nOID;
    private ArrayList<JSONObject> arrylist1;
    DatabaseHelper Dhb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order);


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


        fabDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               filterAlert();



            }
        });

        imbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }




    private void initwidgets() {


        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);

        language = pref.getString("language", "e");

        imbackarrow = findViewById(R.id.imbackarrow);
        liOrder = findViewById(R.id.liOrder);

        Dhb = new DatabaseHelper(Order.this);

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        homelay = findViewById(R.id.homeLay);
        offerlay = findViewById(R.id.offerLay);
        orderlay = findViewById(R.id.orderLay);
        profilelay = findViewById(R.id.profileLay);
        morelay = findViewById(R.id.moreLay);

        tvHeader = findViewById(R.id.tvHeader);



        if (language.equals("a")){
            tvHeader.setText("أوامر");
        }


        tvHeader.setTypeface(Montserrat_Medium);


        fabDateFilter = findViewById(R.id.fabDateFilter);



        orderlay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


    }


    private void order() {

        arrylist.clear();

        boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(Order.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }
        final boolean finalIsshow = isshow;

        String url = "http://inviewmart.com/mairak_api/Myorder.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        if(finalIsshow) {
                            progressDialog.dismiss();
                        }

                        try {



                            arrylist.clear();

                            JSONObject response1 = new JSONObject(response);

                            result = new JSONArray();
                            result = response1.getJSONArray("Response");
                     //       System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

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

                                Collections.reverse(arrylist);

                                OrderListAdapter adapter = new OrderListAdapter(getApplicationContext(), arrylist);
                                liOrder.setAdapter(adapter);
                                adapter.notifyDataSetChanged();


                            } else {
                                arrylist.clear();
                                OrderListAdapter adapter = new OrderListAdapter(getApplicationContext(), arrylist);
                                liOrder.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            }

                            liOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    try {



                                        oId = arrylist.get(position).getString("order_id");

                                      System.out.println("Position++++ " + position);
                                      System.out.println("Position++++1 " + oId);

                                        getDetails(oId);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                           //             System.out.println("Exceptionnnnn///" + e);
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
                        progressDialog.dismiss();
                        showAlert();
                  //      System.out.println(error);
                      //  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                params.put("start_date", startDate);
                params.put("end_date", endDate);
                Log.d("params", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }

    private void getDetails(final String order_id) {


        Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(Order.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }
        final boolean finalIsshow = isshow;

        arrylist2.clear();

        String url = "http://inviewmart.com/mairak_api/Myorder_detail.php";
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
                            arrylist2.clear();

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



                                    arrylist2.add(object3.getJSONObject(i));

                                    product_name = object3.getJSONObject(i).getString("product_name");
                                    quantity1 = object3.getJSONObject(i).getString("quantity");
                                    description = object3.getJSONObject(i).getString("description");
                                    product_id = object3.getJSONObject(i).getString("product_id");
                                    price = object3.getJSONObject(i).getString("price");
                                    special_price = object3.getJSONObject(i).getString("special_price");
                                    discount_price = object3.getJSONObject(i).getString("discount_price");
                                    images = object3.getJSONObject(i).getString("images");

                                   // System.out.println("VAluesssss1111" + product_name + " " + description);

                                  //  System.out.println("Quanttttttt" + quantityV);
                                }


                        //        System.out.println( "Arraaayyyy333 " + arrylist2);


                                if (arrylist2.isEmpty())
                                    Toast.makeText(getApplicationContext(), "No Order Found", Toast.LENGTH_SHORT).show();

                              showDetail(arrylist2,order_id);


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
                  //      System.out.println(error);
                        showAlert();
                        //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", order_id);
                params.put("language",language);
                Log.d("paramsDD", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }

   /* private void showDetail(final String product_id) {

        dialog = new Dialog(Order.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_order_details);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


        TextView prodName1, prodDiscription, aedHead, saedHead, daedHead, aed, aedValue, saed, saedValue, daed, daedValue, quantity, quantityValue;

        ImageView close_btn = dialog.findViewById(R.id.close_btn);
        ImageView productImg = dialog.findViewById(R.id.productImg);
        TextView reorder = dialog.findViewById(R.id.reorder);
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
        reorder.setTypeface(Montserrat_Medium);

        if (language.equals("a")){
            quantity.setText("الجودة");
            aedHead.setText("السعر");
            saedHead.setText("سعر خاص");
            daedHead.setText("خصم");

        }



        System.out.println("VAluesssss" + product_name + " " + description);

        prodName1.setText(product_name);
        prodDiscription.setText(description);
        aedValue.setText(price);
        saedValue.setText(special_price);
        daedValue.setText(discount_price);
        quantityValue.setText(quantity1);
        reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getreorder(product_id);
            }
        });


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


    }*/



    private void showDetail(final ArrayList<JSONObject> arrayList2, String order_id) {

        dialog = new Dialog(Order.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_details);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        ImageView close_btn = dialog.findViewById(R.id.close_btn);
        ListView orderDetailList = dialog.findViewById(R.id.orderDetailList);

      //  System.out.println( "Arraaayyyy4444" + arrayList2);



        OrderViewAdapter adapter = new OrderViewAdapter(getApplicationContext(), arrayList2);
        orderDetailList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        nOID = order_id;

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


    }






    private void getreorder(final String product_id) {


        progressDialog = new ProgressDialog(Order.this);
        progressDialog.show();
        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();
        userid = pref.getString("userid", "0");

        String url = "http://inviewmart.com/mairak_api/Product_reorder.php";
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
                        //    System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");
                            dialog.dismiss();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Order.this, Order.class);
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        //    System.out.println("eccce0" + e);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //  Log.d("Error.Response", response);
                    //    System.out.println(error);
                        showAlert();
                       // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


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
                v = inflater.inflate(R.layout.order_row, null);

            try {

                Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


                tvDate = v.findViewById(R.id.tvDate);
                tvTime = v.findViewById(R.id.tvTime);
                tvOid = v.findViewById(R.id.tvOid);
                tvStatus = v.findViewById(R.id.tvStatus);
                tvPrice = v.findViewById(R.id.tvPrice);


                tvStatus.setTypeface(Montserrat_Medium);
                tvOid.setTypeface(Montserrat_Medium);
                tvTime.setTypeface(Montserrat_Medium);
                tvDate.setTypeface(Montserrat_Medium);
                tvPrice.setTypeface(Montserrat_Medium);


                tvTime.setText(settings.get(position).getString("created_time"));
                tvStatus.setText(settings.get(position).getString("delivered_status"));
                //tvOid.setText("ID " + settings.get(position).getString("order_id"));
                tvOid.setText(settings.get(position).getString("order_code"));
                tvDate.setText(settings.get(position).getString("created_date"));

                String sPrice =  settings.get(position).getString("total_price");
                Double tPrice = Double.valueOf(sPrice);
                Double wTPrice =tPrice+ ((tPrice/100)*5) ;

                String tValue = String.valueOf(wTPrice);
                tvPrice.setText("AED. " + tValue);


              System.out.println("PriceeeeeDDD" + settings.get(position).getString("total_price")+"::::::::::::" + wTPrice+"::::::::::::" + tPrice);
              System.out.println("PriceeeeeDDD" + settings.get(position).getString("order_id")+"::::::::::::" + wTPrice+"::::::::::::" + tPrice);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return v;
        }
    }










    public class OrderViewAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<JSONObject> settings;
        String status;
        JSONObject object;

        public OrderViewAdapter(Context context, ArrayList<JSONObject> settings) {
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
                v = inflater.inflate(R.layout.order_list_detail, null);

            Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


          //  System.out.println( "Arraaayyyy555" + settings);


            TextView ProductId,prouctName,quantity,prodDiscription,price;
            CardView reorder;



            prouctName= v.findViewById(R.id.prouctName);
            quantity= v.findViewById(R.id.quantity);
            prodDiscription= v.findViewById(R.id.prodDiscription);
            price= v.findViewById(R.id.price);

            reorder = v.findViewById(R.id.reorder);


            prouctName.setTypeface(Montserrat_Medium);
            quantity.setTypeface(Montserrat_Medium);
            prodDiscription.setTypeface(Montserrat_Medium);
            price.setTypeface(Montserrat_Medium);


            prodDiscription.setMovementMethod(new ScrollingMovementMethod());

            prouctName.setText(settings.get(position).optString("product_name"));
            quantity.setText("Quantity: "+settings.get(position).optString("quantity"));
            prodDiscription.setText(settings.get(position).optString("description"));
            price.setText("AED. "+settings.get(position).optString("price"));

            System.out.println("ChhecKKDetails" + settings.get(position).optString("price"));

            final String ProId = settings.get(position).optString("product_id");


            reorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getreorder(nOID);

                }
            });


            return v;
        }
    }






    public void homel() {


        Intent intent = new Intent(getApplicationContext(), DashBoard.class);
        startActivity(intent);
        finish();


    }


    public void offer() {
        Intent offerintent = new Intent(Order.this, NewOfferlisting.class);
        startActivity(offerintent);
        finish();


    }


    public void profile() {


        Intent intent = new Intent(getApplicationContext(), NotificationList.class);
        startActivity(intent);
        finish();


    }


    public void more() {

        Intent intent = new Intent(getApplicationContext(),OfferOrderListing.class);
        startActivity(intent);
        finish();



    }


    private void filterAlert() {

        final Dialog dialog = new Dialog(Order.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_date);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


        TextView tvToday,tvThisMonth,tvLastMonth,tvAllOrders;

        LinearLayout llToday,llThisMonth,llLastMonth,llAllOrders;



        tvToday = dialog.findViewById(R.id.tvToday);
        tvThisMonth = dialog.findViewById(R.id.tvThisMonth);
        tvLastMonth = dialog.findViewById(R.id.tvLastMonth);
        tvAllOrders = dialog.findViewById(R.id.tvAllOrders);

        llToday = dialog.findViewById(R.id.llToday);
        llThisMonth = dialog.findViewById(R.id.llThisMonth);
        llLastMonth = dialog.findViewById(R.id.llLastMonth);
        llAllOrders= dialog.findViewById(R.id.llAllOrders);



        llToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());

                c.add(Calendar.DATE,-1);
                String formattedDate2 = df.format(c.getTime());// subtract a month
                //  long dateOfLastMonth = c.getTimeInMillis();

                startDate = formattedDate2;
                endDate = formattedDate;

                arrylist.clear();
                order();


            }
        });




        llThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());

                c.add(Calendar.MONTH,-1);
                String formattedDate2 = df.format(c.getTime());// subtract a month

                endDate = formattedDate;
                startDate = formattedDate2;

                arrylist.clear();
                order();

            }
        });






        llLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                //Date currentTime = Calendar.getInstance().getTime();
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH,-1);
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c.getTime());

                c.add(Calendar.MONTH,-1);
                String formattedDate2 = df.format(c.getTime());// subtract a month
                //  long dateOfLastMonth = c.getTimeInMillis();

                endDate = formattedDate;
                startDate = formattedDate2;


                arrylist.clear();
                order();


            }
        });




        llAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                startDate = "";
                endDate = "";

                arrylist.clear();
                order();
            }
        });





        tvToday.setTypeface(Montserrat_Medium);
        tvThisMonth.setTypeface(Montserrat_Medium);
        tvLastMonth.setTypeface(Montserrat_Medium);
        tvAllOrders.setTypeface(Montserrat_Medium);







    }





    private void showAlert(){
        final Dialog dialog = new Dialog(Order.this);
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
