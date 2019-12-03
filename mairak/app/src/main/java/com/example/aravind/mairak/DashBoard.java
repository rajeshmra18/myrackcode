package com.example.aravind.mairak;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aravind.cart.Cart;
import com.example.aravind.navigation.AppBaseActivity;
import com.example.aravind.offerlisting.NewOfferlisting;
import com.example.aravind.offerlisting.OfferOrderListing;
import com.example.aravind.util.DatabaseHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DashBoard extends AppBaseActivity {


    Typeface Montserrat_Medium;

    ImageView menuico, cart, home, offer, order, profile, more;
    LinearLayout homelay, offerlay, orderlay, profilelay, morelay;
    ListView prodlist;
    TextView tvCartCount;

    private ProgressDialog progressDialog;

    JSONArray result,object2;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String message, statusCode, userid, prodID,imagefield;
    String qtyV = "";
    int quant = 1;
    Double val = 0.00;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();

    String badgeCont,language;



    DatabaseHelper Dhb;
    private String prodName, price, desc, sPrice, dPrice, img,id;
    Double pric;

    boolean isConnected;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    GPSTracker gps;
    double latitude,longitude;
    ArrayList<JSONObject> arrylist1 = new ArrayList<JSONObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getLocation();

        getBadgeCount();

        initwidgets();

        getNotifivcationBadge();

        homelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                orderlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

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


        menuico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onDraweropen();
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

        prodlist = findViewById(R.id.prodList);


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Cart.class);
                startActivity(intent);


            }
        });









        products();


    }

    public void getNotifivcationBadge() {

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


    private void getBadgeCount() {




        {


            String url = "http://inviewmart.com/mairak_api/My_cart.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                           // Log.d("Response", response);

                            try {

                                JSONObject response1 = new JSONObject(response);

                                result = new JSONArray();
                                result = response1.getJSONArray("Response");
                            //    System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                                JSONObject object = new JSONObject();

                                object = result.getJSONArray(0).getJSONObject(0);

                                message = object.getString("message");
                                statusCode = object.getString("status");
                                badgeCont = object.getString("total");


                                tvCartCount.setText(badgeCont);



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
                    Log.d("params", params.toString());


                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);


        }





    }

    private void getLocation() {


        if (isStoragePermissionGranted()) {

            gps = new GPSTracker(DashBoard.this);
            // check if GPS enabled
            if (gps.canGetLocation()) {


                latitude = gps.getLatitude();
                longitude = gps.getLongitude();


                cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {

                } else {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DashBoard.this);
                    builder.setMessage("Do you Want to Retry ?")
                            .setTitle("No Internet Connection!")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!


                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        }

    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) && (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                return true;
            } else {

                ActivityCompat.requestPermissions(DashBoard.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                ActivityCompat.requestPermissions(DashBoard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    private void initwidgets() {

        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);
        language = pref.getString("language", "e");

        Dhb = DatabaseHelper.getInstance(this);

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");





        menuico = findViewById(R.id.menuico);
        cart = findViewById(R.id.cart);
        home = findViewById(R.id.home);
        offer = findViewById(R.id.offer);
        order = findViewById(R.id.order);
        profile = findViewById(R.id.profile);
        more = findViewById(R.id.more);


        homelay = findViewById(R.id.homeLay);
        offerlay = findViewById(R.id.offerLay);
        orderlay = findViewById(R.id.orderLay);
        profilelay = findViewById(R.id.profileLay);
        morelay = findViewById(R.id.moreLay);

        tvCartCount = findViewById(R.id.tvCartCount);

        homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


    }


    public void products() {



        Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(DashBoard.this);
            progressDialog.show();

        }
        final boolean finalIsshow = isshow;

        String url = "http://inviewmart.com/mairak_api/Product.php";
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
                        //    System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");


                            if (statusCode.equals("1")) {

                                String Imageeee = "";
                                object2 = new JSONArray();
                                object2 = result.getJSONArray(1);


                                int i;

                                for (i = 0; i < object2.length(); i++) {

                                    arrylist.add(object2.getJSONObject(i));


                                    JSONArray imagearray = new JSONArray();
                                    imagearray = object2.getJSONObject(i).getJSONArray("images");
                                //    System.out.println("arrrrrr" + imagearray);
                                    if (imagearray.length() != 0) {

                                        Imageeee = imagearray.getString(0);

                                    }


                                    id = arrylist.get(i).getString("id");
                                    prodName = arrylist.get(i).getString("product_name");
                                    price = arrylist.get(i).getString("price");
                                    pric  = arrylist.get(i).getDouble("price");
                                    desc = arrylist.get(i).getString("description");
                                    sPrice = arrylist.get(i).getString("special_price");
                                    dPrice = arrylist.get(i).getString("discount_price");
                                    img = arrylist.get(i).getString("images");


                                    Dhb.addToProductList(id, prodName, price, desc, sPrice, dPrice, img);


                                }


                                if (arrylist.isEmpty())
                                    Toast.makeText(getApplicationContext(), "No Product found", Toast.LENGTH_LONG).show();

                                ProductListAdapter adapter = new ProductListAdapter(DashBoard.this, arrylist, Imageeee);
                                prodlist.setAdapter(adapter);
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

                        progressDialog.dismiss();
                      //  System.out.println(error);

                        Toast.makeText(getApplicationContext(), "No Network! You are working offline now ", Toast.LENGTH_SHORT).show();


                        arrylist = (ArrayList<JSONObject>) Dhb.getProd().clone();

                        ProductListAdapter adapter = new ProductListAdapter(DashBoard.this, arrylist, img);
                        prodlist.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");
                params.put("language", language);
                Log.d("params", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }


    public class ProductListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<JSONObject> settings;
        String url;
        String urll;
        Double pricev;

        public ProductListAdapter(Context context, ArrayList<JSONObject> settings, String imageurl) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.settings = settings;
            this.url = imageurl;
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
                v = inflater.inflate(R.layout.activity_dash_row, null);



                Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

                ImageView canico = v.findViewById(R.id.canicon);
                TextView prodName = v.findViewById(R.id.prodName);
                TextView prodDis = v.findViewById(R.id.prodDiscription);
                TextView aed = v.findViewById(R.id.aed);
                TextView aedValue = v.findViewById(R.id.aedValue);
                Button book = v.findViewById(R.id.bookButton);
                Button btPreorder = v.findViewById(R.id.btPreorder);

                LinearLayout llAedG = v.findViewById(R.id.llAedG);

                prodDis.setTypeface(Montserrat_Medium);
                prodName.setTypeface(Montserrat_Medium);
                aed.setTypeface(Montserrat_Medium);
                aedValue.setTypeface(Montserrat_Medium);
                book.setTypeface(Montserrat_Medium);
                btPreorder.setTypeface(Montserrat_Medium);




            if (language.equals("a")){


                book.setText("إضافة");
                btPreorder.setText("الطلبات المقدمة");
                llAedG.setGravity(Gravity.END);


            }else{

                book.setText("ADD");
                btPreorder.setText("PRE ORDER");

            }


                prodName.setText(settings.get(position).optString("product_name"));
                prodDis.setText(settings.get(position).optString("description"));
                String v1 = (settings.get(position).optString("price"));
                pricev = (settings.get(position).optDouble("price"));
              //  String v2 = v1.replaceAll("[^-?0-9]+", "");

                aedValue.setText(v1);

             //   System.out.println("Product====" + prodName);

                JSONArray ja = new JSONArray();



                imagefield = settings.get(position).optString("images");
                ja = settings.get(position).optJSONArray("images");


            try {
                if (ja!= null){
                    urll = ja.getString(0);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  System.out.println("Image====" + imagefield  + " nnn " + urll);

                Picasso.with(DashBoard.this).load(urll).placeholder(R.drawable.can).into(canico);





            prodName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alert1(position);

                }
            });


                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        alert1(position);


                    }
                });


                btPreorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        alert4(position);

                    }
                });




            return v;
        }
    }


    public void alert1(int p) {

        getLocation();

        final Dialog dialog = new Dialog(DashBoard.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_book);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

        quant = 1;

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


        ImageView prodImage, close;
        final TextView prodName, prodDis, aed, aedval, bookT,  incement, decrement;
        final EditText qty;
        LinearLayout bookLay;



        prodName = dialog.findViewById(R.id.prodName1);
        prodDis = dialog.findViewById(R.id.prodDiscription);
        aed = dialog.findViewById(R.id.aed);
        aedval = dialog.findViewById(R.id.aedValue);
        bookT = dialog.findViewById(R.id.booktext);
        bookLay = dialog.findViewById(R.id.bookButton2);
        prodImage = dialog.findViewById(R.id.productImg);
        close = dialog.findViewById(R.id.close_btn);
        qty = dialog.findViewById(R.id.quantity);
        qty.setSelection(qty.getText().length());
        incement = dialog.findViewById(R.id.increment);
        decrement = dialog.findViewById(R.id.decrement);

        prodDis.setMovementMethod(new ScrollingMovementMethod());


        if (language.equals("a")){


            bookT.setText("إضافة");

        }else{

            bookT.setText("ADD");


        }


        String v =  qty.getText().toString().trim();






        qty.addTextChangedListener(new TextWatcher() {

                                       @Override
                                       public void onTextChanged(CharSequence s, int start, int before, int count) {

                                           if (qty.getText().toString().trim().equals("")){
                                               quant = 1;
                                               Double price = val * quant;
                                               aedval.setText(price + "");
                                           }else {
                                               quant =Integer.valueOf( qty.getText().toString().trim());
                                               Double price = val * quant;
                                               aedval.setText(price + "");
                                           }


                                       }

                                       @Override
                                       public void beforeTextChanged(CharSequence s, int start, int count,
                                                                     int after) {

                                           // TODO Auto-generated method stub
                                       }

                                       @Override
                                       public void afterTextChanged(Editable s) {
                                           // TODO Auto-generated method stub


                                           if (qty.getText().toString().trim().equals("")){
                                               quant = 1;
                                               Double price = val * quant;
                                               aedval.setText(price + "");
                                           }else {
                                               quant =Integer.valueOf( qty.getText().toString().trim());
                                               Double price = val * quant;
                                               aedval.setText(price + "");
                                           }


                                       }
                                   }

        );






        incement.setTypeface(Montserrat_Medium);
        decrement.setTypeface(Montserrat_Medium);
        prodName.setTypeface(Montserrat_Medium);
        prodDis.setTypeface(Montserrat_Medium);
        aed.setTypeface(Montserrat_Medium);
        aedval.setTypeface(Montserrat_Medium);
        bookT.setTypeface(Montserrat_Medium);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quant = 1;
                dialog.dismiss();

            }
        });

        incement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quant =Integer.valueOf( qty.getText().toString().trim());

                quant++;
                qty.setText(quant + "");

               // System.out.println("Valueeee" + val);
