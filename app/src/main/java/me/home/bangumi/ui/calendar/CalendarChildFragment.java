package me.home.bangumi.ui.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import me.home.bangumi.R;
import me.home.bangumi.base.BaseFragment;
import me.home.bangumi.dao.BangumiCalendar;
import me.home.bangumi.ui.calendar.adapter.CalendarItemAdapter;
import me.home.bangumi.utils.LogUtil;

public class CalendarChildFragment extends BaseFragment implements CalendarContract.View{

    private int mPosition;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressbar;
    private CalendarItemAdapter mAdapter;
    private CalendarContract.Presenter mPresenter;

    List<BangumiCalendar> mCalendarList;

    public static CalendarChildFragment newInstance(int position) {
        CalendarChildFragment fragment = new CalendarChildFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt("position");
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_calender_child;
    }

    @Override
    protected void initView(boolean reused) {
        if (reused)
            return;

        mPresenter = new CalendarPresenter(this);
        mPresenter.subscribe();
        mRecyclerView = (RecyclerView) getRootView().findViewById(R.id.calendar_recycleviews);
        mSwipeRefreshLayout = (SwipeRefreshLayout) getRootView().findViewById(R.id.swipe_refresh_layout);
        mProgressbar = (ProgressBar) getRootView().findViewById(R.id.loadingProgress);
        mCalendarList = new ArrayList<>();

        setupRecyclerView();
        setupRefreshLayout();

    }

    private void setupRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.forceRefresh();
                mPresenter.loadData(mPosition);
            }
        });
    }

    private void setupRecyclerView() {
        mAdapter = new CalendarItemAdapter(mCalendarList, getActivity());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnCalendarItemClickListener(new CalendarItemAdapter.onCalendarItemListener() {
            @Override
            public void onCalendarClick(View view, BangumiCalendar calendar) {
                mPresenter.openBangumiDetail(getActivity(), view, calendar);
            }
        });
    }

    @Override
    protected void loadData() {
        if (mCalendarList.size() == 0) {
            LogUtil.d(LogUtil.ZUBIN, "loadData "+ mPosition);
            showLoading();
            mPresenter.loadData(mPosition);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(LogUtil.ZUBIN, "CalendarChildFragment onDestroyView " + mPosition);
        hideLoading();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(CalendarContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void refresh(List<BangumiCalendar> calendarList) {
        mCalendarList.clear();
        mCalendarList.addAll(calendarList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        if (mRecyclerView != null) {
            Snackbar snackbar = Snackbar.make(mRecyclerView, getString(R.string.update_failed), Snackbar.LENGTH_SHORT);
            snackbar.show();
            snackbar.setAction(getString(R.string.update_retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading();
                    mPresenter.loadData(mPosition);
                }
            });
        }

    }
}
