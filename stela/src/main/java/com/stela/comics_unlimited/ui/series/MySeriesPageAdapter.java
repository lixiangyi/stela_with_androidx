package com.stela.comics_unlimited.ui.series;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.stela.comics_unlimited.Base.BaseFragment;

import java.util.ArrayList;

public class MySeriesPageAdapter extends FragmentPagerAdapter {
    private String[] titleList;
    private ArrayList<BaseFragment> fragmentList;
    private Context context;

    public MySeriesPageAdapter(FragmentManager fm, Context context, String[] titleList, ArrayList<BaseFragment> fragmentList) {
        super(fm);
        this.titleList = titleList;
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }

    @Override
    public long getItemId(int position) {
        return fragmentList.get(position).hashCode();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}