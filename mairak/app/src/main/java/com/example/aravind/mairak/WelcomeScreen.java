package com.example.aravind.mairak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class WelcomeScreen extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    Button start;

    SliderLayout sliderLayout;
    HashMap<String,Integer> Hash_file_maps ;
    private Typeface Montserrat_Medium;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String userid;
    private String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        start = findViewById(R.id.startNow);

        pref = getApplicationContext().getSharedPreferences("miarak", Context.MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("userid", null);
        language = pref.getString("language", "e");

        if (language.equals("a")){

            start.setText("ابدأ الآن");

        }



        Montserrat_Medium = Typeface.createFromAsset(getAssets(), "font/Montserrat_medium/Montserrat-Medium.ttf");
        start.setTypeface(Montserrat_Medium);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();

            }
        });



        final float density = getResources().getDisplayMetrics().density;
        Hash_file_maps = new HashMap<String, Integer>();

        sliderLayout =  findViewById(R.id.slider);
        sliderLayout.getPagerIndicator().setDefaultIndicatorColor(getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorPrimary));
        sliderLayout.getPagerIndicator().setDefaultSelectedIndicatorSize(12,12,null);
          Hash_file_maps.put("Nish1", R.drawable.image2);
         Hash_file_maps.put("Nish2",R.drawable.imslider);
//        Hash_file_maps.put("Nish3",R.drawable.nish2);
//        Hash_file_maps.put("Nish4",R.drawable.nish3);
//        Hash_file_maps.put("Nish5", R.drawable.nish4);
//        Hash_file_maps.put("Nish6", R.drawable.nish5);
//        Hash_file_maps.put("Nish7", R.drawable.nish6);
//        Hash_file_maps.put("Nish8", R.drawable.nish7);
//        Hash_file_maps.put("Nish9", R.drawable.nish8);
//        Hash_file_maps.put("Nish10", R.drawable.nish9);
//        Hash_file_maps.put("Nish11", R.drawable.nish10);
//        Hash_file_maps.put("Nish12", R.drawable.nishbanner2);



        for(String name : Hash_file_maps.keySet()){

            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView
                    //.description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);


        }
        sliderLayout.stopAutoCycle();

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
      //  sliderLayout.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
       sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //sliderLayout.setCustomAnimation(new DescriptionAnimation());
        //sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);
    }



    @Override
    public void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        //Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}








}


