package com.stela.comics_unlimited.ui.browse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.stela.comics_unlimited.Base.BaseFragment;
import com.stela.comics_unlimited.R;

import java.util.ArrayList;

public class MyViewPageAdapter extends FragmentStatePagerAdapter {
    private String[] titleList;
    private ArrayList<BaseFragment> fragmentList;
    private Context context;

    public MyViewPageAdapter(FragmentManager fm, Context context, String[] titleList, ArrayList<BaseFragment> fragmentList) {
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

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titleList[position];
//    }

    //注意！！！这里就是我们自定义的布局tab_item
    public View getCustomView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item_second, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_tab);
        tv.setText(titleList[position]);
        return view;
    }
}