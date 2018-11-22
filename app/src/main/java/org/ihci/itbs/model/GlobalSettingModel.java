package org.ihci.itbs.model;

import java.io.Serializable;

/**
 * @author Yukino Yukinoshita
 */

public class GlobalSettingModel implements Serializable {

    private static GlobalSettingModel INSTANCE = null;

    private String currentUserName;
    private UiTheme currentTheme;
    private boolean recommendSetting;

    private GlobalSettingModel() {

    }

    public static GlobalSettingModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GlobalSettingModel();
        }
        return INSTANCE;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public UiTheme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(UiTheme currentTheme) {
        this.currentTheme = currentTheme;
    }

    public boolean isRecommendSetting() {
        return recommendSetting;
    }

    public void setRecommendSetting(boolean recommendSetting) {
        this.recommendSetting = recommendSetting;
    }

    public enum UiTheme{

        /**
         * UI theme boy style
         */
        BOY,

        /**
         * UI theme girl style
         */
        GIRL,

    }

}
