package me.home.bangumi.ui.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.home.bangumi.R;
import me.home.bangumi.base.BaseFragment;
import me.home.bangumi.dao.Index;
import me.home.bangumi.ui.index.adapter.IndexItemAdapter;
import me.home.bangumi.utils.LogUtil;
import me.home.bangumi.utils.ToastUtils;
import me.home.bangumi.utils.Tools;
import me.home.bangumi.widget.VertialSpacingItemDecoration;
import me.home.bangumi.widget.headerfooter.EndlessRecyclerOnScrollListener;
import me.home.bangumi.widget.headerfooter.HeaderAndFooterAdapter;
import me.home.bangumi.widget.headerfooter.LoadingFooter;
import me.home.bangumi.widget.headerfooter.RecyclerViewStateUtils;

public class IndexChildFragment extends BaseFragment implements IndexContract.View {
    private int mPosition;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Index> mList;
    private ProgressBar mProgressbar;
    //emptytext not use
    private TextView mEmptyText;
    private Button mEmptyButton;
    private int MAX_ONEPAGE = 24;

    HeaderAndFooterAdapter mHeaderFooterAdapter;
    IndexItemAdapter mDataAdapter;

    private IndexContract.Presenter mPresenter;
    private boolean isNoMoreData = false;

    public static IndexChildFragment newInstance(int position) {
        IndexChildFragment fragment = new IndexChildFragment();
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
        return R.layout.fragment_index_child;
    }

    @Override
    protected void initView(boolean reused) {
        if (reused)
            return;
        mPresenter = new IndexPresenter(this);
        mPresenter.subscribe();
        mRecyclerView = (RecyclerView) getRootView().findViewById(R.id.index_recycleviews);
        mSwipeRefreshLayout = (SwipeRefreshLayout) getRootView().findViewById(R.id.swipe_refresh_layout);
        mProgressbar = (ProgressBar) getRootView().findViewById(R.id.loadingProgress);
        mEmptyText = (TextView) getRootView().findViewById(R.id.empty_text);
        mEmptyButton = (Button) getRootView().findViewById(R.id.empty_button);
        mList = new ArrayList<>();

        setupRecyclerView();
        setupRefreshLayout();

    }

    private void setupRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String categoty = mPresenter.getCategory();
                String time = mPresenter.getTime(mPosition);
                mPresenter.forceRefresh(categoty, time);
                mPresenter.requestData(time, categoty);
            }
        });

    }

    private void setupRecyclerView() {
        mDataAdapter = new IndexItemAdapter(mList, getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        mHeaderFooterAdapter = new HeaderAndFooterAdapter(mDataAdapter);

        mRecyclerView.addItemDecoration(new VertialSpacingItemDecoration(Tools.getPixFromDip(8)));
        mRecyclerView.setAdapter(mHeaderFooterAdapter);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);

                if (!isNoMoreData) {
                    RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, MAX_ONEPAGE,
                            LoadingFooter.State.Loading, null);
                    // 当前非刷新状态下加载下一页，避免出现重复请求的情况
                    String time = mPresenter.getTime(mPosition);
                    String category = mPresenter.getCategory();
                    mPresenter.requestData(time, category);
                }
            }
        });

        mDataAdapter.setOnItemClickListener(new IndexItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, Index index) {
                mPresenter.openBangumiDetail(getActivity(), view, index);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            LogUtil.d(LogUtil.ZUBIN, "IndexChildFragment onDestroy");
            mPresenter.unsubscribe();
        }
    }

    @Override
    public void refreshList(List<Index> testList) {
        if (testList.size() == 0) {
            // 返回的数据为空，说明数据已经没有更多的数据了
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, MAX_ONEPAGE,
                    LoadingFooter.State.TheEnd, null);
            isNoMoreData = true;
        } else {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, MAX_ONEPAGE,
                    LoadingFooter.State.Normal, null);
        }

        LogUtil.d(LogUtil.ZUBIN, "return list = " + testList.size());
        mList.addAll(testList);
        mDataAdapter.notifyDataSetChanged();
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
    public void clearData() {
        mList.clear();
        isNoMoreData = false;
    }

    @Override
    public void showOnError() {
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, MAX_ONEPAGE,
                LoadingFooter.State.NetWorkError, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, MAX_ONEPAGE,
                                LoadingFooter.State.Loading, null);
                        String time = mPresenter.getTime(mPosition);
                        String category = mPresenter.getCategory();
                        mPresenter.requestData(time, category);
                    }
                });
    }

    @Override
    public void onChangeCateEvent() {
        LogUtil.d(LogUtil.ZUBIN, "onChangeCateEvent + " + mPosition);
        if (mList != null) {
            mList.clear();
            mDataAdapter.notifyDataSetChanged();
        }
        isNoMoreData = false;
        loadData();
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShortToast(msg);
    }

    @Override
    public void setPresenter(IndexContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void loadData() {
        LogUtil.d(LogUtil.ZUBIN, "Index loadData + " + mPosition);

        if (mList != null && mList.size() == 0) {
            showLoading();
            String time = mPresenter.getTime(mPosition);
            String category = mPresenter.getCategory();
            mPresenter.requestData(time, category);
        }
    }
}
