package com.bgi.mairak.mairak;

import android.Manifest;
import android.app.Activity;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Profile extends AppCompatActivity {


    TextView regTitle,emp;
    EditText fullname,phonenumb,phonenumb2,address1,address2,pinNum,state,tvEmail;
    Button update;
    CheckBox empchk;

    ImageView locationpin,proPic,imbackarrow,addIMg;

    Typeface Montserrat_Medium;


    String aemail;


    JSONArray result;
    String orgid;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String message,statusCode;
    String userid;

    private ProgressDialog progressDialog;




    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    File file;
    File profilePicUploadFile = null;
    private static String pictureImagePath = "";





    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    Double latitude,longitude;
    LatLng latlng;
    Double currentLat=8.526971;
    Double currentlong=76.898217;

    String language;

    boolean isGPSAlertShown = false;

    LocationManager locationManager;
    String mprovider;
    Double one_longitud,one_latitude;
    Location location;
    boolean isConnected;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;

    TextView HeadMairak;

    GPSTracker gps;


    String empS = "";
    private JSONArray object2;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();


    private static String[] PERMISSIONS_ALL = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA}; //TODO You can Add multiple permissions here.
    private static final int PERMISSION_REQUEST_CODE = 223;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        requestPermission();


        initwidgets();


        getLocation();



        getProfileData();



        locationpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocation();

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                updateProfile();


            }
        });


        imbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        addIMg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                islocatiopermissiongranted();
                selectImage();

            }
        });


    }

    private void requestPermission() {




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean allPermissionsGranted = true;
            ArrayList<String> toReqPermissions = new ArrayList<>();

            for (String permission : PERMISSIONS_ALL) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    toReqPermissions.add(permission);

                    allPermissionsGranted = false;
                }
            }
            if (allPermissionsGranted){


            }
            else{

                ActivityCompat.requestPermissions(this,
                        toReqPermissions.toArray(new String[toReqPermissions.size()]), PERMISSION_REQUEST_CODE);





            }



        }




    }

    private void updateProfile() {





        final String full = fullname.getText().toString();
        final String ph_no = phonenumb.getText().toString();
        final String ph_no2 = phonenumb2.getText().toString();
        final String add1 = address1.getText().toString();
        final String add2 = address2.getText().toString();
        final String stat = state.getText().toString();
        final String pin1 = pinNum.getText().toString();
        final String mail = tvEmail.getText().toString();



        if (empchk.isChecked()){
            empS = "yes";
        }
        else{
            empS = "no";
        }



        String url = "http://inviewmart.com/mairak_api/Profile_update.php";
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {


            @Override
            public void onResponse(NetworkResponse response) {



                try {

                    String resultResponse = new String(response.data);

                    JSONObject response1 =new JSONObject(resultResponse);

                    result = new JSONArray();
                    result = response1.getJSONArray("Response");
                  //  System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                    JSONObject object = new JSONObject();

                    object = result.getJSONArray(0).getJSONObject(0);

                    message = object.getString("message");
                    statusCode = object.getString("status");




                    if(statusCode.equals("1"))
                    {



                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),DashBoard.class);
                        startActivity(intent);

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
                     //   System.out.println(error);
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
                params.put("user_id",userid);
                params.put("full_name",full);
                params.put("phone_number", ph_no);
                params.put("alternate_phonenumber", ph_no2);
                params.put("is_govtemployee",empS);
                params.put("latitude",lat);
                params.put("longitude",lng);
                params.put("address_one", add1);
                params.put("address_two",add2);
                params.put("state",stat);
                params.put("email",mail);
                params.put("pincode",pin1);
                Log.d("params",params.toString());

                return params;
            }




            @Override
            protected Map<String, DataPart> getByteData() {
                if (file == null || !file.exists()) {
                    return null;
                }
                Map<String, DataPart> params = new HashMap<>();
                params.put("profile_image", new DataPart("profile_image", AppHelper.getDataFromFile(file.getAbsolutePath()), "image/jpeg"));

// Log.e(TAG, "image 899898: " + destination+"\ndestination2:"+AppHelper.getDataFromFile(destination.getAbsolutePath())+ "image/jpeg");
                return params;
            }


        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(Profile.this).addToRequestQueue(request);









    }


    private void initwidgets() {


        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid",null);

        language = pref.getString("language","e");

        imbackarrow = findViewById(R.id.imbackarrow);

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        regTitle = findViewById(R.id.regTitle);

        progressDialog = new ProgressDialog(Profile.this);

        emp = findViewById(R.id.employee);


        fullname = findViewById(R.id.fullName);


        phonenumb = findViewById(R.id.phoneNumber);
        phonenumb2 = findViewById(R.id.phoneNumber2);

        HeadMairak = findViewById(R.id.HeadMairak);
        HeadMairak.setTypeface(Montserrat_Medium);

        address1 = findViewById(R.id.Address1);
        address2 = findViewById(R.id.Address2);
        state = findViewById(R.id.state);
        pinNum = findViewById(R.id.pinNum);


        addIMg= findViewById(R.id.addIMg);

        tvEmail= findViewById(R.id.tvEmail);


        locationpin = findViewById(R.id.pin);
        proPic = findViewById(R.id.imProPic);




        empchk = findViewById(R.id.employeechk);
        update = findViewById(R.id.btupdate);





         if (language.equals("a")){

             fullname.setGravity(Gravity.RIGHT);
             fullname.setHint("الاسم بالكامل");

             phonenumb.setGravity(Gravity.RIGHT);
             phonenumb.setHint("رقم الهاتف (1)");

             phonenumb2.setGravity(Gravity.RIGHT);
             phonenumb2.setHint("رقم الهاتف (2)");

             address1.setGravity(Gravity.RIGHT);
             address1.setHint("العنوان (1)");

             address2.setGravity(Gravity.RIGHT);
             address2.setHint("العنوان (2)");

             state.setGravity(Gravity.RIGHT);
             state.setHint("الدولة");

             pinNum.setGravity(Gravity.RIGHT);
             pinNum.setHint("رمز Pin");

             tvEmail.setGravity(Gravity.RIGHT);
             tvEmail.setHint("البريد الإلكتروني");

             emp.setGravity(Gravity.RIGHT);


             update.setText("تحديث");
             emp.setText("هل أنت متأكد أنك تود الخروج ؟");
             HeadMairak.setText("ماي راك");

             regTitle.setText("الملف الشخصي");

         }







        regTitle.setTypeface(Montserrat_Medium);

        fullname.setTypeface(Montserrat_Medium);
        phonenumb.setTypeface(Montserrat_Medium);
        emp.setTypeface(Montserrat_Medium);
        address1.setTypeface(Montserrat_Medium);
        address2.setTypeface(Montserrat_Medium);
        state.setTypeface(Montserrat_Medium);
        pinNum.setTypeface(Montserrat_Medium);



    }












    private void getProfileData() {


        Boolean show = true;

        boolean isshow=false;
       if (show)
        {

           isshow=true;


            progressDialog.show();

        }


        String url = "http://inviewmart.com/mairak_api/Myprofile.php";

         final boolean finalIsshow = isshow;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                       Log.d("ResponsePrro", response);
                       // progressDialog.dismiss();
                        if(finalIsshow) {
                            progressDialog.dismiss();
                        }

                        try {

                          //  progressDialog.dismiss();
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


                                setProfile();


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

                        showAlert();
                   //     System.out.println(error);
                    //    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",userid);
                Log.d("paramsDDDDDDDD", params.toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);





    }

    private void setProfile() {

        String image,govt,email;

        for (int i = 0 ; i<object2.length();i++){

            try {

                fullname.setText(arrylist.get(i).getString("user_name"));
                phonenumb.setText(arrylist.get(i).getString("phone_number"));
                phonenumb2.setText(arrylist.get(i).getString("alternate_phonenumber"));
                address1.setText(arrylist.get(i).getString("address_one"));
                address2.setText(arrylist.get(i).getString("address_two"));
                state.setText(arrylist.get(i).getString("state"));
                pinNum.setText(arrylist.get(i).getString("pincode"));


                email = arrylist.get(i).optString("email");



                if(email != null){
                    tvEmail.setText(arrylist.get(i).getString("email"));
                    aemail = arrylist.get(i).getString("email");
                }
                else{
                    tvEmail.setHint("Please add Email");
                }
                image = arrylist.get(i).getString("images");
                govt = arrylist.get(i).getString("govt_employee");

                if (govt.equals("yes")){
                    empchk.setChecked(true);

                }
                else {
                    empchk.setChecked(false);
                }





                Picasso.with(getApplicationContext()).load(image).error(R.drawable.noimage).placeholder(R.drawable.noimage).into(proPic);




              /*  if (!image.equals("null") ){

                    Picasso.with(getApplicationContext()).load(image).into(proPic);


//                   Picasso.get().load(image).into(proPic);
                }
                else {
                    Picasso.with(getApplicationContext()).load(R.drawable.noimage).into(proPic);

//                  Picasso.get().load(R.drawable.noimage).into(proPic);

                }*/



            } catch (JSONException e) {
                e.printStackTrace();
            //    System.out.println("Exception ****" + e);
            }


        }



    }


    public void getLocation(){




        if(isStoragePermissionGranted()) {

            gps = new GPSTracker(Profile.this);

            // check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                if (latitude.toString().equals("0.0")||longitude.toString().equals("0.0")){
                    gps.showSettingsAlert();
                    Toast.makeText(getApplicationContext(), "Couldnt Fetch Your Current Location Please try again in a bit!! ", Toast.LENGTH_LONG).show();
                }else {
                    sendRequest2(latitude,longitude);
                }

            /*    cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {

                } else {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
                }*/


            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }

        }else {
            gps.showSettingsAlert();
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
                address1.setText(addArray[0]);
                address2.setText(obj.getLocality());
                state.setText(obj.getAdminArea());
                pinNum.setText(obj.getPostalCode());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {


            Toast.makeText(getApplicationContext(),"Couldn't find your Address ",Toast.LENGTH_SHORT).show();

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
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }


    private void sendRequest(){



       progressDialog.show();



        String lat = latitude.toString().trim();
        String lng = longitude.toString().trim();

        final String URLL= "https://maps.googleapis.com/maps/api/geocode/json?&latlng="+lat+","+lng+"&key=AIzaSyC6zkF-3RFyy91rDUpA1vhBmUfD_kapfuU";
        StringRequest stringRequest = new StringRequest(URLL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        // Toast.makeText(getApplicationContext(),"response: "+response,Toast.LENGTH_LONG).show();
                      //  Log.d("response",response);
                       // showJSON(response);






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


                            JSONObject object = new JSONObject();

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

                                address1.setText(address);
                                address2.setText(subaddress);
                                state.setText(statel);
                                pinNum.setText(pin);




                                System.out.println("Heeeeeeeeereee" + address1);


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
                            Toast.makeText(Profile.this, "SORRY THERE IS A NETWORK PROBLEM.... ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        int timeout=10000;


        RetryPolicy policy=new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        //add the request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }



    private void showJSON(String json){

        ParseJSON pj=new ParseJSON(json);
        pj.parseJSON();
        String[] location={"Locality","Address","City","State","PIN"};
        location=ParseJSON.location;

        for(int i = 0 ; i<location.length;i++){
          //  System.out.println(location[i]);
        }

        try {

            address1.setText(location[0]);
            address2.setText(location[1]);
            state.setText(location[4]);
            pinNum.setText(location[7]);

        } catch (ArrayIndexOutOfBoundsException e) {

        }


    }



    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Galleryimage",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    cameraIntent();

                } else if (items[item].equals("Choose from Galleryimage")) {

                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }


        });
        builder.show();
    }


    // TODO: SELECTING IMAGE FROM GALLERY
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    // TODO: SAVING IMAGE TAKEN USING CAMERA
    private void cameraIntent() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.e("file", "=" + storageDir.exists());
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        file = new File(pictureImagePath);
//        Uri outputFileUri = Uri.fromFile(file);

        try {

            Uri outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".my.package.name.provider", file);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(intent, REQUEST_CAMERA);


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Please Grant Permission",Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE) {
                Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
                Crop.of(data.getData(), destination).asSquare().start(this);

            } else if (requestCode == REQUEST_CAMERA) {
                Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
                Crop.of(Uri.fromFile(file), destination).asSquare().start(this);
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
               // System.out.println("image>>>>>" + file);
            }
        }
    }

    private void handleCrop(int resultCode, Intent croppedimage) {

        if (resultCode == RESULT_OK) {
            proPic.invalidate();
            proPic.setImageBitmap(null);
            proPic.setImageURI(Crop.getOutput(croppedimage));
            profilePicUploadFile = new File(Crop.getOutput(croppedimage).getPath());
            file = new File(Crop.getOutput(croppedimage).getPath());


            //System.out.println("nnnnnn"+AppHelper.getDataFromFile(file.getAbsolutePath()));

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(croppedimage).getMessage(), Toast.LENGTH_SHORT).show();
        }


    }




    private boolean islocatiopermissiongranted() {

        if (ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Profile.this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(Profile.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                        126);
            } else {

                ActivityCompat.requestPermissions(Profile.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                        126);
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 126: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {


                }
                return;
            }

        }
    }


    private void showAlert(){
        final Dialog dialog = new Dialog(Profile.this);
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
