package com.bgi.mairak.mairak;

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
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.Gravity.END;
import static android.view.Gravity.START;

public class Register extends AppCompatActivity {


    TextView regTitle,haveacc,relogin,emp;
    EditText fullname,phonenumb,address1,address2,pinNum,state;
    TextInputEditText password,repassword;
    Button joinnw;
    CheckBox empchk;

    ImageView locationpin;

    Typeface Montserrat_Medium;


    JSONArray result;
    String orgid;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String message,statusCode,oTP;
    private ProgressDialog progressDialog;
    private static final int REQUEST_LOCATION = 1;

    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    Double latitude,longitude;
    LatLng latlng;
    Double currentLat=8.526971;
    Double currentlong=76.898217;

    boolean isGPSAlertShown = false;

    LocationManager locationManager;
    String mprovider;
    Double one_longitud,one_latitude;
    Location location;
    boolean isConnected;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;

    LinearLayout etReferalCodeLay;
    TextView tvGetCode;
    EditText etReferalCode;
    ImageView referShow;

    GPSTracker gps;

    String language;


    String empS = "";
    private String userid;
    private String fcmId;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);
        language = pref.getString("language", "e");

        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        fcmId = settings.getString("fcm_token", "");



        progressDialog = new ProgressDialog(Register.this);
        regTitle = findViewById(R.id.regTitle);
        haveacc= findViewById(R.id.alreadyHave);
        relogin = findViewById(R.id.newLogin);
        emp = findViewById(R.id.employee);


        etReferalCode = findViewById(R.id.etReferalCode);
        etReferalCodeLay = findViewById(R.id.etReferalCodeLay);
        referShow = findViewById(R.id.referShow);
        tvGetCode = findViewById(R.id.tvGetCode);

        etReferalCode.setTypeface(Montserrat_Medium);
        tvGetCode.setTypeface(Montserrat_Medium);

       fullname = findViewById(R.id.fullName);
       phonenumb = findViewById(R.id.phoneNumber);


       address1 = findViewById(R.id.Address1);
        address2 = findViewById(R.id.Address2);
        state = findViewById(R.id.state);
        pinNum = findViewById(R.id.pinNum);


        locationpin = findViewById(R.id.pin);

      repassword = findViewById(R.id.rePassword);
      password = findViewById(R.id.Password);

      empchk = findViewById(R.id.employeechk);

      joinnw = findViewById(R.id.joinbutton);


        referShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (etReferalCodeLay.getVisibility()==View.VISIBLE){


                    etReferalCodeLay.setVisibility(View.GONE);
                    referShow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp));



                }else {


                    etReferalCodeLay.setVisibility(View.VISIBLE);
                    referShow.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));

                }

            }
        });




       regTitle.setTypeface(Montserrat_Medium);
        haveacc.setTypeface(Montserrat_Medium);
        relogin.setTypeface(Montserrat_Medium);
        fullname.setTypeface(Montserrat_Medium);
        phonenumb.setTypeface(Montserrat_Medium);
        password.setTypeface(Montserrat_Medium);
        repassword.setTypeface(Montserrat_Medium);
        joinnw.setTypeface(Montserrat_Medium);
        emp.setTypeface(Montserrat_Medium);
        address1.setTypeface(Montserrat_Medium);
        address2.setTypeface(Montserrat_Medium);
        state.setTypeface(Montserrat_Medium);
        pinNum.setTypeface(Montserrat_Medium);


        if (language.equals("a")){
            password.setHint(" ");
            repassword.setHint(" ");



            regTitle.setText("التسجيل");
            haveacc.setText("هل أنت مشترك ؟");
            relogin.setText("الدخول");

            fullname.setHint("الاسم بالكامل");
            phonenumb.setHint("رقم الهاتف");
            phonenumb.setGravity(END);

            joinnw.setText("اشترك الآن");
            emp.setText("موظف حكومة ؟");
            address1.setHint("العنوان (1)");
            address2.setHint("العنوان (2)");
            state.setHint("الدولة");
            pinNum.setHint("رمز Pin");
            password.setHint("رقم المرور");
            repassword.setHint("إعادة كلمة المرور");
            tvGetCode.setText("حصلت على رمز الإحالة؟");
            etReferalCode.setHint("كود الإحالة");




        }else {

            password.setHint("Password");
            phonenumb.setGravity(START);
            repassword.setHint("Retype Password");
        }


        getLocation();


        locationpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //  getGPSData();
                System.out.println("immmheRRE");
                getLocationOne();






            }
        });





        joinnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();
            }
        });


        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });





    }





    public void getLocation(){



        if(isStoragePermissionGranted()) {

            gps = new GPSTracker(Register.this);

            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();



                cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {

                } else {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Register.this);
                    builder.setMessage("Do you Want to Retry ?")
                            .setTitle("No Internet Connection!")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    startActivity(getIntent());
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    dialog.dismiss();
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







    public void getLocationOne(){



        if(isStoragePermissionGranted()) {
            gps = new GPSTracker(Register.this);
            System.out.println("Immmm hrereeeee");
            if (gps.canGetLocation()) {


                latitude =   gps.getLatitude();
                longitude =  gps.getLongitude();

//                latitude = gps.getLatitude();
//                longitude = gps.getLongitude();
                System.out.println("LOCATTIONNNNN..........." + latitude + ":::::::::::::::::" + longitude);

                if (latitude.toString().equals("0.0")||longitude.toString().equals("0.0")){
                    Toast.makeText(getApplicationContext(), "Couldn't Fetch Your Current Location Please try again in a bit!! ", Toast.LENGTH_LONG).show();
                }else {
                    sendRequest(latitude,longitude);
                }



            } else {

                gps.showSettingsAlert();

            }

        }else {
            gps.showSettingsAlert();
        }
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED)) {

                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        }
        else {

            return true;
        }
    }


    private void sendRequest(Double mlatitude, Double mlongitude){














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
                address1.setText(addArray[0]);
                address2.setText(obj.getLocality());
                state.setText(obj.getAdminArea());
                pinNum.setText(obj.getPostalCode());

            } catch (IOException e) {
                e.printStackTrace();
            }

          /*  final String URLL= "https://maps.googleapis.com/maps/api/geocode/json?&latlng="+lat+","+lng+"&key=AIzaSyC6zkF-3RFyy91rDUpA1vhBmUfD_kapfuU";
            System.out.println("URLllll" + URLL);


            StringRequest stringRequest = new StringRequest(URLL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {






                            // Toast.makeText(getApplicationContext(),"response: "+response,Toast.LENGTH_LONG).show();
                            Log.d("responseDDDDDDD",response);
                           // showJSON(response);




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

                                System.out.println("Response = " + result);

                                resultArray = result.getJSONArray("results");

                                System.out.println("ResponseRwa = " + resultArray);
                                JSONObject  object = new JSONObject();

                                statusCode = result.optString("status");

                                System.out.println("STTAttaaaas" + statusCode);

                                if (statusCode.equals("OK"))
                                {
                                    newObject = resultArray.getJSONObject(0);
                                    address_components = newObject.getJSONArray("address_components");
                                    String formatAdd = newObject.getString("formatted_address");
                                    if (formatAdd.equals("")){
                                        Toast.makeText(getApplicationContext(),"Couldnt Find your Address",Toast.LENGTH_LONG).show();

                                    }else {


                                        String[] animalsArray = formatAdd.split(",");
//                                        address1.setText(animalsArray[0]);
//                                        address2.setText(animalsArray[1]);
//                                        state.setText(animalsArray[5]);
//                                        pinNum.setText(animalsArray[6]);
                                    }

                                    System.out.println("newwwwwwwwwwwwwwwwww----->" + newObject.getJSONArray("address_components"));
                                    System.out.println("newwwwwwwwwwwwwwwwwwRRRRRR----->" + address_components.getJSONObject(8));

                                    Toast.makeText(getApplicationContext(),address_components.getJSONObject(0).toString(),Toast.LENGTH_LONG);



                                    address1Long = address_components.getJSONObject(0);
                                    sub = address_components.getJSONObject(1);
                                   // pinmain = address_components.getJSONObject(8);
                                    statemain = address_components.getJSONObject(6);

                                    address  = address1Long.getString("long_name");
                                    subaddress = sub.getString("long_name");
                                    statel =  statemain.getString("long_name");
                                    pin = pinmain.getString("long_name");

                                  //  address1.setText(address);
//                                    address2.setText(subaddress);
//                                    state.setText(statel);
//                                    pinNum.setText(pin);


                                    Toast.makeText(getApplicationContext(),address + subaddress+ statel,Toast.LENGTH_LONG ).show();


                                    System.out.println("Heeeeeeeeereee" + address + " :: " + address2);


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
                                Toast.makeText(Register.this, "SORRY THERE IS A NETWORK PROBLEM.... ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            int timeout=10000;


            RetryPolicy policy=new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);

*/

        }
       else
            {


            Toast.makeText(getApplicationContext(),"Couldn't find your Address ",Toast.LENGTH_SHORT).show();

        }



    }




    private void showJSON(String json){

        ParseJSON pj=new ParseJSON(json);
        pj.parseJSON();
        String[] location={"Locality","Address","City","State","PIN"};
        location=ParseJSON.location;

        for(int i = 0 ; i<location.length;i++){
             System.out.println(location[i]);
        }

        try {

            address1.setText(location[0]);
            address2.setText(location[1]);
            //City.setText(location[3]);
            state.setText(location[4]);
            pinNum.setText(location[7]);
            // Log.d("jsonarray",location[0]);

    //      Locality.setText(location[0]);
    //      Address.setText(location[1]);
//      City.setText(location[3]);
//      State.setText(location[4]);
//      Pin.setText(location[6]);


            // Log.d("jsonarray",location[0]);
        } catch (ArrayIndexOutOfBoundsException e) {

        }


    }















    private void fieldValidation() {
        if (phonenumb.getText().toString().length() <= 0) {
            phonenumb.setError("Mandatory");
            phonenumb.requestFocus();
        }else if (phonenumb.getText().toString().length() <= 9){
            phonenumb.setError("Enter a valid number");
            phonenumb.requestFocus();
        }
//        else if (password.getText().toString().length() <= 0) {
//            password.setError("Mandatory");
//            password.requestFocus();
//        }
        else if (fullname.getText().toString().length() <= 0) {
            fullname.setError("Mandatory");
            fullname.requestFocus();
        }

        else if (address1.getText().toString().length() <= 0) {
            address1.setError("Mandatory");
            address1.requestFocus();
        }
        else if (address2.getText().toString().length() <= 0) {
            address2.setError("Mandatory");
            address2.requestFocus();
        }
        else if (state.getText().toString().length() <= 0) {
            state.setError("Mandatory");
            state.requestFocus();
        }
        /*else if (pinNum.getText().toString().length() <= 0) {
            pinNum.setError("Mandatory");
        }*/
//        else if (!password.getText().toString().equals(repassword.getText().toString())){
//            Toast.makeText(getApplicationContext(),"CHECK THE PASSWORD YOU HAVE ENTERED",Toast.LENGTH_SHORT).show();
//            password.setError("Incorrect");
//            repassword.setError("Incorrect");
//            password.requestFocus();
//        }

        else
        {
            register();

        }
    }

    public void register(){







        Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;


            progressDialog.show();
        }
        final boolean finalIsshow = isshow;


        final String full = fullname.getText().toString();
        final String ph_no = phonenumb.getText().toString();
        final String pass = password.getText().toString();
        final String add1 = address1.getText().toString();
        final String add2 = address2.getText().toString();
        final String stat = state.getText().toString();
        final String pin1 = pinNum.getText().toString();
        final String repass = repassword.getText().toString();
        final String referral = etReferalCode.getText().toString().trim();


        if (empchk.isChecked()){
            empS = "yes";
        }
        else{
            empS = "no";
        }



        String url = "http://inviewmart.com/mairak_api/Register.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);




                        if(finalIsshow) {
                            progressDialog.dismiss();
                        }


                        try {

                            JSONObject response1 =new JSONObject(response);

                            result = new JSONArray();
                            result = response1.getJSONArray("Response");
                             System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");






                            if(statusCode.equals("1"))
                            {

                                oTP = object.getString("otp");
                                editor.putString("phoneNumber",ph_no);
                                editor.putString("regOTP",oTP);
                                editor.putString("register","y");
                                editor.commit();

                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),OTP_Reader.class);
                                startActivity(intent);

                                  finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //  Log.d("Error.Response", response);
                    //    System.out.println(error);
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {


                String lat = latitude.toString().trim();
                String lng = longitude.toString().trim();

                Map<String, String>  params = new HashMap<String, String>();
                params.put("full_name",full);
                params.put("phone_number", ph_no);
              //  params.put("password", pass);
              //  params.put("repassword",repass);
                params.put("is_govtemployee",empS);
                params.put("latitude",lat);
                params.put("longitude",lng);
                params.put("refcode",referral);
                params.put("address_one", add1);
                params.put("address_two",add2);
                params.put("state",stat);
                params.put("pincode",pin1);
                params.put("fcm_id", fcmId);
                params.put("language", language);
                Log.d("params",params.toString());

              //  System.out.println("Fuull "+full+"   phnoooooo  "+ph_no+"   pass "+pass + "    empsssssss " + empS + "   laaatt " +lat + "  longg "+ lng);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);





    }





    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }





    }






    private void showAlert(){
        final Dialog dialog = new Dialog(Register.this);
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



    private void getGPSData() {


        if (ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Register.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                latitude = latti;
                longitude = longi;


            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                latitude = latti;
                longitude = longi;




            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                latitude = latti;
                longitude = longi;


                getAddress(latitude,longitude);

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }




    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(Register.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);



            //  if (addresses != null && addresses.size() > 0){

            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);


            address1.setText( obj.getLocality());
            address2.setText(obj.getSubAdminArea());
            //City.setText(location[3]);
            state.setText(obj.getAdminArea());
            pinNum.setText(obj.getPostalCode());

            //  }



            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
