package com.zup.movies.services.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zup.movies.components.Favorites.FavoritesFragment;
import com.zup.movies.components.Home.HomeFragment;

/**
 * Created by ghenrique on 30/05/16.
 */
public class ViewPagerCustomAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private static String[] titles = {"HOME","FAVORITOS"};

    public ViewPagerCustomAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return FavoritesFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}

