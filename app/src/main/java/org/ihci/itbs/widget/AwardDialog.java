package org.ihci.itbs.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.ihci.itbs.R;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.util.StyleSelector;
import org.ihci.itbs.view.adapter.AwardViewPagerAdapter;

import java.util.List;
import java.util.Objects;

/**
 * @author Yukino Yukinoshita
 */

public class AwardDialog extends Dialog {

    private Context context;
    private View view;

    private int star;
    private List<Award> awardList;

    public AwardDialog(Context context, int star, List<Award> awardList) {
        super(context);
        this.context = context;
        this.star = star;
        this.awardList = awardList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_award, null);
        setContentView(view);

        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        initViewPager();
        initStar();
        initStyle();
    }

    private void initViewPager() {
        AwardViewPagerAdapter adapter = new AwardViewPagerAdapter(awardList);
        final ViewPager viewPager = view.findViewById(R.id.viewPagerAwardList);
        viewPager.setAdapter(adapter);

        final ImageView imageViewLeftAward = view.findViewById(R.id.imageViewLeftAward);
        final ImageView imageViewRightAward = view.findViewById(R.id.imageViewRightAward);

        imageViewLeftAward.setVisibility(View.GONE);
        if (adapter.getCount() < 2) {
            imageViewRightAward.setVisibility(View.GONE);
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PagerAdapter pagerAdapter = viewPager.getAdapter();
                if (pagerAdapter == null) {
                    return;
                }
                int totalViews = pagerAdapter.getCount();

                if (position == 0) {
                    imageViewLeftAward.setVisibility(View.GONE);
                } else {
                    imageViewLeftAward.setVisibility(View.VISIBLE);
                }

                if (position == totalViews - 1) {
                    imageViewRightAward.setVisibility(View.GONE);
                } else {
                    imageViewRightAward.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initStar() {
        TextView textViewStar = view.findViewById(R.id.textViewStarGain);
        textViewStar.setText(String.valueOf(star));
    }

    private void initStyle() {
        ConstraintLayout constraintLayoutAward = view.findViewById(R.id.constraintLayoutAward);
        ConstraintLayout constraintLayoutStar = view.findViewById(R.id.constraintLayoutStar);
        ImageView imageViewLeftAward = view.findViewById(R.id.imageViewLeftAward);
        ImageView imageViewRightAward = view.findViewById(R.id.imageViewRightAward);

        imageViewLeftAward.setImageDrawable(StyleSelector.getLeftArrow());
        imageViewRightAward.setImageDrawable(StyleSelector.getRightArrow());

        GradientDrawable gradientDrawableAward = (GradientDrawable) constraintLayoutAward.getBackground();
        gradientDrawableAward.setColor(StyleSelector.getColorLight());
        constraintLayoutAward.setBackground(gradientDrawableAward);

        GradientDrawable gradientDrawableStar = (GradientDrawable) constraintLayoutStar.getBackground();
        gradientDrawableStar.setColor(StyleSelector.getColorPrimary());
        constraintLayoutStar.setBackground(gradientDrawableStar);
    }

}
