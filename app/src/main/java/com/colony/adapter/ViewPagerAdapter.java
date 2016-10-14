package com.colony.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zahi on 12/10/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles =  new ArrayList<>();

    public void addFaragmets(Fragment fragments , String TabTitles)
    {
        this.fragments.add(fragments);
        this.tabTitles.add(TabTitles);



    }
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles.get(position);
    }
}
