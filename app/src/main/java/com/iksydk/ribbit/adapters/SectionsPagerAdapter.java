package com.iksydk.ribbit.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iksydk.ribbit.R;
import com.iksydk.ribbit.ui.FriendsFragment;
import com.iksydk.ribbit.ui.InboxFragment;

import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter
{

    protected Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
        switch(position)
        {
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }

        return null;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        Locale l = Locale.getDefault();
        switch(position)
        {
            case 0:
                return mContext.getString(R.string.title_section1)
                        .toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section2)
                        .toUpperCase(l);
        }
        return null;
    }

    public int getIcon(int position)
    {
        switch(position)
        {
            case 0:
                return R.mipmap.ic_tab_inbox;
            case 1:
                return R.mipmap.ic_tab_friends;
        }
        return R.mipmap.ic_tab_inbox;
    }
}