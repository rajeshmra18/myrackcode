package com.example.aravind.mairak;

import android.Manifest;
import android.app.Dialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {



    ImageView ivBackarrow;
    TextView tvHeader,tvLangTitle,tvEnglish,tvArabic,tvPromomTile,tvApplay;
    SwitchCompat swEnglish,swArabic;

    EditText etPromoCode;

    LinearLayout llApply;
    String message, statusCode;
    JSONArray result;
    Typeface Montserrat_Medium;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    String language;
    private GPSTracker gps;
    double latitude,longitude;
    TextView tvCartCount;
    boolean isConnected;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    private String userid;
    private String pCode,IMEII;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        getLocation();

        initwidgets();





        chechLang();


        llApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                applyPromo();

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

    private void applyPromo() {


         pCode = String.valueOf(etPromoCode.getText()) ;


        Boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(Settings.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }
        final boolean finalIsshow = isshow;


        String url = "http://inviewmart.com/mairak_api/Promo_code.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (finalIsshow){
                            progressDialog.dismiss();
                        }

                        try {

                            JSONObject response1 = new JSONObject(response);
                            result = new JSONArray();
                            result = response1.getJSONArray("Response");
                      //      System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");


                            if (statusCode.equals("1")) {

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();


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
                params.put("latitude",String.valueOf(latitude));
                params.put("longitude",String.valueOf(longitude));
                params.put("promo_code",pCode);
                params.put("ime_number",IMEII);






                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


        





    }

    private void chechLang() {

        if (language.equals("e")){
            swEnglish.setChecked(true);
            swArabic.setChecked(false);
        }else if (language.equals("a")){
            swArabic.setChecked(true);
            swEnglish.setChecked(false);
        }


        swArabic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swArabic.isChecked()){
                    swEnglish.setChecked(false);
                    swArabic.setChecked(true);
                    editor.putString("language", "a");
                    editor.apply();
                }else {
                    swArabic.setChecked(false);
                    swEnglish.setChecked(true);
                    editor.putString("language", "e");
                    editor.apply();
                }


            }
        });

        swEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (swEnglish.isChecked()){
                    swEnglish.setChecked(true);
                    swArabic.setChecked(false);
                    editor.putString("language", "e");
                    editor.apply();
                }else {
                    swArabic.setChecked(true);
                    swEnglish.setChecked(false);
                    editor.putString("language", "a");
                    editor.apply();
                }



            }
        });




    }

    private void initwidgets() {

        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);

        language =  pref.getString("language", "e");



        ivBackarrow = findViewById(R.id.ivBackarrow);


        tvHeader= findViewById(R.id.tvHeader);
        tvLangTitle= findViewById(R.id.tvLangTitle);
        tvEnglish= findViewById(R.id.tvEnglish);
        tvArabic= findViewById(R.id.tvArabic);

        swEnglish=  findViewById(R.id.swEnglish);
        swArabic=  findViewById(R.id.swArabic);

        tvApplay = findViewById(R.id.tvApplay);
        tvPromomTile = findViewById(R.id.tvPromomTile);

        llApply = findViewById(R.id.llApply);

        tvPromomTile.setTypeface(Montserrat_Medium);
        tvApplay.setTypeface(Montserrat_Medium);

        etPromoCode = findViewById(R.id.etPromoCode);



        if (language.equals("a")){

            tvLangTitle.setText("اختر لغتك المفضلة");
            tvHeader.setText("الإعدادات");

            tvEnglish.setText("الإنجليزية");
            tvArabic.setText("العربية");
            tvEnglish.setGravity(Gravity.LEFT);
            tvArabic.setGravity(Gravity.LEFT);
            tvApplay.setText("تطبيق");

        }





        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


        tvHeader.setTypeface(Montserrat_Medium);
        tvLangTitle.setTypeface(Montserrat_Medium);
        tvEnglish.setTypeface(Montserrat_Medium);
        tvArabic.setTypeface(Montserrat_Medium);

        IMEII = getUniqueIMEIId(this);
      //  System.out.println("IMEIIIIII-----------<>"+ IMEII);

     //   System.out.println("Locattttttt"+ latitude + "   " + longitude);



    }




    public static String getUniqueIMEIId(Context context) {
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
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            Log.e("imei", "=" + imei);
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



    private void getLocation() {


        if (isStoragePermissionGranted()) {

            gps = new GPSTracker(Settings.this);
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
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Settings.this);
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

                ActivityCompat.requestPermissions(Settings.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                ActivityCompat.requestPermissions(Settings.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    private void showAlert(){
        final Dialog dialog = new Dialog(Settings.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),DashBoard.class);
        startActivity(intent);
        finish();
    }
}
