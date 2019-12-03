package com.mairak.bgi.mairak;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class FirstScreen extends AppCompatActivity {

    Typeface Montserrat_Medium;
    Button login,register;
    TextView tvLangTitle,tvEnglish,tvArabic;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String language;

    SwitchCompat swEnglish,swArabic;

    private static final int CODE_WRITE_SETTINGS_PERMISSION = 332;

    private static String[] PERMISSIONS_ALL = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE
    }; //TODO You can Add multiple permissions here.
    private static final int PERMISSION_REQUEST_CODE = 223;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);



        requestPermission();





        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        language =  pref.getString("language", "e");

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");


        login = findViewById(R.id.loginBut);
        register = findViewById(R.id.regBut);


        login.setTypeface(Montserrat_Medium);
        register.setTypeface(Montserrat_Medium);


        tvLangTitle = findViewById(R.id.tvLangTitle);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvArabic = findViewById(R.id.tvArabic);


        swEnglish=  findViewById(R.id.swEnglish);
        swArabic=  findViewById(R.id.swArabic);


        tvLangTitle.setTypeface(Montserrat_Medium);
        tvEnglish.setTypeface(Montserrat_Medium);
        tvArabic.setTypeface(Montserrat_Medium);


        chechLang();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                finish();
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

                }else {

                    swArabic.setChecked(false);
                    swEnglish.setChecked(true);

                }


                editor.putString("language", "a");
                editor.apply();


            }
        });

        swEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (swEnglish.isChecked()){
                    swEnglish.setChecked(true);
                    swArabic.setChecked(false);
                }else {
                    swArabic.setChecked(true);
                    swEnglish.setChecked(false);
                }

                editor.putString("language", "e");
                editor.apply();

            }
        });




    }





}
