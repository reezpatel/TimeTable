package com.reezx.android.timetable;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.reezx.android.timetable.Database.Period;

import java.util.ArrayList;

/**
 * Created by reezpatel on 22-Jul-17.
 */

public class TimeTablePageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<ArrayList<Period>> periodsArrayLists;
    private Context context;

    public TimeTablePageAdapter(FragmentManager fm, ArrayList<ArrayList<Period>> periods, Context context) {
        super(fm);
        periodsArrayLists = periods;
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Day Order: " + (position+1);
    }



    @Override
    public Fragment getItem(int position) {
        Fragment fragment = DayTimeTableModuleFragment.newInstance(position+1,periodsArrayLists.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return periodsArrayLists.size();
    }
}
