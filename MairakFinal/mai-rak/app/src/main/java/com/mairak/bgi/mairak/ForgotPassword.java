package com.mairak.bgi.mairak;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {


    TextView loginTitle;
    EditText etEmail;
    Button loginbutton,btCancel;

    JSONArray result;
    String message,statusCode;

    Typeface Montserrat_Medium;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        initwidgets();


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });


    }

    private void initwidgets() {



        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        loginTitle = findViewById(R.id.loginTitle);
        etEmail = findViewById(R.id.etEmail);
        loginbutton = findViewById(R.id.loginbutton);
        btCancel = findViewById(R.id.btCancel);


        loginTitle.setTypeface(Montserrat_Medium);
        etEmail.setTypeface(Montserrat_Medium);
        loginbutton.setTypeface(Montserrat_Medium);
        btCancel.setTypeface(Montserrat_Medium);


    }



    private void fieldValidation() {
        if (etEmail.getText().toString().length() <= 0) {
            etEmail.setError("Mandatory");
        }
        else
        {
            reset();

        }
    }

    private void reset() {

        boolean show = true;

        boolean isshow=false;
        if (show) {

            isshow=true;

            progressDialog = new ProgressDialog(ForgotPassword.this);
            progressDialog.show();
            //progressDialog.setContentView(R.layout.progress_dialog);
        }


        String url = "http://inviewmart.com/mairak_api/Forgot_password.php";

        final boolean finalIsshow = isshow;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                      //  Log.d("Response", response);

                        if(finalIsshow) {
                            progressDialog.dismiss();
                        }


                        try {

                            JSONObject response1 =new JSONObject(response);

                            result = new JSONArray();
                            result = response1.getJSONArray("Response");
                           // System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();
                            JSONObject object2 = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");




                            if(statusCode.equals("1"))
                            {


                                Toast.makeText(ForgotPassword.this, "New password sent to you email", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(),Login.class);
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
                        showAlert();
                      //  System.out.println(error);
                       // Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", etEmail.getText().toString());
                params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);



    }




    private void showAlert(){
        final Dialog dialog = new Dialog(ForgotPassword.this);
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
