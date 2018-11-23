package org.ihci.itbs.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import org.ihci.itbs.R;
import org.ihci.itbs.contract.CalendarContract;
import org.ihci.itbs.contract.UserContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.RecommendItem;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.model.repo.RecommendRepo;
import org.ihci.itbs.presenter.CalendarPresenter;
import org.ihci.itbs.presenter.UserPresenter;

import java.util.List;
import java.util.Random;

/**
 * @author Yukino Yukinoshita
 */

public class OverviewView extends AppCompatActivity implements CalendarContract.View, UserContract.View {

    private CalendarContract.Presenter calendarPresenter;
    private UserContract.Presenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        initView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_overview);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

    @Override
    public void refreshCalendar() {

    }

    private void initView() {
        calendarPresenter = new CalendarPresenter(this);
        userPresenter = new UserPresenter(this);

        initRecommendContent();
        initUser();
        initMouth();
        initCalendar();
        initGoal();
    }

    private void initRecommendContent() {
        TextView textViewRecommend = findViewById(R.id.textViewRecommend);
        if (!GlobalSettingModel.getInstance().isRecommend()) {
            textViewRecommend.setVisibility(View.GONE);
            return;
        }
        List<RecommendItem> recommendItems = RecommendRepo.getInstance().getRecommendItemArrayList();
        if (recommendItems == null || recommendItems.size() == 0) {
            textViewRecommend.setVisibility(View.GONE);
            return;
        }

        textViewRecommend.setText(recommendItems.get(new Random().nextInt(recommendItems.size())).getTitle());
    }

    private void initUser() {
        User user;
        if (userPresenter.cacheLogin()) {
            user = userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
        } else {
            user = new User();
        }
        initUserAvatar(user);
        initUserName(user);
        initUserAwardAndCurrency(user);
    }

    private void initUserAvatar(User user) {
        if (user.getAvatar() == null) {
            return;
        }
        ImageView userAvatar = findViewById(R.id.imageViewUserAvatar);
        userAvatar.setImageBitmap(user.getAvatar());

        // Set Avatar Onclick Listener
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_overview);
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void initUserName(User user) {
        String userName = user.getUserName();
        if (userName == null || "".equals(userName)) {
            return;
        }

        TextView textViewUserName = findViewById(R.id.textViewUserName);

        // Set Text
        textViewUserName.setText(userName);

        // Set Color
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                textViewUserName.setTextColor(Color.BLACK);
                break;
            case GIRL:
                textViewUserName.setTextColor(Color.WHITE);
                break;
            default:
                textViewUserName.setTextColor(Color.BLACK);
        }
    }

    private void initUserAwardAndCurrency(User user) {
        if (user == null) {
            return;
        }
        List<Award> awards = user.getAwardArrayList();
        int numOfAwards;
        if (awards == null) {
            numOfAwards = 0;
        } else {
            numOfAwards = awards.size();
        }
        Currency currency = user.getCurrency();
        int seniorCurrency, juniorCurrency;
        if (currency == null) {
            seniorCurrency = juniorCurrency = 0;
        } else {
            seniorCurrency = currency.getSeniorCurrency();
            juniorCurrency = currency.getJuniorCurrency();
        }

        TextView textViewAward = findViewById(R.id.textViewAwardNumber);
        TextView textViewSenior = findViewById(R.id.textViewSeniorCurrency);
        TextView textViewJunior = findViewById(R.id.textViewJuniorCurrency);

        // Set Text
        textViewAward.setText(String.valueOf(numOfAwards));
        textViewSenior.setText(String.valueOf(seniorCurrency));
        textViewJunior.setText(String.valueOf(juniorCurrency));

        // Set Color
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                textViewAward.setTextColor(Color.BLACK);
                textViewSenior.setTextColor(Color.BLACK);
                textViewJunior.setTextColor(Color.BLACK);
                break;
            case GIRL:
                textViewAward.setTextColor(Color.WHITE);
                textViewSenior.setTextColor(Color.WHITE);
                textViewJunior.setTextColor(Color.WHITE);
                break;
            default:
                textViewAward.setTextColor(Color.BLACK);
                textViewSenior.setTextColor(Color.BLACK);
                textViewJunior.setTextColor(Color.BLACK);
        }
    }

    private void initMouth() {
        ImageView topMouth = findViewById(R.id.imageViewLogoTop);
        ImageView bottomMouth = findViewById(R.id.imageViewLogoBottom);

        // Set OnClickListener
        View.OnClickListener mouthOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO go to brush teeth view
            }
        };
        topMouth.setOnClickListener(mouthOnClickListener);
        bottomMouth.setOnClickListener(mouthOnClickListener);

        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                topMouth.setBackground(getDrawable(R.drawable.logo_boy_top));
                break;
            case GIRL:
                topMouth.setBackground(getDrawable(R.drawable.logo_girl_top));
                break;
            default:
                topMouth.setBackground(getDrawable(R.drawable.logo_boy_top));
        }
    }

    private void initCalendar() {
        initCalendarDayOfWeekText();
        initCalendarContent();
        initCalendarDateHint();
    }

    private void initCalendarDayOfWeekText() {
        TableRow tableRow = findViewById(R.id.tableRowDayOfWeekRow);
        String[] dayOfWeekString = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (String dayOfWeek : dayOfWeekString) {
            TextView textView = new TextView(this);
            textView.setText(dayOfWeek);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
                case BOY:
                    textView.setTextColor(getResources().getColor(R.color.colorBoyPrimary));
                    break;
                case GIRL:
                    textView.setTextColor(getResources().getColor(R.color.colorGirlPrimary));
                    break;
                default:
                    textView.setTextColor(getResources().getColor(R.color.colorBoyPrimary));
            }
            textView.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(textView);
        }
    }

    private void initCalendarContent() {

    }

    private void initCalendarDateHint() {

    }

    private void initGoal() {

    }

}
