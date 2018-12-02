package com.vikasreddy.restfiddler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return HeadersFragment.newInstance();
        } else if(position == 1) {
            return QueryParamsFragment.newInstance();
        } else if(position == 2) {
            return BodyFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
