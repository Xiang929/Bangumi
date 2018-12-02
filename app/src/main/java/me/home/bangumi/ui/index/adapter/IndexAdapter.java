package me.home.bangumi.ui.index.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.home.bangumi.ui.index.IndexChildFragment;

public class IndexAdapter extends FragmentPagerAdapter {
    List<IndexChildFragment> mList = new ArrayList<>();
    List<String> name = new ArrayList<>();

    public IndexAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(IndexChildFragment fragment,
                            String title) {
        mList.add(fragment);
        name.add(title);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
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
