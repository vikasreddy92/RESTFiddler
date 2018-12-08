package com.vikasreddy.restfiddler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ResponsePageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    private ParcelableResponse response;
    private FragmentManager fragmentManager;

    public ResponsePageAdapter(FragmentManager fm, int numOfTabs, ParcelableResponse response) {
        super(fm);
        this.fragmentManager = fm;
        this.numOfTabs = numOfTabs;
        this.response = response;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return ResponseBodyFragment.newInstance(fragmentManager, response);
        } else if(position == 1) {
            return ResponseCookiesFragment.newInstance(fragmentManager, response);
        } else if(position == 2) {
            return ResponseHeadersFragment.newInstance(fragmentManager, response);
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
