package me.home.bangumi.ui.login;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;

import me.home.bangumi.BangumiApp;
import me.home.bangumi.R;
import me.home.bangumi.api.ApiManager;
import me.home.bangumi.api.LoginManager;
import me.home.bangumi.api.response.Token;
import me.home.bangumi.event.UserLoginEvent;
import me.home.bangumi.utils.RxBus;
import me.home.bangumi.utils.ToastUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;
    private CompositeSubscription mSubscriptions;

    public LoginPresenter(LoginContract.View mLoginView) {
        this.mLoginView = mLoginView;

        mLoginView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void openLoginHint(Activity activity) {
        String signup = "http://bangumi.tv/signup";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(activity.getResources().getColor(R.color.colorPrimary));
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(signup));
    }

    @Override
    public void login(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            mLoginView.showEmailError();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mLoginView.showPassError();
            return;
        }

        mLoginView.showLoginProDialog();

        Subscription subscription = ApiManager.getBangumiInstance().login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Token>() {
                    @Override
                    public void call(Token token) {
                        if (!TextUtils.isEmpty(token.getAuth())) {
                            LoginManager.saveToken(BangumiApp.sAppCtx, token);
//                            EventBus.getDefault().postSticky(new UserLoginEvent(response.body()));
                            RxBus.getDefault().postSticky(new UserLoginEvent());
                            mLoginView.hideLoginProDialog();
                            mLoginView.closeActivity();
                        } else {
                            ToastUtils.showShortToast(R.string.login_fail);
                            mLoginView.hideLoginProDialog();
                        }
                    }
                });

        mSubscriptions.add(subscription);
    }
}
