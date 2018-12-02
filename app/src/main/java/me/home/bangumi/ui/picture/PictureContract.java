package me.home.bangumi.ui.picture;

import me.home.bangumi.base.BasePresenter;
import me.home.bangumi.base.BaseView;


public interface PictureContract {

    interface Presenter extends BasePresenter {
        void checkPermission(String imageUrl, String name);

        void downLoadImage(String imageUrl, String name);
    }

    interface View extends BaseView<Presenter> {
        void showToast(String msg);
    }
}
