package org.ihci.itbs.presenter;

import org.ihci.itbs.contract.GlobalSettingContract;
import org.ihci.itbs.model.GlobalSettingModel;

import java.lang.ref.WeakReference;

/**
 * @author Yukino Yukinoshita
 */

public class GlobalSettingPresenter implements GlobalSettingContract.Presenter {

    private WeakReference<GlobalSettingContract.View> viewWeakReference;
    private GlobalSettingModel model;

    public GlobalSettingPresenter(GlobalSettingContract.View view) {
        this.viewWeakReference = new WeakReference<>(view);
        this.model = GlobalSettingModel.getInstance();
    }

    @Override
    public GlobalSettingModel.UiTheme getUiTheme() {
        return model.getCurrentTheme();
    }

    @Override
    public void setUiTheme(GlobalSettingModel.UiTheme theme) {
        model.setCurrentTheme(theme);
    }

    @Override
    public boolean getRecommendSetting() {
        return model.isRecommend();
    }

    @Override
    public void setRecommendSetting(boolean recommendSetting) {
        model.setRecommend(recommendSetting);
    }

    @Override
    public void notifyUpdate() {

    }
}
