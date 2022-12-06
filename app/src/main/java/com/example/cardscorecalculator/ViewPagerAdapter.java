package com.example.cardscorecalculator;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    // creates a view pager adapter for the tabbed layout in the Timer/Stopwatch activity
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    // gets each fragment
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new TimerFragment();
        else if (position == 1)
            fragment = new StopwatchFragment();

        return fragment;
    }

    // gets the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // returns the page title for the tab title
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "Timer";
        else if (position == 1)
            title = "StopWatch";
        return title;
    }
}