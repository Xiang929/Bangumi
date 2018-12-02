package me.home.bangumi.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDelegate;

import java.util.Collection;

import me.home.bangumi.BangumiApp;
import me.home.bangumi.R;
import me.home.bangumi.api.LoginManager;
import me.home.bangumi.constants.MyConstants;
import me.home.bangumi.dao.Index;
import me.home.bangumi.event.OpenNavigationEvent;
import me.home.bangumi.ui.about.AboutActivity;
import me.home.bangumi.ui.calendar.CalendarFragment;
import me.home.bangumi.ui.collection.CollectionFragment;
import me.home.bangumi.ui.index.IndexFragment;
import me.home.bangumi.ui.login.LoginActivity;
import me.home.bangumi.ui.search.SearchActivity;
import me.home.bangumi.utils.LogUtil;
import me.home.bangumi.utils.PreferencesUtils;
import me.home.bangumi.utils.RxBus;
import me.home.bangumi.utils.ToastUtils;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mHomeView;
    private CompositeSubscription mSubscriptions;
    private Subscription mRxOpenSub;

    public HomePresenter(HomeContract.View mHomeView) {
        this.mHomeView = mHomeView;
        mHomeView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mSubscriptions = new CompositeSubscription();

        subscribeOpenDrawerEvent();
    }

    private void subscribeOpenDrawerEvent() {
        if (mRxOpenSub != null) {
            mSubscriptions.remove(mRxOpenSub);
        }

        mRxOpenSub = RxBus.getDefault().toObservable(OpenNavigationEvent.class)
                .subscribe(new Observer<OpenNavigationEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // 如果异常，这里后面就收不到event了，必须重新订阅
                        LogUtil.d(LogUtil.ZUBIN, e.getMessage());
                        subscribeOpenDrawerEvent();
                    }

                    @Override
                    public void onNext(OpenNavigationEvent openNavigationEvent) {
                        LogUtil.d(LogUtil.ZUBIN, "OpenNavigationEvent onNext");
                        mHomeView.openDrawer();
                    }
                });

        mSubscriptions.add(mRxOpenSub);
    }

    @Override
    public void unsubscribe() {
        if (mRxOpenSub != null) {
            mSubscriptions.remove(mRxOpenSub);
        }
        RxBus.getDefault().removeAllStickyEvents();
    }

    @Override
    public void updateCurrentTag(String tag) {
        PreferencesUtils.putString(BangumiApp.sAppCtx, "current", tag);
    }

    @Override
    public String getCurrentTag() {
        return PreferencesUtils.getString(BangumiApp.sAppCtx, "current", CalendarFragment.class.getName());
    }

    @Override
    public void updateSelectedFragment(FragmentManager manager, String tag) {

        Fragment f = manager.findFragmentByTag(tag);
        if (f == null) {
            if (tag.equals(CalendarFragment.class.getName())) {
                manager.beginTransaction()
                        .replace(R.id.frame_container, CalendarFragment.newInstance(), tag)
                        .commit();
            } else if(tag.equals(CollectionFragment.class.getName())){
                manager.beginTransaction()
                        .replace(R.id.frame_container, CollectionFragment.newInstance(), tag)
                        .commit();
                mHomeView.updateDrawChecked(R.id.nav_collections);
            } else {
                manager.beginTransaction()
                        .replace(R.id.frame_container, IndexFragment.newInstance(), tag)
                        .commit();
                mHomeView.updateDrawChecked(R.id.nav_index);
            }
        }
    }

    @Override
    public void openSearch(Activity context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    public void openIndex(Activity context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    public void openExpand(Activity context) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(BangumiApp.sAppCtx.getResources().getColor(R.color.colorPrimary));
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse("http://bangumi.tv/m"));
    }

    @Override
    public void openAbout(Activity activity) {
        activity.startActivity(new Intent(activity, AboutActivity.class));
    }

    @Override
    public void logout() {
        if (LoginManager.isLogin(BangumiApp.sAppCtx)) {
            mHomeView.showLogoutDialog();
        } else {
            ToastUtils.showShortToast(R.string.not_login_hint);
        }
    }

    @Override
    public void login(Activity activity) {
        if (LoginManager.isLogin(BangumiApp.sAppCtx)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(BangumiApp.sAppCtx.getResources().getColor(R.color.colorPrimary));
            builder.setShowTitle(true);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(activity, Uri.parse(LoginManager.getUserHomeUrl(BangumiApp.sAppCtx)));
        } else {
            activity.startActivity(new Intent(activity, LoginActivity.class));
        }
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public void updateTheme(boolean isChecked, Activity activity) {
        PreferencesUtils.putBoolean(BangumiApp.sAppCtx, MyConstants.THEME_KEY, isChecked);
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        activity.recreate();
    }

    @Override
    public void checkLogin() {
        if (LoginManager.isLogin(BangumiApp.sAppCtx)) {
            mHomeView.setUserInfo();
        }
    }

}
