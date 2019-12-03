package com.bgi.mairak.mairak;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OTP_Reader extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    Button btOTPSub;
    EditText etOTP;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String language,oTP,phone_number;
    private ProgressDialog progressDialog;


    private String userid;
    private JSONArray result;
    private String message,statusCode;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp__reader);

        checkAndRequestPermissions();

        btOTPSub = findViewById(R.id.btOTPSub);
        etOTP = findViewById(R.id.etOTP);




        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();


        language = pref.getString("language", "e");

       oTP =  pref.getString("regOTP","");
       phone_number=pref.getString("phoneNumber","");

//        registerReceiver(receiver,new IntentFilter("otp"));


        if (language.equals("a")){

            btOTPSub.setText("خضع");


        }


        btOTPSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fieldValidation();


            }
        });







    }

    private void fieldValidation() {



        if(etOTP.getText().toString().trim() == ""){

            etOTP.setError("OTP Required !");

        }else {



            sendOTP();
           // subOTP();


        }



    }

    private void subOTP() {





      /*  if (etOTP.getText().toString().trim().equals(oTP)){

            Intent intent = new Intent(getApplicationContext(),WelcomeScreen.class);
            startActivity(intent);
            finish();

        }else {

            Toast.makeText(getApplicationContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();
        }*/


    }





    private void sendOTP() {


            progressDialog = new ProgressDialog(OTP_Reader.this);
            progressDialog.show();

        String url = "http://inviewmart.com/mairak_api/Otp_verification.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                            progressDialog.dismiss();


                        try {

                            JSONObject response1 =new JSONObject(response);

                            result = new JSONArray();
                            result = response1.getJSONArray("Response");
                            System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");



                            String ref_code ;


                            if(statusCode.equals("1"))
                            {





                                editor.putString("refCode",object.getString("ref_code"));
                                editor.putString("ref_url",object.getString("ref_url"));
                                editor.putString("loggedIn", "y");
                                editor.putString("userid",object.getString("user_id"));
                                editor.commit();


                                Intent intent = new Intent(getApplicationContext(),DashBoard.class);
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



                Map<String, String>  params = new HashMap<String, String>();
                params.put("otp",etOTP.getText().toString().trim());
                params.put("phone_number", phone_number);
                params.put("language", language);

                Log.d("params",params.toString());

                //  System.out.println("Fuull "+full+"   phnoooooo  "+ph_no+"   pass "+pass + "    empsssssss " + empS + "   laaatt " +lat + "  longg "+ lng);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);














    }


    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            int receiveSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
            int readSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
                return false;
            }
            return true;
        }
        return true;

    }



    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                final String sender = intent.getStringExtra("Sender");
                etOTP.setText(message.replaceAll("\\D+", ""));
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                Log.e("OTP MESSSGE", message);
            }
        }
    };



    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


}
