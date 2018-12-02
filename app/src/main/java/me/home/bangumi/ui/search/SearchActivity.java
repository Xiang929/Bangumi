package me.home.bangumi.ui.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import me.home.bangumi.R;
import me.home.bangumi.api.entity.SearchItemEntity;
import me.home.bangumi.base.BaseActivity;
import me.home.bangumi.ui.search.adapter.SearchAdapter;
import me.home.bangumi.utils.LogUtil;
import me.home.bangumi.utils.Tools;
import me.home.bangumi.widget.headerfooter.EndlessRecyclerOnScrollListener;
import me.home.bangumi.widget.headerfooter.HeaderAndFooterAdapter;
import me.home.bangumi.widget.headerfooter.LoadingFooter;
import me.home.bangumi.widget.headerfooter.RecyclerViewStateUtils;
import me.home.bangumi.widget.VertialSpacingItemDecoration;

public class SearchActivity extends BaseActivity implements SearchContract.View {

    private Toolbar mToolbar;
    private EditText mSearchEdit;
    private ImageView mBackImg, mStatusImg;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressbar;
    private SearchContract.Presenter mPresenter;
    private SearchAdapter mDataAdapter;
    private HeaderAndFooterAdapter mWrapperAdapter;
    private List<SearchItemEntity> mList;
    private boolean isNoData = false;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {

        mPresenter = new SearchPresenter(this);
        mPresenter.subscribe();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearchEdit = (EditText) findViewById(R.id.search_editText);
        mBackImg = (ImageView) findViewById(R.id.search_arrow_back);
        mStatusImg = (ImageView) findViewById(R.id.status_image);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
        mList = new ArrayList<>();

        setupToolbar();
        setupRecyclerView();

        setupSearchEdit();
    }

    private void setupRecyclerView() {
        mDataAdapter = new SearchAdapter(mList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new VertialSpacingItemDecoration(Tools.getPixFromDip(2)));
        mRecyclerView.setHasFixedSize(true);
        mWrapperAdapter = new HeaderAndFooterAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mWrapperAdapter);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                if (!isNoData) {
                    LogUtil.d(LogUtil.ZUBIN, "search onLoadMore");
                    mPresenter.searchItem(mSearchEdit, "all");
                    // 这个一页里面1 是指一页返回的数据，实际上一页返回的数据不确定，所以这边随便了一个数，保证能加载下一页
                    RecyclerViewStateUtils.setFooterViewState(SearchActivity.this, mRecyclerView, 8,
                            LoadingFooter.State.Loading, null);
                }
            }
        });

        mDataAdapter.setOnItemClickListener(new SearchAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, SearchItemEntity entity) {
                mPresenter.openBangumiDetail(SearchActivity.this, view, entity);
            }
        });
    }

    private void setupSearchEdit() {
        LogUtil.d(LogUtil.ZUBIN, "search setupSearchEdit");
        mPresenter.searchItem(mSearchEdit, "all");
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
    }

    @Override
    protected void initBefore() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void refreshList(List<SearchItemEntity> list) {
        // 返回的数据大于0，说明这也有数据，页数才加1,
        if (list.size() > 0) {
            RecyclerViewStateUtils.setFooterViewState(SearchActivity.this, mRecyclerView, 8,
                    LoadingFooter.State.Normal, null);
        } else {
            isNoData = true;
            RecyclerViewStateUtils.setFooterViewState(SearchActivity.this, mRecyclerView, 8,
                    LoadingFooter.State.TheEnd, null);
        }

        mList.addAll(list);
        mDataAdapter.notifyDataSetChanged();

    }

    @Override
    public void showLoading() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void clearData() {
        mList.clear();
        mDataAdapter.notifyDataSetChanged();
        isNoData = false;
        RecyclerViewStateUtils.setFooterViewState(SearchActivity.this, mRecyclerView, 8,
                LoadingFooter.State.Normal, null);
    }

    @Override
    public void showOnError() {
        RecyclerViewStateUtils.setFooterViewState(this, mRecyclerView, 8,
                LoadingFooter.State.NetWorkError, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecyclerViewStateUtils.setFooterViewState(SearchActivity.this, mRecyclerView, 8,
                                LoadingFooter.State.Loading, null);
                        mPresenter.searchItem(mSearchEdit, "all");
                    }
                });
    }
}
