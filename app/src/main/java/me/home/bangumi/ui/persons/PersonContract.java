package me.home.bangumi.ui.persons;

import me.drakeet.multitype.Items;
import me.home.bangumi.base.BasePresenter;
import me.home.bangumi.base.BaseView;


public interface PersonContract {

    interface Presenter extends BasePresenter {
        void requestPerson(String subjectId);
    }

    interface View extends BaseView<Presenter> {
        void setProgressBarVisible(int visible);

        void refresh(Items items);

        void showToast(String msg);
    }
}
