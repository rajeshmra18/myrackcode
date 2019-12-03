package com.example.aravind.mairak;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aravind.models.LoginData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextView loginText,forgotpass,newhere,joinnow;
    EditText phonenumb;
    Button login;
    TextInputEditText password;

    Typeface Montserrat_Medium;

    private ProgressDialog progressDialog;
    String language;
    JSONArray result;
    String userid,refCode;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String message,statusCode;
    private String fcmId;
    private SharedPreferences settings;
    private LoginData loginData = new LoginData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("miarak",MODE_PRIVATE);
        editor = pref.edit();

        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        fcmId = settings.getString("fcm_token", "");

        language = pref.getString("language", "e");

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        loginText = findViewById(R.id.loginTitle);
        forgotpass = findViewById(R.id.forgotPass);
        newhere = findViewById(R.id.newHere);
        joinnow = findViewById(R.id.joinNow);



        loginText.setTypeface(Montserrat_Medium);
        forgotpass.setTypeface(Montserrat_Medium);
        newhere.setTypeface(Montserrat_Medium);
        joinnow.setTypeface(Montserrat_Medium);


        phonenumb = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.Password);

        login = findViewById(R.id.loginbutton);

        login.setTypeface(Montserrat_Medium);
        phonenumb.setTypeface(Montserrat_Medium);
        password.setTypeface(Montserrat_Medium);


        if (language.equals("a")){

        loginText.setText("الدخول");
        forgotpass.setText("هل نسيت كلمة السر ؟");
        newhere.setText("جديد هنا");
        joinnow.setText("اشترك الآن");

        login.setText("الدخول");
        phonenumb.setHint("رقم الهاتف");
        password.setHint("رقم المرور");
        }
        else {
            password.setHint("Password");
        }












        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();
            }
        });



        joinnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });


        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });


    }



    private void fieldValidation() {
        if (phonenumb.getText().toString().length() <= 0) {
            phonenumb.setError("Mandatory");
        } else if (password.getText().toString().length() <= 0) {
            password.setError("Mandatory");
        }
        else
        {
            logins();

        }
    }



    public void logins(){


        final String ph_no = phonenumb.getText().toString();
        final String pass = password.getText().toString();

        boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }


       String url = "http://inviewmart.com/mairak_api/Login.php";

        final boolean finalIsshow = isshow;

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
                          //  System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();
                            JSONObject object2 = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);


                            message = object.getString("message");
                            statusCode = object.getString("status");



                            if(statusCode.equals("1"))
                            {
//                              System.out.println("LOOOginObjectt----------->" + result.getJSONArray(1).getJSONObject(0) + " :::::::::: " + result);
                                object2 = result.getJSONArray(1).getJSONObject(0);
                                userid = object2.getString("user_id");
                                refCode = object2.getString("ref_code");
                                loginData.setUserID(userid);
                                loginData.setRefCode(refCode);

                                System.out.println("userrrrrrrrrrrrrr" + loginData.getRefCode());


                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                                editor.putString("refCode",refCode);
                                editor.putString("ref_url",object2.getString("ref_url"));
                                editor.putString("loggedIn", "y");
                                editor.putString("phonenum", ph_no);
                                editor.putString("userid",userid);


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

                        if(finalIsshow) {
                            progressDialog.dismiss();
                        }

                        showAlert();

                    //    System.out.println(error);
                      //  Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("phone_number", ph_no);
                params.put("password", pass);
                params.put("fcm_id", fcmId);
                params.put("language", language);

                System.out.println("PArammmssss"+ params);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);


    }




    private void showAlert(){
        final Dialog dialog = new Dialog(Login.this);
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
