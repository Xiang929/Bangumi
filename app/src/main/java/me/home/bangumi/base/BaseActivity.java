package me.home.bangumi.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.home.bangumi.api.ApiManager;
import me.home.bangumi.api.BangumiApi;

public abstract class BaseActivity extends AppCompatActivity {

    public static final BangumiApi sBangumi = ApiManager.getBangumiInstance();

    protected abstract int getContentViewResId();

    protected abstract void initView();

    protected abstract void initBefore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBefore();
        setContentView(getContentViewResId());
        initView();
    }
}
