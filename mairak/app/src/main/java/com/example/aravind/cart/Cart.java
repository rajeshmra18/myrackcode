package com.example.aravind.cart;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
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
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aravind.mairak.DashBoard;
import com.example.aravind.mairak.GPSTracker;
import com.example.aravind.mairak.ProgressDialog;
import com.example.aravind.mairak.R;
import com.example.aravind.models.CartInfos;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Cart extends AppCompatActivity {

    ImageView ivBackarrow;
    TextView tvHeader, tvNoCart, tvTotaltxt, tvTotalAmt, tvNumbertxt, tvNumberAmt;
    ListView lvCartList;
    Button btBook;
    TextView tvIncTax, tvTaxValue, tvTaxPer;
    JSONArray result;
    String message, statusCode;
    JSONArray object2;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();
    LinearLayout promo1Lay, promo2Lay, promo3Lay;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid, language;
    private Typeface Montserrat_Medium;
    private String qty, prc;

    Double tqty = 0.0, tprc = 0.0;

    CartInfos cartInfos = new CartInfos();
    boolean isConnected;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    GPSTracker gps;
    double latitude, longitude;
    private String selectedPromocode = "";


    TextView tvProName, tvProDisc, tvQuantityTxt, tvQuantityVal, tvAedtxt, tvAedVal, applyPromo, tvdefPromoC1, tvdefPromoC2, tvdefPromoC3;
    ImageView ivProdImg, ivDelete;
    private ProgressDialog progressDialog;
    private String totalVal;

    EditText etPromoCode;
    LinearLayout llApply;
    private String pCode, IMEII;

    Double totalValue = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getLocation();

        initwidgets();

        getCartInfo();


        getCartList();


        if (arrylist.isEmpty()) {

            lvCartList.setVisibility(View.GONE);
            tvNoCart.setVisibility(View.VISIBLE);
        } else {
            lvCartList.setVisibility(View.VISIBLE);
            tvNoCart.setVisibility(View.GONE);
        }


        btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tqty > 0.00) {


                    //cartBook();
                    Intent intent = new Intent(getApplicationContext(), AddressConfirm.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(getApplicationContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                }


            }
        });


        applyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPromoDia();


            }
        });


        ivBackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                startActivity(intent);

                finish();
            }
        });
    }


    private void cartBook() {


        Boolean show = true;

        boolean isshow = false;
        if (show) {

            isshow = true;

            progressDialog = new ProgressDialog(Cart.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }
        final boolean finalIsshow = isshow;


        String url = "http://inviewmart.com/mairak_api/Cart_order.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


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

                                alert2();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                arrylist.clear();
                                getCartList();


                            } else {


                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showAlert();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                params.put("lat", String.valueOf(latitude));
                params.put("lng", String.valueOf(longitude));


                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }


    private void getCartInfo() {


/*

        Boolean show = true;

        boolean isshow = false;
        if (show) {

            isshow = true;

            progressDialog = new ProgressDialog(Cart.this);
            progressDialog.show();

        }

        final boolean finalIsshow = isshow;
*/

        String url = "http://inviewmart.com/mairak_api/Cart_info.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


//                        if (finalIsshow) {
//                            progressDialog.dismiss();
//                        }


                        try {


//                            progressDialog.dismiss();
                            JSONObject response1 = new JSONObject(response);
                            JSONObject result1 = new JSONObject(response);
                            arrylist.clear();
                            result = new JSONArray();
                            result1 = response1.getJSONObject("Response");
                            System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result1);

                            JSONObject data = new JSONObject();
                            JSONArray promoobj = new JSONArray();
                            JSONObject websetobj = new JSONObject();

                            //  object = result.getJSONArray(0).getJSONObject(0);

                            //   message = object.getString("message");

                            statusCode = result1.getString("status");

                            System.out.println("gdffftt" + result1 + "::::::::" + statusCode);

                            if (statusCode.equals("1")) {
                                data = result1.getJSONObject("data");
                                promoobj = data.getJSONArray("promocode");
                                websetobj = data.getJSONObject("web_settings");

                                String tax = websetobj.getString("tax");


                                cartInfos.setTax(Integer.valueOf(tax));
                                cartInfos.setPromocode1(promoobj.getString(0));
                                // cartInfos.setPromocode1("");
                                cartInfos.setPromocode2(promoobj.getString(1));
                                cartInfos.setPromocode3(promoobj.getString(2));

                                tvTaxValue.setText(String.valueOf(cartInfos.getTax()));


                                System.out.println("Successs" + cartInfos.getTax() + " :::::::::::: " + cartInfos.getPromocode1() + cartInfos.getPromocode2() + cartInfos.getPromocode3());


                            } else {


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showAlert();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                //  Log.d("params11", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }


    private void getCartList() {


        Boolean show = true;

        boolean isshow = false;
        if (show) {

            isshow = true;

            progressDialog = new ProgressDialog(Cart.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }

        final boolean finalIsshow = isshow;

        String url = "http://inviewmart.com/mairak_api/My_cart.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //  Log.d("Response", response);

                        if (finalIsshow) {
                            progressDialog.dismiss();
                        }

                        arrylist.clear();
                        tprc = 0.0;
                        tqty = 0.0;
                        try {

                            JSONObject response1 = new JSONObject(response);
                            arrylist.clear();
                            result = new JSONArray();
                            result = response1.getJSONArray("Response");
                            System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");
                            totalVal = object.getString("total");

                            if (statusCode.equals("1")) {

                                String Imageeee = "";
                                object2 = new JSONArray();
                                object2 = result.getJSONArray(1);


                                int i;

                                for (i = 0; i < object2.length(); i++) {

                                    arrylist.add(object2.getJSONObject(i));


                                    qty = arrylist.get(i).getString("qnty");
                                    prc = arrylist.get(i).getString("price");


                                    tqty = tqty + Double.valueOf(qty);
                                    tprc = tprc + Double.valueOf(prc);


                                }

                                Double taxValue = (tprc / 100) * Integer.valueOf(tvTaxValue.getText().toString().trim());


                                totalValue = tprc + taxValue;

                                System.out.println("ORginalllPricer" + tprc + "total" + totalValue);

                                tvTotalAmt.setText("AED. " + String.valueOf(tprc + taxValue));
                                tvNumberAmt.setText(String.valueOf(tqty));


                                if (arrylist.isEmpty()) {

                                    tvNumberAmt.setText("0");
                                    tvTotalAmt.setText("0");


                                    //  Toast.makeText(getApplicationContext(), "No Product found", Toast.LENGTH_LONG).show();
                                    if (arrylist.isEmpty()) {

                                        lvCartList.setVisibility(View.GONE);
                                        tvNoCart.setVisibility(View.VISIBLE);

                                    } else {
                                        lvCartList.setVisibility(View.VISIBLE);
                                        tvNoCart.setVisibility(View.GONE);
                                    }
                                } else {
                                    lvCartList.setVisibility(View.VISIBLE);
                                    tvNoCart.setVisibility(View.GONE);
                                }
                                CartListAdapter adapter = new CartListAdapter(Cart.this, arrylist, Imageeee);
                                lvCartList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                if (message.equals("Cart Is Empty")) {
                                    tvNoCart.setText("Cart Is Empty");

                                }

                            } else {


                                if (totalVal.equals("0")) {

                                    tvNumberAmt.setText("0");
                                    tvTotalAmt.setText("0");

                                    lvCartList.setVisibility(View.GONE);
                                    tvNoCart.setVisibility(View.VISIBLE);

                                } else {
                                    lvCartList.setVisibility(View.VISIBLE);
                                    tvNoCart.setVisibility(View.GONE);

                                }

                                if (!message.equals("Cart Is Empty")) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                } else {
                                    tvNoCart.setText("Cart Is Empty");
                                }

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
                        //   System.out.println(error);

                        showAlert();

                        //Toast.makeText(getApplicationContext(), "No Network! You are working offline now ", Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("language", language);
                //  Log.d("params11", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }

    public class CartListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<JSONObject> settings;
        String status;
        JSONObject object;
        String url;

        public CartListAdapter(Context context, ArrayList<JSONObject> settings, String imageurl) {
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
                v = inflater.inflate(R.layout.cart_row, null);


            Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


            final String imagefield, fimg, cart_id;

            tvProName = v.findViewById(R.id.tvProName);
            tvProDisc = v.findViewById(R.id.tvProDisc);
            tvQuantityTxt = v.findViewById(R.id.tvQuantityTxt);
            tvQuantityVal = v.findViewById(R.id.tvQuantityVal);
            tvAedtxt = v.findViewById(R.id.tvAedtxt);
            tvAedVal = v.findViewById(R.id.tvAedVal);
            ivProdImg = v.findViewById(R.id.ivProdImg);
            ivDelete = v.findViewById(R.id.ivDelete);


            tvProName.setTypeface(Montserrat_Medium);
            tvProDisc.setTypeface(Montserrat_Medium);
            tvQuantityTxt.setTypeface(Montserrat_Medium);
            tvQuantityVal.setTypeface(Montserrat_Medium);
            tvAedtxt.setTypeface(Montserrat_Medium);
            tvAedVal.setTypeface(Montserrat_Medium);


            if (language.equals("a")) {

                tvQuantityTxt.setText("الجودة");

            }


            tvProName.setText(settings.get(position).optString("p_name"));
            tvProDisc.setText(settings.get(position).optString("p_desc"));
            tvQuantityVal.setText(settings.get(position).optString("qnty"));
            tvAedVal.setText(settings.get(position).optString("price"));


            cart_id = settings.get(position).optString("cart_id");

            imagefield = settings.get(position).optString("p_image");

            System.out.println("Image====" + imagefield);
            //  System.out.println("Image====>>" + fimg);


            if (!imagefield.equals(""))
                Picasso.with(context).load(imagefield).error(R.drawable.can).placeholder(R.drawable.can).into(ivProdImg);


            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCart(cart_id);
                }
            });


            return v;
        }
    }

    private void deleteCart(final String cart_id) {


        {
            Boolean show = true;

            boolean isshow = false;
            if (show) {

                isshow = true;

                progressDialog = new ProgressDialog(Cart.this);
                progressDialog.show();
                //progressDialog.setContentView(R.layout.progress_dialog);
            }


            String url = "http://inviewmart.com/mairak_api/Delete_cart.php";


            final boolean finalIsshow = isshow;

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            //   Log.d("Response", response);


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

                                    arrylist.clear();

                                   /* if (arrylist.isEmpty()){

                                        lvCartList.setVisibility(View.GONE);
                                        tvNoCart.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        lvCartList.setVisibility(View.VISIBLE);
                                        tvNoCart.setVisibility(View.GONE);
                                    }*/


                                    tvNoCart.setText("");


                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                                    getCartList();

                                } else {
                                    // Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
                            //  Toast.makeText(getApplicationContext(), "No Network! You are working offline now ", Toast.LENGTH_SHORT).show();


                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", userid);
                    params.put("cart_id", cart_id);
                    params.put("type", cart_id);
                    //  Log.d("params", params.toString());


                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);


        }


    }


    public void alert2() {

        final Dialog dialog = new Dialog(Cart.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.activity_thankyou);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        TextView tank = dialog.findViewById(R.id.tankT);
        TextView sub = dialog.findViewById(R.id.sub);
        ImageView close = dialog.findViewById(R.id.close_btn);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tank.setTypeface(Montserrat_Medium);
        sub.setTypeface(Montserrat_Medium);


        if (language.equals("a")) {
            tank.setText("\"شكراً لحجزك معنا في\"ماي راك");
            sub.setText("سيقوم فريق التوصيل الخاص بنا بالوصول إليك قريباً بخصوص الطلب الخاص بك");
        }

    }

    private void showPromoDia() {


        final Dialog dialog = new Dialog(Cart.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.promo_code_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        llApply = dialog.findViewById(R.id.llApply);
        etPromoCode = dialog.findViewById(R.id.etPromoCode);
        tvdefPromoC1 = dialog.findViewById(R.id.tvdefPromoC1);
        tvdefPromoC2 = dialog.findViewById(R.id.tvdefPromoC2);
        tvdefPromoC3 = dialog.findViewById(R.id.tvdefPromoC3);

        TextView tvPromomTile = dialog.findViewById(R.id.tvPromomTile);

        tvPromomTile.setTypeface(Montserrat_Medium);
        tvdefPromoC1.setTypeface(Montserrat_Medium);
        tvdefPromoC2.setTypeface(Montserrat_Medium);
        tvdefPromoC3.setTypeface(Montserrat_Medium);
        etPromoCode.setTypeface(Montserrat_Medium);

        RadioButton rbEtPromo, rbdefPromo1, rbdefPromo2, rbdefPromo3;
        final RadioGroup radioSelect = dialog.findViewById(R.id.radioSelect);


        rbEtPromo = dialog.findViewById(R.id.rbEtPromo);
        rbdefPromo1 = dialog.findViewById(R.id.rbdefPromo1);
        rbdefPromo2 = dialog.findViewById(R.id.rbdefPromo2);
        rbdefPromo3 = dialog.findViewById(R.id.rbdefPromo3);


        promo1Lay = dialog.findViewById(R.id.promo1Lay);
        promo2Lay = dialog.findViewById(R.id.promo2Lay);
        promo3Lay = dialog.findViewById(R.id.promo3Lay);

        if (cartInfos.getPromocode1().equals("")) {
            rbdefPromo1.setVisibility(View.GONE);
            promo1Lay.setVisibility(View.GONE);
            tvdefPromoC1.setVisibility(View.GONE);
        } else {
            rbdefPromo1.setVisibility(View.VISIBLE);
            promo1Lay.setVisibility(View.VISIBLE);
            tvdefPromoC1.setVisibility(View.VISIBLE);
        }


        if (cartInfos.getPromocode2().equals("")) {
            rbdefPromo2.setVisibility(View.GONE);
            promo2Lay.setVisibility(View.GONE);
            tvdefPromoC2.setVisibility(View.GONE);
        } else {
            rbdefPromo2.setVisibility(View.VISIBLE);
            promo2Lay.setVisibility(View.VISIBLE);
            tvdefPromoC2.setVisibility(View.VISIBLE);
        }


        if (cartInfos.getPromocode3().equals("")) {
            rbdefPromo3.setVisibility(View.GONE);
            promo3Lay.setVisibility(View.GONE);
            tvdefPromoC3.setVisibility(View.GONE);
        } else {
            rbdefPromo3.setVisibility(View.VISIBLE);
            promo3Lay.setVisibility(View.VISIBLE);
            tvdefPromoC3.setVisibility(View.VISIBLE);
        }


        tvdefPromoC1.setText(cartInfos.getPromocode1());
        tvdefPromoC2.setText(cartInfos.getPromocode2());
        tvdefPromoC3.setText(cartInfos.getPromocode3());


        System.out.println("ProomoCodeeew" + cartInfos.getPromocode1());


        final String pCode = etPromoCode.getText().toString().trim();

        LinearLayout llApplyLast = dialog.findViewById(R.id.llApplyLast);

        llApplyLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioSelect.getCheckedRadioButtonId();

                System.out.println("SelleectID" + selectedId);

                if (radioSelect.getCheckedRadioButtonId() == R.id.rbEtPromo) {


                    selectedPromocode = String.valueOf(etPromoCode.getText());


                } else if (radioSelect.getCheckedRadioButtonId() == R.id.rbdefPromo1) {

                    selectedPromocode = String.valueOf(tvdefPromoC1.getText());


                } else if (radioSelect.getCheckedRadioButtonId() == R.id.rbdefPromo2) {

                    selectedPromocode = String.valueOf(tvdefPromoC2.getText());


                } else if (radioSelect.getCheckedRadioButtonId() == R.id.rbdefPromo3) {

                    selectedPromocode = String.valueOf(tvdefPromoC3.getText());

                }

                System.out.println("COodeeee" + selectedPromocode);
                dialog.dismiss();
                applyPromoC(dialog, selectedPromocode);

            }
        });

        llApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                applyPromoC(dialog, selectedPromocode);
            }
        });


    }

    private void getLocation() {


        if (isStoragePermissionGranted()) {

            gps = new GPSTracker(Cart.this);
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
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Cart.this);
                    builder.setMessage("Do you Want to Retry ?")
                            .setTitle("No Internet Connection!")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }
            } else {

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

                ActivityCompat.requestPermissions(Cart.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                ActivityCompat.requestPermissions(Cart.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        } else {

            return true;
        }
    }


    private void initwidgets() {

        ivBackarrow = findViewById(R.id.ivBackarrow);
        tvHeader = findViewById(R.id.tvHeader);
        tvNoCart = findViewById(R.id.tvNoCart);
        tvTotaltxt = findViewById(R.id.tvTotaltxt);
        tvTotalAmt = findViewById(R.id.tvTotalAmt);
        tvNumbertxt = findViewById(R.id.tvNumbertxt);
        tvNumberAmt = findViewById(R.id.tvNumberAmt);
        btBook = findViewById(R.id.btBook);
        applyPromo = findViewById(R.id.applyPromo);
        lvCartList = findViewById(R.id.lvCartList);


        IMEII = getUniqueIMEIId(this);

        TextView havePromo = findViewById(R.id.havePromo);

        tvIncTax = findViewById(R.id.tvIncTax);
        tvTaxValue = findViewById(R.id.tvTaxValue);
        tvTaxPer = findViewById(R.id.tvTaxPer);


        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);
        language = pref.getString("language", "e");


        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


        havePromo.setTypeface(Montserrat_Medium);
        applyPromo.setTypeface(Montserrat_Medium);

        tvIncTax.setTypeface(Montserrat_Medium);
        tvTaxValue.setTypeface(Montserrat_Medium);
        tvTaxPer.setTypeface(Montserrat_Medium);

        tvHeader.setTypeface(Montserrat_Medium);
        tvNoCart.setTypeface(Montserrat_Medium);
        tvTotaltxt.setTypeface(Montserrat_Medium);
        tvTotalAmt.setTypeface(Montserrat_Medium);
        tvNumbertxt.setTypeface(Montserrat_Medium);
        tvNumberAmt.setTypeface(Montserrat_Medium);
        btBook.setTypeface(Montserrat_Medium);


        if (language.equals("a")) {

            btBook.setText("الحجز");
        }


    }


    private void showAlert() {
        final Dialog dialog = new Dialog(Cart.this);
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

    private void applyPromoC(final Dialog dialog, String selectedPromocode) {


        pCode = selectedPromocode;
        //pCode = String.valueOf(etPromoCode.getText()) ;


        Boolean show = true;

        boolean isshow = false;
        if (show) {

            isshow = true;

            progressDialog = new ProgressDialog(Cart.this);
            progressDialog.show();

        }
        final boolean finalIsshow = isshow;


        String url = "http://inviewmart.com/mairak_api/Promo_code.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (finalIsshow) {
                            progressDialog.dismiss();
                        }

                        try {

                            JSONObject response1 = new JSONObject(response);
                            result = new JSONArray();
                            result = response1.getJSONArray("Response");
                            System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object;
                            JSONObject data;

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");
                            data = object.getJSONObject("data");
                            Double discAmnt = 0.00;
                            Double newAmt = 0.00;

                            if (statusCode.equals("1")) {


                                dialog.dismiss();


                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                if (data.getString("type").equals("F")) {

                                    discAmnt = data.getDouble("discount");

                                    if (totalValue > discAmnt) {
                                        Toast.makeText(getApplicationContext(), data.getString("discount") + "Deducted ", Toast.LENGTH_LONG).show();

                                        newAmt = totalValue - discAmnt;
                                        tvTotalAmt.setText("AED. " + String.valueOf(newAmt));
                                    } else {
                                        Toast.makeText(getApplicationContext(), totalValue.toString(), Toast.LENGTH_LONG).show();

                                        tvTotalAmt.setText("AED.0.0");

                                    }


                                } else if (data.getString("type").equals("F")) {

                                    Double percentV = (totalValue / 100) * data.getDouble("discount");

                                    if (totalValue > percentV) {
                                        newAmt = totalValue - percentV;
                                        tvTotalAmt.setText("AED. " + String.valueOf(newAmt));
                                    } else {
                                        tvTotalAmt.setText("AED.0.0");
                                    }


                                }


                            } else {

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showAlert();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("promo_code", pCode);
                params.put("ime_number", IMEII);

                System.out.println("Parrammaaas" + params);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }


    public String getUniqueIMEIId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(Cart.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = telephonyManager.getImei();
            }
            Log.e("imei", "=" + imei);
            System.out.println("IMMMMMEEeeeeee" + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }


}
