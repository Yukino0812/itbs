package org.ihci.itbs.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import org.ihci.itbs.R;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.view.ItbsApplication;

/**
 * @author Yukino Yukinoshita
 */

public class StyleSelector {

    public static int getTextColor() {
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                return Color.BLACK;
            case GIRL:
                return Color.WHITE;
            default:
                return Color.BLACK;
        }
    }

    public static int getColorPrimary() {
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                return ItbsApplication.getContext().getResources().getColor(R.color.colorBoyPrimary);
            case GIRL:
                return ItbsApplication.getContext().getResources().getColor(R.color.colorGirlPrimary);
            default:
                return ItbsApplication.getContext().getResources().getColor(R.color.colorBoyPrimary);
        }
    }

    public static int getColorLight() {
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                return ItbsApplication.getContext().getResources().getColor(R.color.colorBoyLight);
            case GIRL:
                return ItbsApplication.getContext().getResources().getColor(R.color.colorGirlLight);
            default:
                return ItbsApplication.getContext().getResources().getColor(R.color.colorBoyLight);
        }
    }

    public static int getColorDeep() {
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                return ItbsApplication.getContext().getResources().getColor(R.color.colorBoyDeep);
            case GIRL:
                return ItbsApplication.getContext().getResources().getColor(R.color.colorGirlDeep);
            default:
                return ItbsApplication.getContext().getResources().getColor(R.color.colorBoyDeep);
        }
    }

    public static Drawable getDefaultAvatar() {
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                return ItbsApplication.getContext().getDrawable(R.drawable.user_avatar_default_boy);
            case GIRL:
                return ItbsApplication.getContext().getDrawable(R.drawable.user_avatar_default_girl);
            default:
                return ItbsApplication.getContext().getDrawable(R.drawable.user_avatar_default_boy);
        }
    }

    public static Drawable getTopMouth() {
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                return ItbsApplication.getContext().getDrawable(R.drawable.logo_boy_top);
            case GIRL:
                return ItbsApplication.getContext().getDrawable(R.drawable.logo_girl_top);
            default:
                return ItbsApplication.getContext().getDrawable(R.drawable.logo_boy_top);
        }
    }

    public static Drawable getGoal() {
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                return ItbsApplication.getContext().getDrawable(R.drawable.goal_boy);
            case GIRL:
                return ItbsApplication.getContext().getDrawable(R.drawable.goal_girl);
            default:
                return ItbsApplication.getContext().getDrawable(R.drawable.goal_boy);
        }
    }

    public static Drawable getLeftArrow() {
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                return ItbsApplication.getContext().getDrawable(R.drawable.arrow_left_boy);
            case GIRL:
                return ItbsApplication.getContext().getDrawable(R.drawable.arrow_left_girl);
            default:
                return ItbsApplication.getContext().getDrawable(R.drawable.arrow_left_boy);
        }
    }

    public static Drawable getRightArrow() {
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                return ItbsApplication.getContext().getDrawable(R.drawable.arrow_right_boy);
            case GIRL:
                return ItbsApplication.getContext().getDrawable(R.drawable.arrow_right_girl);
            default:
                return ItbsApplication.getContext().getDrawable(R.drawable.arrow_right_boy);
        }
    }

}
