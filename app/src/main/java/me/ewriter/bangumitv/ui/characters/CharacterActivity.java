package me.ewriter.bangumitv.ui.characters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.ewriter.bangumitv.R;
import me.ewriter.bangumitv.base.BaseActivity;
import me.ewriter.bangumitv.widget.commonAdapter.TitleItem;
import me.ewriter.bangumitv.utils.ToastUtils;
import me.ewriter.bangumitv.utils.Tools;
import me.ewriter.bangumitv.widget.HorizonSpacingItemDecoration;

/**
 * 角色介绍
 */

public class CharacterActivity extends BaseActivity implements CharacterContract.View{

    private Toolbar mToolbar;
    private ProgressBar mProgressbar;
    private RecyclerView mRecyclerView;

    private String subjectId;
    private Items list;

    CharacterContract.Presenter mPresenter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_character;
    }

    @Override
    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mPresenter = new CharacterPresenter(this);
        mPresenter.subscribe();

        setUpToolbar();
        setUpRecyclerView();

        mPresenter.requestCharacter(subjectId);
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle(getString(R.string.bangumi_detail_character));
    }

    private void setUpRecyclerView() {
        list = new Items();
        list.add(new TitleItem(getString(R.string.bangumi_detail_character), R.drawable.ic_whatshot_24dp));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new HorizonSpacingItemDecoration(Tools.getPixFromDip(16)));
    }

    @Override
    protected void initBefore() {
        subjectId = getIntent().getStringExtra("subjectId");
    }

    @Override
    public void refresh(Items items) {
        list.addAll(items);
        MultiTypeAdapter adapter = new MultiTypeAdapter(list);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShortToast(msg);
    }

    @Override
    public void setProgressBarVisible(int visible) {
        if (mProgressbar != null) {
            mProgressbar.setVisibility(visible);
        }
    }

    @Override
    public void setPresenter(CharacterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }
}
