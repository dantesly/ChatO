package com.m21438255.proyectosnapchat.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.m21438255.proyectosnapchat.R;
import com.m21438255.proyectosnapchat.fragments.FriendsFragment;
import com.m21438255.proyectosnapchat.fragments.InboxFragment;

import java.util.Locale;


/**
 * Created by Adrian on 12/01/2016.
 */


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    protected Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {

        super(fm);
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2).toUpperCase(l);
        }
        return null;
    }

    public int getIcon(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_tab_inbox;
            case 1:
                return R.drawable.ic_tab_friends;
        }
        return -1;
    }
}

