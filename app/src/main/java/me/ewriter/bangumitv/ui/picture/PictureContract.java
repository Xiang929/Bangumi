package me.ewriter.bangumitv.ui.picture;

import me.ewriter.bangumitv.base.BasePresenter;
import me.ewriter.bangumitv.base.BaseView;


public interface PictureContract {

    interface Presenter extends BasePresenter {
        void checkPermission(String imageUrl, String name);

        void downLoadImage(String imageUrl, String name);
    }

    interface View extends BaseView<Presenter> {
        void showToast(String msg);
    }
}
