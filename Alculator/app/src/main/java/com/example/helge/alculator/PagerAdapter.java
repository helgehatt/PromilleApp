package com.example.helge.alculator;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;

    public PagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.page_history);
            case 1:
                return mContext.getResources().getString(R.string.page_graph);
            case 2:
                return mContext.getResources().getString(R.string.page_main);
            case 3:
                return mContext.getResources().getString(R.string.page_settings);
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Fragment.instantiate(mContext, HistoryFragment.class.getName());
            case 1:
                return Fragment.instantiate(mContext, GraphFragment.class.getName());
            case 2:
                return Fragment.instantiate(mContext, MainFragment.class.getName());
            case 3:
                return Fragment.instantiate(mContext, SettingsFragment.class.getName());
        }
        return null;
    }
}
