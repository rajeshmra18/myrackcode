package com.example.aravind.cart;

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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aravind.mairak.DashBoard;
import com.example.aravind.mairak.GPSTracker;
import com.example.aravind.mairak.ProgressDialog;
import com.example.aravind.mairak.R;
import com.example.aravind.util.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddressConfirm extends AppCompatActivity {

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

    private String language,userid,message,statusCode;
    private Typeface Montserrat_Medium;
    private ProgressDialog progressDialog;
    private JSONArray object2;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_confirm);

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
                cartOrder(address);


            }
        });


        btndeliver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (etname.getText().toString().trim().equals("")){
                    etname.setError("Enter your Name");
                    etname.requestFocus();
                }else if (etadd1.getText().toString().trim().equals("")){

                    etadd1.setError("Enter your Name");
                    etadd1.requestFocus();
                }else if (etadd2.getText().toString().trim().equals("")){

                    etadd2.setError("Enter your Name");
                    etadd2.requestFocus();
                }else if (etstate.getText().toString().trim().equals("")){

                    etstate.setError("Enter your Name");
                    etstate.requestFocus();
                }else {


                    Dhb.addToAddressBook(etname.getText().toString().trim(),etadd1.getText().toString().trim(),etadd2.getText().toString().trim(),etstate.getText().toString().trim());

                    address = etname.getText().toString().trim()+","+etadd1.getText().toString().trim()+","+etadd2.getText().toString().trim()+","+etstate.getText().toString().trim();
                    cartOrder(address);

                }


            }
        });


        ivBackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),DashBoard.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void cartOrder(final String address) {


        Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(AddressConfirm.this);
            progressDialog.show();

        }
        final boolean finalIsshow = isshow;



        String url = "http://inviewmart.com/mairak_api/Cart_order.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if(finalIsshow) {
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",userid);
                params.put("lat",String.valueOf(latitude));
                params.put("lng",String.valueOf(longitude));
                params.put("address",address);

                System.out.println("REPArraamssss"+ params);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);








    }

    private void getDefaultAdd() {




        Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(AddressConfirm.this);
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



    private void showAlert(){
        final Dialog dialog = new Dialog(AddressConfirm.this);
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


    private void getLocation() {



        if (isStoragePermissionGranted()) {

            gps = new GPSTracker(AddressConfirm.this);
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
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddressConfirm.this);
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

                ActivityCompat.requestPermissions(AddressConfirm.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                ActivityCompat.requestPermissions(AddressConfirm.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        } else {

            return true;
        }
    }



    public void alert2() {

        final Dialog dialog = new Dialog(AddressConfirm.this);
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

    private void initwidgets() {

        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        Dhb = DatabaseHelper.getInstance(this);
        userid = pref.getString("userid", null);
        language = pref.getString("language", "e");

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



    }



    public void getLocationOne(){

        if(isStoragePermissionGranted()) {

            gps = new GPSTracker(AddressConfirm.this);
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                sendRequest2(latitude,longitude);

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
                etstate.setText(obj.getPostalCode());


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {


            Toast.makeText(getApplicationContext(),"Couldn't find your Address ",Toast.LENGTH_SHORT).show();

        }



    }


    private void sendRequest(){





        progressDialog = new ProgressDialog(AddressConfirm.this);
        progressDialog.show();





        System.out.println("Currrentlocation"+latitude.toString().trim() + ":::::::::" + longitude.toString().trim() );


        if (latitude!=null && longitude!=null)
        {


            String lat = latitude.toString().trim();
            String lng = longitude.toString().trim();

            final String URLL= "https://maps.googleapis.com/maps/api/geocode/json?&latlng="+lat+","+lng+"&key=AIzaSyC6zkF-3RFyy91rDUpA1vhBmUfD_kapfuU";
            StringRequest stringRequest = new StringRequest(URLL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {







                            System.out.println("URLllll" + URLL);
                            // Toast.makeText(getApplicationContext(),"response: "+response,Toast.LENGTH_LONG).show();
                            Log.d("response",response);
                            // showJSON(response);


                            progressDialog.dismiss();

                            try {
                                JSONObject result = new JSONObject(response);



                                JSONArray resultArray;
                                JSONArray address_components = new JSONArray();
                                JSONObject newObject = new JSONObject();
                                JSONObject address1Long = new JSONObject();
                                JSONObject sub = new JSONObject();
                                JSONObject pinmain = new JSONObject();
                                JSONObject statemain = new JSONObject();

                                String address,subaddress,statel,pin  ;

                                System.out.println("Response = " + response);
                                resultArray = new JSONArray();
                                resultArray = result.optJSONArray("results");


                                JSONObject  object = new JSONObject();

                                statusCode = result.optString("status");

                                if (statusCode.equals("OK"))
                                {

                                    newObject = resultArray.optJSONObject(0);
                                    address_components = newObject.optJSONArray("address_components");
                                    address1Long = address_components.getJSONObject(0);
                                    sub = address_components.getJSONObject(1);
                                    pinmain = address_components.getJSONObject(8);
                                    statemain = address_components.getJSONObject(6);

                                    address  = address1Long.getString("long_name");
                                    subaddress = sub.getString("long_name");
                                    statel =  statemain.getString("long_name");
                                    pin = pinmain.getString("long_name");

                                    etadd1.setText(address);
                                    etadd2.setText(subaddress);
                                    etstate.setText(statel);








                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError) {
                                Toast.makeText(AddressConfirm.this, "SORRY THERE IS A NETWORK PROBLEM.... ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            int timeout=10000;


            RetryPolicy policy=new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            //add the request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);



        }else {


            Toast.makeText(getApplicationContext(),"Couldn't find your Address ",Toast.LENGTH_SHORT).show();

        }



    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),DashBoard.class);
        startActivity(intent);
        finish();
    }
}
