package com.example.helge.alculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    PagerAdapter mPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position) {
                    case 0:
                        ((HistoryFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position)).updateLabels();
                        break;
                    case 1:
                        ((GraphFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position)).updateLabels();
                        break;
                }
            }
        };

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(2);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        if (prefs.getBoolean("isFirstUse", true)){
            startActivity(new Intent(MainActivity.this, TutorialActivity.class));
            prefs.edit().putBoolean("isFirstUse", false).apply();
        }

    }

}
