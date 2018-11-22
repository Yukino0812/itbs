package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.RecommendItem;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface RecommendContract {

    interface View extends BaseView {

        void showUpdating();

        void showStopUpdate();

        void refreshRecommendItem();

    }

    interface Presenter extends BasePresenter {

        List<RecommendItem> listRecommendItem();

        void removeRecommendItem(String link);

        RecommendItem getRecommendItem(String link);

        void updateLatestRecommendItem();

    }

}
