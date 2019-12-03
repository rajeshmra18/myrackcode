package com.example.aravind.offerlisting.Adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aravind.offerlisting.Model.OfferModel;
import com.example.aravind.offerlisting.NewOfferlisting;
import com.example.aravind.mairak.GPSTracker;
import com.example.aravind.mairak.ProgressDialog;
import com.example.aravind.mairak.R;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyHolder> {
    private Context context;
    public ArrayList<OfferModel> models;
    Double latitude, longitude;
    String placename = "";
    LatLng latlng;
    Double currentLat = 8.526971;
    Double currentlong = 76.898217;
    NewOfferlisting NewOfferlisting;
    boolean isGPSAlertShown = false;
    Dialog dialog;
    LocationManager locationManager;
    String mprovider;
    Double one_longitud, one_latitude;
    Location location;
    boolean isConnected;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    private ProgressDialog progressDialog;
    GPSTracker gps;
    private JSONArray result, object2;
    private String message, statusCode;
    String userid;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String language;

    public OfferAdapter(NewOfferlisting newOfferlisting, ArrayList<OfferModel> models, Context applicationContext) {
        this.context = applicationContext;
        this.models = models;
        this.NewOfferlisting = newOfferlisting;

    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offeritem, parent, false);
        OfferAdapter.MyHolder myholder = new OfferAdapter.MyHolder(view);
        pref = context.getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);
        language = pref.getString("language", "e");

        return myholder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {





        Picasso.with(context).load(models.get(position).getImage()).into(holder.Offerimage);
        holder.details.setText("" + models.get(position).getDetails());
        holder.heading.setText("" + models.get(position).getHeading());

        holder.quantity.setText("Quantity : " + models.get(position).getQuantity());
        holder.price.setText("AED." + models.get(position).getPrice());
        holder.date.setText("" + models.get(position).getStartdate() + "  to  " + models.get(position).getEnddate());



        if (language.equals("a")){


            holder.quantity.setText("الجودة : " + models.get(position).getQuantity());
            holder.date.setText("" + models.get(position).getStartdate() + "  إلى  " + models.get(position).getEnddate());


        }



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                Intent newactivity = new Intent(context, OfferDetails.class);
//                newactivity.putExtra("image", models.get(position).getImage());
//                newactivity.putExtra("heading", models.get(position).getHeading());
//                newactivity.putExtra("detiails", models.get(position).getDetails());
//                newactivity.putExtra("startdate", models.get(position).getStartdate());
//                newactivity.putExtra("enddate", models.get(position).getEnddate());
//                newactivity.putExtra("quantity", models.get(position).getQuantity());
//                newactivity.putExtra("id", models.get(position).getId());
//                newactivity.putExtra("price", models.get(position).getPrice());
//
//                context.startActivity(newactivity);


                dialog = new Dialog(NewOfferlisting);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_offer_details);

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.show();


                ImageView prodImage, close;
                ImageView profie;
                TextView headingtxt, detailstxt, startdatetxt, enddatetxt, quantitytxt, pricetxt, Book;


                profie = (ImageView) dialog.findViewById(R.id.image);
                headingtxt = (TextView) dialog.findViewById(R.id.heading);
                detailstxt = (TextView) dialog.findViewById(R.id.details);
                startdatetxt = (TextView) dialog.findViewById(R.id.startdate);
                enddatetxt = (TextView) dialog.findViewById(R.id.enddate);
                quantitytxt = (TextView) dialog.findViewById(R.id.quantity);
                pricetxt = (TextView) dialog.findViewById(R.id.price);
                Book = (TextView) dialog.findViewById(R.id.BookNow);


                Picasso.with(context).load(models.get(position).getImage()).into(profie);
                headingtxt.setText("" + models.get(position).getHeading());
                detailstxt.setText("" + models.get(position).getDetails());
                startdatetxt.setText("Start Date: " + models.get(position).getStartdate());
                enddatetxt.setText("End Date: " + models.get(position).getEnddate());
                quantitytxt.setText("Quantity: " + models.get(position).getQuantity());
                pricetxt.setText("Price: " + models.get(position).getPrice());


                headingtxt.setTypeface(models.get(position).getMontserrat_Medium());
                detailstxt.setTypeface(models.get(position).getMontserrat_Medium());
                startdatetxt.setTypeface(models.get(position).getMontserrat_Medium());
                enddatetxt.setTypeface(models.get(position).getMontserrat_Medium());
                quantitytxt.setTypeface(models.get(position).getMontserrat_Medium());
                pricetxt.setTypeface(models.get(position).getMontserrat_Medium());
                Book.setTypeface(models.get(position).getMontserrat_Medium());



                if (language.equals("a")){
                    Book.setText("الحجز");
                    quantitytxt.setText("الجودة: " + models.get(position).getQuantity());
                    pricetxt.setText("السعر: " + models.get(position).getPrice());
                    startdatetxt.setText("تاريخ البداية: " + models.get(position).getStartdate());
                    enddatetxt.setText("تاريخ الانتهاء: " + models.get(position).getEnddate());
                }


                Book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bookdata(models.get(position).getId());


                    }
                });


            }
        });


        holder.heading.setTypeface(models.get(position).getMontserrat_Medium());
        holder.details.setTypeface(models.get(position).getMontserrat_Medium());
        holder.date.setTypeface(models.get(position).getMontserrat_Medium());
        holder.quantity.setTypeface(models.get(position).getMontserrat_Medium());
        holder.price.setTypeface(models.get(position).getMontserrat_Medium());


    }

    private void Bookdata(String id) {
        getLocation();
       // System.out.println("latlonggg" + latitude + "   " + longitude);
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            placename = addresses.get(0).getLocality();
           // System.out.println(addresses.get(0).getLocality());
        } else {
            // do your stuff
        }

        bookwebcall(id);
    }

    private void bookwebcall(final String id) {
        {

            progressDialog = new ProgressDialog(NewOfferlisting);
            progressDialog.show();
            pref = context.getSharedPreferences("miarak", Context.MODE_PRIVATE);
            editor = pref.edit();
            userid = pref.getString("userid", "0");
           // System.out.println("userrrrrrr" + userid + "  " + latitude + " " + longitude + " " + placename + " " + id + " ");
            String url = "http://inviewmart.com/mairak_api/Offer_order.php";
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
                              //  System.out.println("responseeeeeeeeeeeeeeeeeeeeee" + result);

                                JSONObject object = new JSONObject();

                                object = result.getJSONArray(0).getJSONObject(0);

                                message = object.getString("message");
                                statusCode = object.getString("status");
                                dialog.dismiss();
                                progressDialog.dismiss();

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                               // System.out.println("eccce0" + e);
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            //  Log.d("Error.Response", response);
                          //  System.out.println(error);
                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();


                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", userid);
                    params.put("offer_id", id);
                    params.put("order_place", placename);
                    params.put("latitude", String.valueOf(latitude));
                    params.put("longitude", String.valueOf(longitude));
                    params.put("secret_hash", "W9zUnkWf5wJS27Yb2Nmmz3T");


                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(postRequest);

        }
    }

    private void getLocation() {


        if (isStoragePermissionGranted()) {

            gps = new GPSTracker(context);
            // check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();


                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {

                } else {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
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
            if ((context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) && (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                return true;
            } else {

                ActivityCompat.requestPermissions(NewOfferlisting, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                ActivityCompat.requestPermissions(NewOfferlisting, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView Offerimage;
        TextView heading, details, date, quantity, price;
        CardView cardView;

        public MyHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            price = (TextView) itemView.findViewById(R.id.price);

            heading = (TextView) itemView.findViewById(R.id.offerheading);
            details = (TextView) itemView.findViewById(R.id.offerdetails);
            Offerimage = (ImageView) itemView.findViewById(R.id.offerimage);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
