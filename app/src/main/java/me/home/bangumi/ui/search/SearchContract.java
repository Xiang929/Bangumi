package me.home.bangumi.ui.search;

import android.app.Activity;
import android.widget.EditText;

import java.util.List;

import me.home.bangumi.api.entity.SearchItemEntity;
import me.home.bangumi.base.BasePresenter;
import me.home.bangumi.base.BaseView;

public interface SearchContract {

    interface Presenter extends BasePresenter {
        /**
         * 搜索条目，默认是执行这个搜索
         * @param type 类型,  默认为 all 全部 ；1为书籍； 2为动画 ；3为音乐；4为游戏；6为三次元
         */
        void searchItem(EditText editText, String type);


        /**
         *  搜索任务
         * @param keyword
         * @param type 默认 all，全部；crt 虚拟角色；prsn 现实人物
         * @param page
         */
        void searchPerson(String keyword, String type, int page);


        /** 打开详情页面 */
        void openBangumiDetail(Activity activity, android.view.View view, SearchItemEntity entity);
    }

    interface View extends BaseView<Presenter> {
        void refreshList(List<SearchItemEntity> list);

        void showLoading();

        void hideLoading();

        /** 清空当前列表的所有数据，并把状态置为初始值，一般是在切换了关键字或条件时调用*/
        void clearData();

        /** 加载失败的时候调用 */
        void showOnError();
    }
}
