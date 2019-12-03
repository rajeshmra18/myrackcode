package com.mairak.bgi.offerlisting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mairak.bgi.mairak.R;
import com.squareup.picasso.Picasso;

public class OfferDetails extends AppCompatActivity {
    String id;
    ImageView profie;
    TextView headingtxt, detailstxt, startdatetxt, enddatetxt, quantitytxt, pricetxt, Book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        initwidgets();
        setvalues();
    }

    private void initwidgets() {
        getSupportActionBar().hide();
        id = getIntent().getStringExtra("id");
        profie = (ImageView) findViewById(R.id.image);
        headingtxt = (TextView) findViewById(R.id.heading);
        detailstxt = (TextView) findViewById(R.id.details);
        startdatetxt = (TextView) findViewById(R.id.startdate);
        enddatetxt = (TextView) findViewById(R.id.startdate);
        quantitytxt = (TextView) findViewById(R.id.quantity);
        pricetxt = (TextView) findViewById(R.id.price);
        Book = (TextView) findViewById(R.id.BookNow);

    }

    private void setvalues() {

        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("image")).into(profie);
        headingtxt.setText("" + getIntent().getStringExtra("heading"));
        detailstxt.setText("" + getIntent().getStringExtra("detiails"));
        startdatetxt.setText("Start Date: " + getIntent().getStringExtra("startdate"));
        enddatetxt.setText("End Date: " + getIntent().getStringExtra("enddate"));
        quantitytxt.setText("Quantity: " + getIntent().getStringExtra("quantity"));
        pricetxt.setText("Price: " + getIntent().getStringExtra("price"));
    }
}
