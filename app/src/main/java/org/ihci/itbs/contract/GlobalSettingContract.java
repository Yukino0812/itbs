package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.GlobalSettingModel;

/**
 * @author Yukino Yukinoshita
 */

public interface GlobalSettingContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {

        GlobalSettingModel.UiTheme getUiTheme();

        void setUiTheme(GlobalSettingModel.UiTheme theme);

        boolean getRecommendSetting();

        void setRecommendSetting(boolean recommendSetting);

    }

}
