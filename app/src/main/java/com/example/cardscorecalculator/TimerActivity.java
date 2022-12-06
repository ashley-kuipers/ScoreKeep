package com.example.cardscorecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class TimerActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // connect vars with views
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        topAppBar = findViewById(R.id.topAppBarTimerActivity);

        // sets the app bar back button function
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(TimerActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

        // setting up tabLayout
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        // joins TabLayout with ViewPager.
        tabLayout.setupWithViewPager(viewPager);
    }
}