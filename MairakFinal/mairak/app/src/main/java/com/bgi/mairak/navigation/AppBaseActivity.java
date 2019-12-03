package com.bgi.mairak.navigation;

/**
 * /*
 * This is a simple and easy approach to reuse the same
 * navigation drawer on your other activities. Just create
 * a base layout that conains a DrawerLayout, the
 * navigation drawer and a FrameLayout to hold your
 * content view. All you have to do is to extend your
 * activities from this class to set that navigation
 * drawer. Happy hacking :)
 * P.S: You don't need to declare this Activity in the
 * AndroidManifest.xml. This is just a base class.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bgi.mairak.models.LoginData;
import com.bgi.mairak.offerlisting.NewOfferlisting;
import com.bgi.mairak.offerlisting.OfferOrderListing;
import com.bgi.mairak.offerlisting.PreOrderList;
import com.bgi.mairak.mairak.DashBoard;
import com.bgi.mairak.mairak.Login;
import com.bgi.mairak.mairak.Order;
import com.bgi.mairak.mairak.Profile;
import com.bgi.mairak.mairak.R;
import com.bgi.mairak.mairak.Settings;
import com.bgi.mairak.util.DatabaseHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AppBaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    private FrameLayout view_stub; //This is the framelayout to keep your content view
    private NavigationView navigation_view; // The new navigation view from Android Design Library. Can inflate menu resources. Easy
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu drawerMenu;
    private LoginData loginData ;


    private String refer ;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String message, statusCode, userid, prodID,language;
    Dialog dialog;

    ImageView ivPropic;
    TextView tvUsername,tvPhnumber;

    Menu menu;
    MenuItem navLogout,navSettings,navProfile,navPreOrderList,navOfferList,navOrders,navOffer,navHome,navShare;

    private DatabaseHelper dhb;


    JSONArray result;
    private JSONArray object2;
    ArrayList<JSONObject> arrylist = new ArrayList<JSONObject>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.app_base_layout);// The base layout that contains your navigation drawer.

        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);
        language = pref.getString("language", "e");

        String Storelink = pref.getString("ref_url","");

        System.out.println(
                "Thiss is" + Storelink
        );

        refer =Storelink+" \nTo get additional benefits use this code: "+pref.getString("refCode","");

        view_stub = (FrameLayout) findViewById(R.id.view_stub);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        View header=navigation_view.getHeaderView(0);

        dhb = new DatabaseHelper(AppBaseActivity.this);

        menu = navigation_view.getMenu();

        ivPropic = header.findViewById(R.id.ivPropic);
        tvUsername = header.findViewById(R.id.tvUsername);
        tvPhnumber = header.findViewById(R.id.tvPhnumber);


        navLogout = menu.findItem(R.id.navLogout);
        navSettings = menu.findItem(R.id.navSettings);
        navProfile = menu.findItem(R.id.navProfile);
        navPreOrderList = menu.findItem(R.id.navPreOrderList);
        navOfferList = menu.findItem(R.id.navOfferList);
        navOrders = menu.findItem(R.id.navOrders);
        navOffer = menu.findItem(R.id.navOffer);
        navHome = menu.findItem(R.id.navHome);
        navShare = menu.findItem(R.id.navShare);






        getProfileData();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerMenu = navigation_view.getMenu();

        for (int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }
        // and so on...
    }
















    @Override
    protected void onResume() {
        super.onResume();

        if (language.equals("a")){

            navSettings.setTitle("الإعدادات");
            navLogout.setTitle("الخروج");
            navProfile.setTitle("الملف الشخصي الخاص بي");
            navPreOrderList.setTitle("الطلبات المقدمة");
            navOfferList.setTitle("عرض الطلبات");
            navOrders.setTitle("الطلبات الخاصة بي");
            navOffer.setTitle("العروض");
            navHome.setTitle("الرئيسية");
            navShare.setTitle("مشاركة – يتقاسم");


        }else{

            navLogout.setTitle("Logout");
            navSettings.setTitle("Settings");
            navProfile.setTitle("My Profile");
            navPreOrderList.setTitle("My Pre Orders");
            navOfferList.setTitle("My Offer Orders");
            navOrders.setTitle("My Orders");
            navOffer.setTitle("Offers");
            navHome.setTitle("Home");
            navShare.setTitle("Share");
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);



    }

    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.navHome) {

            Intent intent = new Intent(getApplicationContext(),DashBoard.class);
            startActivity(intent);


            // Handle the camera action
        } else if (id == R.id.navOffer) {

            Intent intent = new Intent(getApplicationContext(),NewOfferlisting.class);
            startActivity(intent);


        } else if (id == R.id.navOrders) {

            Intent intent = new Intent(getApplicationContext(),Order.class);
            startActivity(intent);


        } else if (id == R.id.navOfferList) {

            Intent intent = new Intent(getApplicationContext(),OfferOrderListing.class);
            startActivity(intent);



        } else if (id == R.id.navProfile) {

            Intent intent = new Intent(getApplicationContext(),Profile.class);
            startActivity(intent);



        }else if (id == R.id.navPreOrderList) {

            Intent intent = new Intent(getApplicationContext(),PreOrderList.class);
            startActivity(intent);



        } else if (id == R.id.navSettings) {

            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);


        } else if (id == R.id.navShare) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,refer);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);


        }else if (id == R.id.navLogout) {

            logOut();



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void logOut() {

        Typeface Montserrat_Medium;

        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");

        dialog = new Dialog(AppBaseActivity.this
        );
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.activity_warning);
        dialog.show();

        TextView logut = dialog.findViewById(R.id.logoutT);
        TextView dou = dialog.findViewById(R.id.dou);

        Button yes = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);


        if (language.equals("a")){
            logut.setText("الخروج");
            dou.setText("هل أنت متأكد أنك تود الخروج ؟");
            yes.setText("نعم");
            no.setText("لا");

        }



        logut.setTypeface(Montserrat_Medium);
        dou.setTypeface(Montserrat_Medium);
        yes.setTypeface(Montserrat_Medium);
        no.setTypeface(Montserrat_Medium);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();



                dhb.deleteNotification();
                dhb.deleteFromProductList();

                editor.remove("loggedIn").commit();
                editor.remove("register").commit();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();


            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });


    }


    public  void onDraweropen() {

        mDrawerLayout.openDrawer(Gravity.START);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }









    private void getProfileData() {




        String url = "http://inviewmart.com/mairak_api/Myprofile.php";


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
                         //   System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                            JSONObject object = new JSONObject();

                            object = result.getJSONArray(0).getJSONObject(0);

                            message = object.getString("message");
                            statusCode = object.getString("status");


                            if (statusCode.equals("1")) {


                                String image;
                                object2 = new JSONArray();
                                object2 = result.getJSONArray(1);


                                int i;

                                for (i = 0; i < object2.length(); i++) {





                                   tvUsername.setText(object2.getJSONObject(i).getString("user_name"));
                                   tvPhnumber.setText(object2.getJSONObject(i).getString("phone_number"));
                                   image = object2.getJSONObject(i).getString("images");

                                   editor.putString("Uname" ,object2.getJSONObject(i).getString("user_name") );
                                   editor.putString("Pnum" ,object2.getJSONObject(i).getString("phone_number"));


                                    Picasso.with(getApplicationContext()).load(image).error(R.drawable.noimage).placeholder(R.drawable.noimage).into(ivPropic);




                                }





                            } else {
                            //    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

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
                     //   System.out.println(error);

                        tvUsername.setText(pref.getString("Uname",""));
                        tvPhnumber.setText(pref.getString("Pnum",""));
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


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






}