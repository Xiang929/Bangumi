package me.home.bangumi.ui.progress;

import java.util.List;

import me.drakeet.multitype.Items;
import me.home.bangumi.api.entity.AnimeEpEntity;
import me.home.bangumi.base.BasePresenter;
import me.home.bangumi.base.BaseView;


public interface ProgressContract {

    interface Presenter extends BasePresenter {
        void requestProgress(String subjectId);

        void updateEpStatus(AnimeEpEntity entity, int position, int gridPosition);

        void openDiscuss(AnimeEpEntity entity);
    }

    interface View extends BaseView<Presenter> {
        void setProgressBarVisible(int visible);

        void refresh(List<AnimeEpEntity> items);

        void showToast(String msg);

        void dismissProgressDialog();

        void showProgressDialog();

        void updateEp(int position);

    }

}
