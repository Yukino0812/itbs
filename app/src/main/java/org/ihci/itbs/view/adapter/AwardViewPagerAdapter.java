package org.ihci.itbs.view.adapter;

import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.ihci.itbs.R;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.view.ItbsApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class AwardViewPagerAdapter extends PagerAdapter {

    private List<Award> awardList;
    private List<View> viewList;

    public AwardViewPagerAdapter(List<Award> awardList) {
        this.awardList = awardList;
        init();
    }

    private void init() {
        viewList = new ArrayList<>();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        boolean isNight = hour > 18 || hour < 6;

        ImageView imageView = new ImageView(ItbsApplication.getContext());
        if (isNight) {
            imageView.setImageDrawable(ItbsApplication.getContext().getDrawable(R.drawable.reward_night));
        } else {
            imageView.setImageDrawable(ItbsApplication.getContext().getDrawable(R.drawable.reward_day_time));
        }
        imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        viewList.add(imageView);

        if (awardList == null) {
            return;
        }
        for (Award award : awardList) {
            if (award == null) {
                continue;
            }
            ImageView imageViewAward = new ImageView(ItbsApplication.getContext());
            imageViewAward.setImageDrawable(getDrawableAwardPicture(award.getAwardName()));
            imageViewAward.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
            viewList.add(imageViewAward);
        }

    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull View arg0, int arg1, @NonNull Object arg2) {
        ((ViewPager) arg0).removeView(viewList.get(arg1));
    }

    @Override
    public void finishUpdate(@NonNull View arg0) {
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View arg0, int arg1) {
        ((ViewPager) arg0).addView(viewList.get(arg1), 0);
        return viewList.get(arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(@NonNull View arg0) {
    }

    private Drawable getDrawableAwardPicture(String awardName) {
        try {
            R.drawable instance = new R.drawable();
            Field field = instance.getClass().getField("award_" + awardName);
            return ItbsApplication.getContext().getResources().getDrawable(field.getInt(instance));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return ItbsApplication.getContext().getDrawable(R.drawable.award_default);
        }
    }

}
