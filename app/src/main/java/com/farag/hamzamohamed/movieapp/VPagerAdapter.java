package com.farag.hamzamohamed.movieapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.farag.hamzamohamed.movieapp.fragments.SearchName;
import com.farag.hamzamohamed.movieapp.fragments.ChooseSearch;

import java.util.ArrayList;
import java.util.List;

public class VPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentsList = new ArrayList<>();
    private final List<String> mFragmentTitle = new ArrayList<>();


    public VPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitle.get(position);
    }

    public void addFragment(Fragment fragment ,String title){
        mFragmentsList.add(fragment);
        mFragmentTitle.add(title);
    }
}
