package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.Award;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface AwardContract {

    interface View extends BaseView {

        void showAwardList(List<Award> awards);

    }

    interface Presenter extends BasePresenter {

        List<Award> listAllAward();

        Award getAward(String awardName);

        void userGetAward(Award award);

    }

}
