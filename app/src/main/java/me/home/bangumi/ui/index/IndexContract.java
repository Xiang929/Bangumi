package me.home.bangumi.ui.index;

import android.app.Activity;

import java.util.List;

import me.home.bangumi.base.BasePresenter;
import me.home.bangumi.base.BaseView;
import me.home.bangumi.dao.Index;
import me.home.bangumi.dao.MyCollection;

public interface IndexContract {
        interface Presenter extends BasePresenter {
        /**请求更新数据*/
        void requestData(String type, String category);

        /** 时间*/
        String getTime(int position);

        /** 获取请求的类型 如 book， game ，anime */
        String getCategory();

        /**下拉刷新时用于强制从网络更新*/
        void forceRefresh(String category, String date);

        /** 打开详情页面 */
        void openBangumiDetail(Activity activity, android.view.View view, Index index);
    }

    interface View extends BaseView<Presenter> {
        void refreshList(List<Index> testList);

        void showLoading();

        void hideLoading();

        void clearData();

        void showOnError();

        void onChangeCateEvent();

        void showToast(String msg);
    }
}
