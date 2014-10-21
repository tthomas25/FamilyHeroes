package com.example.thomas.familyheroes;

import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * Created by Thomas on 13/10/2014.
 */
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private final List fragments;

    //On fournit à l'adapter la liste des fragments à afficher
    public MyPagerAdapter(FragmentManager fm, List fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
