package me.home.bangumi.ui.index;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.home.bangumi.R;
import me.home.bangumi.base.BaseFragment;
import me.home.bangumi.event.CategoryChangeEvent;
import me.home.bangumi.event.OpenNavigationEvent;
import me.home.bangumi.ui.index.adapter.IndexAdapter;
import me.home.bangumi.utils.RxBus;

public class IndexFragment extends BaseFragment {
    private ViewPager mViewPager;
    private RecyclerTabLayout mTabLayout;
    private Toolbar mToolbar;
    private PopupMenu mPopupMenu;
    View showView;

    public static IndexFragment newInstance() {
        return new IndexFragment();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void initView(boolean reused) {
        if (reused)
            return;

        mViewPager = (ViewPager) getRootView().findViewById(R.id.viewpager);
        mTabLayout = (RecyclerTabLayout) getRootView().findViewById(R.id.item_tabs);
        mToolbar = (Toolbar) getRootView().findViewById(R.id.toolbar);

        setupToolbar();
        setupViewPager();
    }

    @Override
    protected void loadData() {
    }

    private void setupToolbar() {
        mToolbar.setTitle(getString(R.string.nav_index));
        mToolbar.inflateMenu(R.menu.filter_menu);
        mToolbar.setNavigationIcon(R.drawable.ic_action_drawer);

        showView = mToolbar.findViewById(R.id.toolbar_filter);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new OpenNavigationEvent());
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.toolbar_filter) {
//                    startActivity(new Intent(getActivity(), SearchActivity.class));
                    showMenu();
                }
                return false;
            }
        });
    }

    private void showMenu() {
        mPopupMenu = new PopupMenu(getActivity(), showView);
        mPopupMenu.inflate(R.menu.menu_popupmenu);
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.item_anime:
                        RxBus.getDefault().postSticky(new CategoryChangeEvent("anime"));
                        return true;

                    case R.id.item_book:
                        RxBus.getDefault().postSticky(new CategoryChangeEvent("book"));
                        return true;

                    case R.id.item_game:
                        RxBus.getDefault().postSticky(new CategoryChangeEvent("game"));
                        return true;

                    case R.id.item_music:
                        RxBus.getDefault().postSticky(new CategoryChangeEvent("music"));
                        return true;

                    case R.id.item_real:
                        RxBus.getDefault().postSticky(new CategoryChangeEvent("real"));
                        return true;
                }

                return false;
            }
        });

        mPopupMenu.show();
    }

    private void setupViewPager() {
        final int startYear = 1900;
        int endYear = 3000;
        final int initialYear = Calendar.getInstance().get(Calendar.YEAR);
        final IndexAdapter adapter = new IndexAdapter(getChildFragmentManager());
        List<String> items = new ArrayList<>();
        for (int i = startYear; i <= endYear; i++) {
            for (int j = 1; j <= 12; j++) {
                items.add(String.valueOf(i) + "年\n" + String.valueOf(j) + "月");
            }
        }
        for (int i = 0; i < items.size(); i++) {
            adapter.addFragment(IndexChildFragment.newInstance(i), items.get(i));
        }
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(adapter);
                mViewPager.setCurrentItem((initialYear-startYear) * 12 +
                                            Calendar.getInstance().get(Calendar.MONTH));
                mTabLayout.setUpWithViewPager(mViewPager);
            }
        });
    }
}
