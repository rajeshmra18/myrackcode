package com.bgi.mairak.mairak;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bgi.mairak.util.NetworkCheck;

public class SplashScreen extends AppCompatActivity {


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    String userid, orgid;
    private SharedPreferences prefs;
    Typeface Montserrat_Medium;
    private NetworkCheck networkCheck = new NetworkCheck(SplashScreen.this);

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };


    private static final int CODE_WRITE_SETTINGS_PERMISSION = 332;
    private static String[] PERMISSIONS_ALL = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.ACCESS_NETWORK_STATE }; //TODO You can Add multiple permissions here.
    private static final int PERMISSION_REQUEST_CODE = 223;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;

        TextView power = findViewById(R.id.powered);
        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        power.setTypeface(Montserrat_Medium);

        prefs = getSharedPreferences("miarak", MODE_PRIVATE);

        if (networkCheck.isConnectingToInternet()){


            isStoragePermissionGranted();

        }else {


          //  showAlert();
            isStoragePermissionGranted();

        }






    }

    public void isStoragePermissionGranted() {

        delayFlow();

   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean allPermissionsGranted = true;
            ArrayList<String> toReqPermissions = new ArrayList<>();

            for (String permission : PERMISSIONS_ALL) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    toReqPermissions.add(permission);

                    allPermissionsGranted = false;
                }
            }
            if (allPermissionsGranted)

                delayFlow();

            else{

                ActivityCompat.requestPermissions(this,
                        toReqPermissions.toArray(new String[toReqPermissions.size()]), PERMISSION_REQUEST_CODE);


                delayFlow();


            }



        }else {
            delayFlow();
        }*/
    }




    /*public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED) &&(checkSelfPermission(Manifest.permission.CAMERA )== PackageManager.PERMISSION_GRANTED)) {

                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }*/

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ) {
                        // process the normal flow
                        //else any one or both the permissions are not granted
                        delayFlow();
                    }
                }
            }
        }

    }
*/


    private void delayFlow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  if (isNetworkAvailable())
                {
                    if (prefs.contains("loggedIn") && prefs.getString("loggedIn", "").equals("y")) {


                        Intent i = new Intent(SplashScreen.this, DashBoard.class);
                        startActivity(i);


                        finish();
                    } else {
                        Intent i = new Intent(SplashScreen.this, FirstScreen.class);
                        startActivity(i);


                        finish();
                    }


                }
                //    else
                {

                }
                //showAlert();
            }


        }, SPLASH_TIME_OUT);
    }







    private void showAlert(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView retry = dialog.findViewById(R.id.retry);
        final TextView cancel = dialog.findViewById(R.id.cancel);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionGranted();
                dialog.dismiss();
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
