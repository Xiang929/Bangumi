package me.ewriter.bangumitv.ui.index;

import android.app.Activity;

import java.util.List;

import me.ewriter.bangumitv.base.BasePresenter;
import me.ewriter.bangumitv.base.BaseView;
import me.ewriter.bangumitv.dao.MyCollection;

public interface IndexContract {
        interface Presenter extends BasePresenter {
        /**请求更新数据*/
        void requestData(String type, String category);

        /** 获取当前页的类型 在看,想看 等*/
        String getTime(int position);

        /** 获取请求的类型 如 book， game ，anime */
        String getCategory();

        /**下拉刷新时用于强制从网络更新*/
        void forceRefresh(String category, String type);

        /** 打开详情页面 */
        void openBangumiDetail(Activity activity, android.view.View view, MyCollection collection);
    }

    interface View extends BaseView<Presenter> {
        void refreshList(List<MyCollection> testList);

        void showLoading();

        void hideLoading();

        void showLoginHint();

        void clearData();

        void showOnError();

        void onLogoutEvent();

        void onLoginEvent();

        void onChangeCateEvent();

        void showToast(String msg);
    }
}
