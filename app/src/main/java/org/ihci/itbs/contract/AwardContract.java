package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.User;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface AwardContract {

    interface View extends BaseView {

        void showAwardList();

    }

    interface Presenter extends BasePresenter {

        List<Award> listAllAward();

        Award getAward(String awardName);

        void userGetAward(Award award);

        boolean buyAward(User user, String awardName);

    }

}
