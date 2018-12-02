package me.home.bangumi.ui.collection.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.home.bangumi.ui.collection.CollectionChildFragment;

public class CollectionAdapter extends FragmentPagerAdapter {

    List<CollectionChildFragment> mList = new ArrayList<>();
    List<String> name = new ArrayList<>();

    public CollectionAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(CollectionChildFragment fragment, String title) {
        mList.add(fragment);
        name.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return name.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
