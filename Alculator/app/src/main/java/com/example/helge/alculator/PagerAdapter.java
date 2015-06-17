package com.example.helge.alculator;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.helge.alculator.SettingsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    protected Context mContext;

    public PagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "History";
            case 1:
                return "Main";
            case 2:
                return "Settings";
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HistoryFragment();
            case 1:
                return new MainFragment();
            case 2:
                return new SettingsFragment();
        }
        return null;
    }
}