//                int price = val * quant;
//                aedval.setText(price + "");

            }
        });


        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quant > 1) {
                    quant--;
                    qty.setText(quant + "");
                    Double price = val * quant;

                    aedval.setText(price + "");
                }

            }
        });


        try {


            prodName.setText(arrylist.get(p).getString("product_name"));
            prodID = arrylist.get(p).getString("id");
            prodDis.setText(arrylist.get(p).getString("description"));
            String v1 = arrylist.get(p).getString("price");
           // String v2 = v1.replaceAll("[^-?0-9]+", "");
            JSONArray imagearray = new JSONArray();
            imagearray = arrylist.get(p).getJSONArray("images");
            String Image = imagearray.getString(0);

            Picasso.with(DashBoard.this).load(Image).placeholder(R.drawable.can).into(prodImage);
           // System.out.println("immmaaggeeee111"+ Image);
            // String v2 = v1.substring(1,v1.length());
            val = Double.parseDouble(v1);




            aedval.setText(v1);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        bookLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                qtyV = qty.getText().toString();

                if (isNetworkAvailable()) {


                    Boolean show = true;

                    boolean isshow = false;
                    if (show) {

                        isshow = true;

                        progressDialog = new ProgressDialog(DashBoard.this);
                        progressDialog.show();
                    }

                    userid = pref.getString("userid", null);

                    String url = "http://inviewmart.com/mairak_api/Add_to_cart.php";

                    final boolean finalIsshow = isshow;

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                          //  Log.d("Response", response);

                            if (finalIsshow) {
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


                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                    getBadgeCount();


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
                                 //   System.out.println(error);
                                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {


                            Map<String, String> params = new HashMap<String, String>();
                            params.put("p_id", prodID);
                            params.put("userid", userid);
                            params.put("qnty", qtyV);
                            params.put("price", aedval.getText().toString());
                            params.put("lat",String.valueOf(latitude));
                            params.put("lng",String.valueOf(longitude));






                          //  Log.d("params", params.toString());


                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(DashBoard.this);
                    queue.add(postRequest);


                } else {

                    Toast.makeText(DashBoard.this, "Sorry you are offline cant order this product right now, Please connect to the network", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }


    public void alert2() {

        final Dialog dialog = new Dialog(DashBoard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.thankyou_pre);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        TextView tank = dialog.findViewById(R.id.tankT);
        TextView sub = dialog.findViewById(R.id.sub);
        ImageView close = dialog.findViewById(R.id.close_btn);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        tank.setTypeface(Montserrat_Medium);
        sub.setTypeface(Montserrat_Medium);



        if (language.equals("a")){
            tank.setText("\"شكراً لحجزك معنا في\"ماي راك");
            sub.setText("سيقوم فريق التوصيل الخاص بنا بالوصول إليك قريباً بخصوص الطلب الخاص بك");
        }

    }


    public void alert3() {

        final Dialog dialog = new Dialog(DashBoard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.activity_warning);
        dialog.show();

        TextView logut = dialog.findViewById(R.id.logoutT);
        TextView dou = dialog.findViewById(R.id.dou);

        Button yes = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);

        logut.setTypeface(Montserrat_Medium);
        dou.setTypeface(Montserrat_Medium);
        yes.setTypeface(Montserrat_Medium);
        no.setTypeface(Montserrat_Medium);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.remove("loggedIn").commit();
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();


            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });


    }

    public void alert4(int p) {

        //Pre Order
        getLocation();
        final Dialog dialog = new Dialog(DashBoard.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.preorder);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

        quant = 1;

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


        ImageView prodImage, close;
        final TextView prodName, prodDis, aed, aedval, bookT,  incement, decrement, tvDate, tvTime;
        final EditText qty;
        LinearLayout bookLay;





        prodName = dialog.findViewById(R.id.prodName1);
        prodDis = dialog.findViewById(R.id.prodDiscription);
        aed = dialog.findViewById(R.id.aed);
        aedval = dialog.findViewById(R.id.aedValue);
        bookT = dialog.findViewById(R.id.booktext);
        bookLay = dialog.findViewById(R.id.bookButton2);
        prodImage = dialog.findViewById(R.id.productImg);
        close = dialog.findViewById(R.id.close_btn);
        qty = dialog.findViewById(R.id.quantity);
        qty.setSelection(qty.getText().length());
        tvDate = dialog.findViewById(R.id.tvDate);
        tvTime = dialog.findViewById(R.id.tvTime);

        prodDis.setMovementMethod(new ScrollingMovementMethod());

        incement = dialog.findViewById(R.id.increment);
        decrement = dialog.findViewById(R.id.decrement);


        incement.setTypeface(Montserrat_Medium);
        decrement.setTypeface(Montserrat_Medium);
        prodName.setTypeface(Montserrat_Medium);
        prodDis.setTypeface(Montserrat_Medium);
        aed.setTypeface(Montserrat_Medium);
        aedval.setTypeface(Montserrat_Medium);
        bookT.setTypeface(Montserrat_Medium);



        qty.addTextChangedListener(new TextWatcher() {

                                           @Override
                                           public void onTextChanged(CharSequence s, int start, int before, int count) {

                                               if (qty.getText().toString().trim().equals("")){
                                                   quant = 1;
                                                   Double price = val * quant;
                                                   aedval.setText(price + "");
                                               }else {
                                                   quant =Integer.valueOf( qty.getText().toString().trim());
                                                   Double price = val * quant;
                                                   aedval.setText(price + "");
                                               }


                                           }

                                           @Override
                                           public void beforeTextChanged(CharSequence s, int start, int count,
                                                                         int after) {

                                               // TODO Auto-generated method stub
                                           }

                                           @Override
                                           public void afterTextChanged(Editable s) {
                                               // TODO Auto-generated method stub


                                               if (qty.getText().toString().trim().equals("")){
                                                   quant = 1;
                                                   Double price = val * quant;
                                                   aedval.setText(price + "");
                                               }else {
                                                   quant =Integer.valueOf( qty.getText().toString().trim());
                                                   Double price = val * quant;
                                                   aedval.setText(price + "");
                                               }


                                           }
                                       }

        );




        if (language.equals("a")){


            bookT.setText("الطلبات المقدمة");
            tvTime.setText("اختر الوقت");
            tvDate.setText("اختر التاريخ");

        }else{

            bookT.setText("PRE ORDER");
            tvTime.setText("Select Time");
            tvDate.setText("Select Date");


        }





        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quant = 1;
                dialog.dismiss();
            }
        });

        incement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                quant =Integer.valueOf( qty.getText().toString().trim());
                quant++;

                qty.setText(quant + "");

              //  System.out.println("Valueeee" + val);

            }
        });


        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quant > 1) {
                    quant--;
                    qty.setText(quant + "");
                    Double price = val * quant;

                    aedval.setText(price + "");
                }

            }
        });

        qtyV = qty.getText().toString();


        tvDate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                setDate(tvDate);


            }
        });


        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setTime(tvTime);

            }
        });


        try {

            prodName.setText(arrylist.get(p).getString("product_name"));
            prodID = arrylist.get(p).getString("id");
            prodDis.setText(arrylist.get(p).getString("description"));
            String v1 = aedval.getText().toString();
            //String v2 = v1.replaceAll("[^-?0-9]+", "");
            JSONArray imagearray = new JSONArray();
            imagearray = arrylist.get(p).getJSONArray("images");
            String Image = imagearray.getString(0);
            Picasso.with(DashBoard.this).load(Image).placeholder(R.drawable.can).into(prodImage);
            val = Double.parseDouble(v1);

            aedval.setText(v1);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        bookLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (tvDate.getText().toString().equals("Select Date")||tvTime.getText().toString().equals("Select Time")){


                    Toast.makeText(DashBoard.this, "Please Select Date and Time", Toast.LENGTH_SHORT).show();

                }
                else {


                    if (isNetworkAvailable()) {


                        boolean show = true;

                        boolean isshow = false;
                        if (show) {

                            isshow = true;

                            progressDialog = new ProgressDialog(DashBoard.this);
                            progressDialog.show();

                        }

                        userid = pref.getString("userid", null);

                        String url = "http://inviewmart.com/mairak_api/Preorder.php";

                        final boolean finalIsshow = isshow;

                        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // response
                               // Log.d("Response", response);

                                if (finalIsshow) {
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


                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                        alert2();
                                        dialog.dismiss();


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

                                   //     System.out.println(error);
                                        showAlert();



                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() {


                                Map<String, String> params = new HashMap<String, String>();
                                params.put("product_id", prodID);
                                params.put("user_id", userid);
                                params.put("quantity", qtyV);
                                params.put("preorder_date", tvDate.getText().toString());
                                params.put("preorder_time", tvTime.getText().toString());
                                params.put("location_lat",String.valueOf(latitude));
                                params.put("location_long",String.valueOf(longitude));

                               // Log.d("params", params.toString());


                                return params;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(DashBoard.this);
                        queue.add(postRequest);


                    } else {

                        Toast.makeText(DashBoard.this, "Sorry you are offline cant order this product right now, Please connect to the network", Toast.LENGTH_SHORT).show();

                    }



                }





            }
        });


    }


    private void setTime(final TextView tvTime) {


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {





                int hourr = selectedHour;
                int minutes = selectedMinute;


                String timeSet = "";
                if (hourr > 12) {
                    hourr -= 12;
                    timeSet = "PM";
                } else if (hourr == 0) {
                    hourr += 12;
                    timeSet = "AM";
                } else if (hourr == 12) {
                    timeSet = "PM";
                } else {
                    timeSet = "AM";
                }

                String min = "";
                if (minutes < 10)
                    min = "0" + minutes;
                else
                    min = String.valueOf(minutes);

                // Append in a StringBuilder
                String aTime = new StringBuilder().append(hourr).append(':')
                        .append(min).append(" ").append(timeSet).toString();
                tvTime.setText(aTime);


            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();


    }

    private void setDate(final TextView tvDate) {

        int mYear, mMonth, mDay;


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }



    public void offer() {

        Intent offerintent = new Intent(DashBoard.this, NewOfferlisting.class);
        startActivity(offerintent);


    }


    @Override
    protected void onResume() {
        super.onResume();

        getBadgeCount();
        getNotifivcationBadge();
        homelay.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        morelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        offerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        orderlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        profilelay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    public void order() {



        Intent intent = new Intent(getApplicationContext(), Order.class);
        startActivity(intent);


    }


    public void profile() {


        Intent intent = new Intent(getApplicationContext(), NotificationList.class);
        startActivity(intent);


    }


    public void more() {

        Intent intent = new Intent(getApplicationContext(),OfferOrderListing.class);
        startActivity(intent);
        finish();


    }






    private void showAlert(){
        final Dialog dialog = new Dialog(DashBoard.this);
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




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }





}
