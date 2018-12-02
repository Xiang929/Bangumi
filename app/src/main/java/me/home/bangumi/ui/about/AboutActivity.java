package me.home.bangumi.ui.about;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import me.home.bangumi.R;
import me.home.bangumi.base.BaseActivity;

/**
 * 这里没有什么逻辑处理，懒得改成mvp 了
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mVersionText;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mWeibo;
    private TextView mGithub;
    private TextView mDonate;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mVersionText = (TextView) findViewById(R.id.version);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        setupHeader();
    }

    @Override
    protected void initBefore() {

    }

    private void setupHeader() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
        getSupportActionBar().setTitle(R.string.about);
        mCollapsingToolbarLayout.setTitleEnabled(false);
        mCollapsingToolbarLayout.setExpandedTitleGravity(GravityCompat.START);
    }

    @Override
    public void onClick(View v) {
        return;
    }
}
