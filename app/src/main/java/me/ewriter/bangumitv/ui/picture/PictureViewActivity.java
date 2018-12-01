package me.ewriter.bangumitv.ui.picture;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.ewriter.bangumitv.R;
import me.ewriter.bangumitv.base.BaseActivity;
import me.ewriter.bangumitv.utils.ToastUtils;


public class PictureViewActivity extends BaseActivity implements View.OnClickListener, PictureContract.View {

    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TEXT = "image_text";

    String mImageUrl, mImageText;
    boolean isHidden = false;

    private Toolbar mToolbar;
    private ImageView mPicture;
    private TextView mDetailText;
    private ViewGroup mPicGroup;

    PictureContract.Presenter mPresenter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_picture_view;
    }

    @Override
    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPicture = (ImageView) findViewById(R.id.picture);
        mDetailText = (TextView) findViewById(R.id.picture_detail);
        mPicGroup = (ViewGroup) findViewById(R.id.picture_group);

        setupToolbar();
        mPicGroup.setOnClickListener(this);

        if (!TextUtils.isEmpty(mImageUrl)) {
            Picasso.with(this).load(mImageUrl).
                    error(R.drawable.img_on_error).
                    into(mPicture);
        } else {
            Picasso.with(this).load(R.drawable.img_on_error).into(mPicture);
        }

        if (!TextUtils.isEmpty(mImageText)) {
            mDetailText.setText(mImageText);
        }

        mPresenter = new PicturePresenter(this);
        mPresenter.subscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                mPresenter.checkPermission(mImageUrl, mImageText+".jpg");
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void setupToolbar() {
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.shadow_color));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initBefore() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Fade());
        }
        mImageUrl = getIntent().getStringExtra(PictureViewActivity.EXTRA_IMAGE_URL);
        mImageText = getIntent().getStringExtra(PictureViewActivity.EXTRA_IMAGE_TEXT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picture_group:
                hideOrShowMask();
                break;

            default:
                break;
        }
    }

    private void hideOrShowMask() {
        mToolbar.animate()
                .translationY(isHidden ? 0 : -mToolbar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();

        mDetailText.animate()
                .translationY(isHidden? 0 : +mDetailText.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();

        isHidden = !isHidden;
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShortToast(msg);
    }

    @Override
    public void setPresenter(PictureContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }
}
