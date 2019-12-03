package com.mairak.bgi.offerlisting;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mairak.bgi.mairak.DashBoard;
import com.mairak.bgi.mairak.GPSTracker;
import com.mairak.bgi.mairak.ProgressDialog;
import com.mairak.bgi.mairak.R;
import com.mairak.bgi.util.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OfferAddress extends AppCompatActivity {
    ImageView imExpand,ivBackarrow,imLocPin;
    LinearLayout formAdd;
    TextView selectTv,tvHeader,tvNamedef,tvadd1def,tvadd2def,tvstatedef,addnewTv;
    EditText etname,etadd1,etadd2,etstate;
    Button btndeliverdef,btnEdit,btnDelete,btndeliver2;
    RadioButton selectRadio;
    JSONArray result;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    boolean isConnected;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    GPSTracker gps;
    Double latitude=0.00,longitude=0.00;

    private  String address;
    DatabaseHelper Dhb;
    private String OfferID;

    private String language,userid,message,statusCode;
    private Typeface Montserrat_Medium;
    private ProgressDialog progressDialog;
    private JSONArray object2;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_address);
        initwidgets();

        getLocation();

        getDefaultAdd();



        imLocPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getLocationOne();


            }
        });

        imExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (formAdd.getVisibility()==View.VISIBLE){


                    formAdd.setVisibility(View.GONE);
                    imExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp));



                }else {


                    formAdd.setVisibility(View.VISIBLE);
                    imExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));

                }


            }
        });

        btndeliverdef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                address = tvNamedef.getText().toString().trim()+ "," +tvadd1def.getText().toString().trim()+","+tvadd2def.getText().toString().trim()+ "," +tvstatedef.getText().toString().trim();
                 OfferOrder(address,OfferID);


            }
        });


        btndeliver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (etname.getText().toString().trim().equals("")){
                    etname.setError("Enter your Name");
                    etname.requestFocus();
                }else if (etadd1.getText().toString().trim().equals("")){

                    etadd1.setError("Enter the Address1");
                    etadd1.requestFocus();
                }else if (etadd2.getText().toString().trim().equals("")){

                    etadd2.setError("Enter the Address2");
                    etadd2.requestFocus();
                }

                else {


                    Dhb.addToAddressBook(etname.getText().toString().trim(),etadd1.getText().toString().trim(),etadd2.getText().toString().trim(),etstate.getText().toString().trim());

                    address = etname.getText().toString().trim()+","+etadd1.getText().toString().trim()+","+etadd2.getText().toString().trim()+","+etstate.getText().toString().trim();
                    OfferOrder(address,OfferID);

                }


            }
        });


        ivBackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),NewOfferlisting.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void OfferOrder(final String address, String offerID) {


        {

            progressDialog = new ProgressDialog(OfferAddress.this);
            progressDialog.show();
            pref = OfferAddress.this.getSharedPreferences("miarak", Context.MODE_PRIVATE);
            editor = pref.edit();
            userid = pref.getString("userid", "0");
            // System.out.println("userrrrrrr" + userid + "  " + latitude + " " + longitude + " " + placename + " " + id + " ");
            String url = "http://inviewmart.com/mairak_api/Offer_order.php";
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
                              //  dialog.dismiss();
                                progressDialog.dismiss();

                                alert2();



                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                // System.out.println("eccce0" + e);
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", userid);
                    params.put("offer_id", OfferID);
                    params.put("order_place", address);
                    params.put("latitude", String.valueOf(latitude));
                    params.put("longitude", String.valueOf(longitude));
                    params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(postRequest);

        }






    }


    private void initwidgets() {

        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        Dhb = DatabaseHelper.getInstance(this);
        userid = pref.getString("userid", null);
        language = pref.getString("language", "e");


            Bundle extras = getIntent().getExtras();

        if (extras!=null)
        OfferID= extras.getString("OfferID");



        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        imExpand = findViewById(R.id.imExpand);
        formAdd = findViewById(R.id.formAdd);
        formAdd.setVisibility(View.GONE);
        ivBackarrow = findViewById(R.id.ivBackarrow);
        imLocPin = findViewById(R.id.imLocPin);

        selectTv = findViewById(R.id.selectTv);
        tvHeader= findViewById(R.id.tvHeader);
        tvNamedef= findViewById(R.id.tvNamedef);
        tvadd1def= findViewById(R.id.tvadd1def);
        tvadd2def= findViewById(R.id.tvadd2def);
        tvstatedef= findViewById(R.id.tvstatedef);
        addnewTv= findViewById(R.id.addnewTv);
        etname= findViewById(R.id.etname);
        etadd1= findViewById(R.id.etadd1);
        etadd2= findViewById(R.id.etadd2);
        etstate= findViewById(R.id.etstate);
        btndeliverdef= findViewById(R.id.btndeliverdef);
        btnEdit= findViewById(R.id.btnEdit);
        btnDelete= findViewById(R.id.btnDelete);
        btndeliver2= findViewById(R.id.btndeliver2);



        selectTv.setTypeface(Montserrat_Medium);
        tvHeader.setTypeface(Montserrat_Medium);
        tvNamedef.setTypeface(Montserrat_Medium);
        tvadd1def.setTypeface(Montserrat_Medium);
        tvadd2def.setTypeface(Montserrat_Medium);
        tvstatedef.setTypeface(Montserrat_Medium);
        addnewTv.setTypeface(Montserrat_Medium);
        etname.setTypeface(Montserrat_Medium);
        etadd1.setTypeface(Montserrat_Medium);
        etadd2.setTypeface(Montserrat_Medium);
        etstate.setTypeface(Montserrat_Medium);
        btndeliverdef.setTypeface(Montserrat_Medium);
        btnEdit.setTypeface(Montserrat_Medium);
        btnDelete.setTypeface(Montserrat_Medium);
        btndeliver2.setTypeface(Montserrat_Medium);


        if (language.equals("a")){
            selectTv.setText("تحديد عنوان تسليم");
            btndeliverdef.setText(" التسليم إلي العنوان الافتراضي");
            addnewTv.setText("أضف عنوانا جديدا");
            etname.setHint("الأسم");
            etadd1.setHint("العنوان رقم 1");
            etadd2.setHint("العنوان رقم 2");
            etstate.setHint("ولاية");
            btndeliver2.setText("التوصيل الي هذا العنوان");
            tvHeader.setText("ماي راك");

        }


    }


    public void getLocationOne(){

        if(isStoragePermissionGranted()) {

            gps = new GPSTracker(OfferAddress.this);
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                sendRequest2(latitude,longitude);

            } else {

                gps.showSettingsAlert();

            }

        }
    }

    private void getLocation() {



        if (isStoragePermissionGranted()) {

            gps = new GPSTracker(OfferAddress.this);
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
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OfferAddress.this);
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

    private void sendRequest2(Double mlatitude, Double mlongitude) {


        if (mlatitude !=0.00 && mlongitude !=0.00)
        {


            String lat = mlatitude.toString().trim();
            String lng = mlongitude.toString().trim();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(mlatitude, mlongitude, 1);
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();


                String[] addArray = obj.getAddressLine(0).split(",");

                etadd1.setText(addArray[0]);
                etadd2.setText(obj.getLocality());
                etstate.setText(obj.getAdminArea());


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {


            Toast.makeText(getApplicationContext(),"Couldn't find your Address ",Toast.LENGTH_SHORT).show();

        }



    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) && (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                return true;
            } else {

                ActivityCompat.requestPermissions(OfferAddress.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                ActivityCompat.requestPermissions(OfferAddress.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        } else {

            return true;
        }
    }


    private void getDefaultAdd() {




        Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(OfferAddress.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }


        String url = "http://inviewmart.com/mairak_api/Myprofile.php";

        final boolean finalIsshow = isshow;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        // Log.d("Response", response);

                        if(finalIsshow) {
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
                                    Toast.makeText(getApplicationContext(), "No DATA Found", Toast.LENGTH_SHORT).show();


                                setDefAddress();


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

                        //     System.out.println(error);
                        //    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",userid);
                Log.d("params", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);







    }

    private void setDefAddress() {





        for (int i = 0 ; i<object2.length();i++){

            try {

                tvNamedef.setText(arrylist.get(i).getString("user_name"));
                tvadd1def.setText(arrylist.get(i).getString("address_one"));
                tvadd2def.setText(arrylist.get(i).getString("address_two"));
                tvstatedef.setText(arrylist.get(i).getString("state"));


            } catch (JSONException e) {
                e.printStackTrace();

            }


        }







    }

    public void alert2() {

        final Dialog dialog = new Dialog(OfferAddress.this);
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
                Intent intent = new Intent(getApplicationContext(),DashBoard.class);
                startActivity(intent);
                finish();

            }
        });

        tank.setTypeface(Montserrat_Medium);
        sub.setTypeface(Montserrat_Medium);



        if (language.equals("a")){
            tank.setText("\"شكراً لحجزك معنا في\"ماي راك");
            sub.setText("سيقوم فريق التوصيل الخاص بنا بالوصول إليك قريباً بخصوص الطلب الخاص بك");
        }

    }


}
