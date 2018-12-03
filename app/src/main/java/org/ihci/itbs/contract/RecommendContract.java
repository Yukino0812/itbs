package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.RecommendItem;

import java.util.List;

/**
 * Contract defined the communication between {@code View} and {@code Presenter}.
 *
 * @author Yukino Yukinoshita
 */

public interface RecommendContract {

    interface View extends BaseView {

        /**
         * Show content updating
         */
        void showUpdating();

        /**
         * Show stop updating
         */
        void showStopUpdate();

        /**
         * Prompt {@code View} to refresh recommend list
         */
        void refreshRecommendItem();

    }

    interface Presenter extends BasePresenter {

        /**
         * list all recommend item
         *
         * @return recommend items
         */
        List<RecommendItem> listRecommendItem();

        /**
         * Remove an item
         *
         * @param link item link as id mark unique item
         */
        void removeRecommendItem(String link);

        /**
         * Get an item
         *
         * @param link item link as id mark unique item
         * @return item
         */
        RecommendItem getRecommendItem(String link);

        /**
         * Prompt {@code Presenter} to update recommend list
         */
        void updateLatestRecommendItem();

    }

}
