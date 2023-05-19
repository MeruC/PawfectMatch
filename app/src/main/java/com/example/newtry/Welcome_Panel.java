package com.example.newtry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class Welcome_Panel extends AppCompatActivity {

    Button btnClick;
    ViewPager2 viewPager2;
    TextView textView;
    private Handler slideHandler = new Handler();

    @Override
    public void onBackPressed() {
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_panel);
    initialize();
    viewPager2 = findViewById(R.id.view_Pager);

    List<SlideItem> slideItems = new ArrayList<>();
    slideItems.add(new SlideItem(R.drawable.one));
    slideItems.add(new SlideItem(R.drawable.two));
    slideItems.add(new SlideItem(R.drawable.three));
    slideItems.add(new SlideItem(R.drawable.four));
    slideItems.add(new SlideItem(R.drawable.five));

        viewPager2.setAdapter(new SlideAdapter(slideItems, viewPager2));
    viewPager2.setClipToPadding(false);
    viewPager2.setClipChildren(false);
    viewPager2.setOffscreenPageLimit(5);
   // viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);
    viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });
    viewPager2.setPageTransformer(compositePageTransformer);
    viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            slideHandler.removeCallbacks(sliderRunnable);
            slideHandler.postDelayed(sliderRunnable,2000);
        }
    });



    }

    private void initialize() {
        btnClick = findViewById(R.id.btnClick);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome_Panel.this, regActivity.class);
                startActivity(intent);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable, 3000);
    }
}

/*


   <ImageView
       android:layout_width="30dp"
       android:layout_height="90dp"
       android:layout_marginLeft="20dp"
       android:layout_marginTop="30dp"
       android:src="@drawable/hehehe" />

   <ImageView
       android:layout_width="30dp"
       android:layout_height="90dp"
       android:layout_marginTop="-60dp"
       android:layout_marginLeft="180dp"
       android:src="@drawable/hehehe" />

   <ImageView
       android:layout_width="30dp"
       android:layout_height="90dp"
       android:layout_marginTop="-30dp"
       android:layout_marginLeft="350dp"
       android:src="@drawable/hehehe" />









   <ImageView
       android:layout_width="30dp"
       android:layout_height="90dp"
       android:layout_marginLeft="10dp"
       android:layout_marginTop="-70dp"
       android:src="@drawable/hehehe" />

   <ImageView
       android:layout_width="30dp"
       android:layout_height="90dp"
       android:layout_marginLeft="330dp"
       android:layout_marginTop="-190dp"
       android:src="@drawable/hehehe" />


       <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="273dp">



        <ImageView
            android:layout_width="30dp"
            android:layout_height="90dp"
            android:layout_marginLeft="90dp"
            android:layout_marginTop="110dp"
            android:src="@drawable/hahaha" />

        <ImageView
            android:layout_width="30dp"
            android:layout_height="90dp"
            android:layout_marginTop="40dp"
            android:layout_marginLeft="190dp"
            android:src="@drawable/hahaha" />

        <ImageView
            android:layout_width="30dp"
            android:layout_height="90dp"
            android:layout_marginTop="0dp"
            android:layout_marginLeft="350dp"
            android:src="@drawable/hahaha" />

        <ImageView
            android:layout_width="30dp"
            android:layout_height="90dp"
            android:layout_marginTop="30dp"
            android:layout_marginLeft="20dp"
            android:src="@drawable/hahaha" />

        <ImageView
            android:layout_width="30dp"
            android:layout_height="90dp"
            android:layout_marginTop="90dp"
            android:layout_marginLeft="290dp"
            android:src="@drawable/hahaha" />


    </FrameLayout>


 */