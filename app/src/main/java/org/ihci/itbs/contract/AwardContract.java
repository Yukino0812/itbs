package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.User;

import java.util.List;

/**
 * Contract defined the communication between {@code View} and {@code Presenter}.
 *
 * @author Yukino Yukinoshita
 */

public interface AwardContract {

    interface View extends BaseView {

        /**
         * Prompt {@code View} to refresh award list
         *
         * @param awardList
         */
        void showAwardList(List<Award> awardList);

    }

    interface Presenter extends BasePresenter {

        /**
         * get all awards
         *
         * @return awards
         */
        List<Award> listAllAward();

        /**
         * get one award
         *
         * @param awardName name
         * @return award
         */
        Award getAward(String awardName);

        /**
         * User buy award
         *
         * @param user user
         * @param awardName name of award
         * @return true if buy successfully
         */
        boolean buyAward(User user, String awardName);

    }

}
