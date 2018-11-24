package org.ihci.itbs.view;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.ihci.itbs.R;
import org.ihci.itbs.contract.CalendarContract;
import org.ihci.itbs.contract.RecommendContract;
import org.ihci.itbs.contract.UserContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.Goal;
import org.ihci.itbs.model.pojo.HistoryUse;
import org.ihci.itbs.model.pojo.RecommendItem;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.model.repo.RecommendRepo;
import org.ihci.itbs.presenter.CalendarPresenter;
import org.ihci.itbs.presenter.RecommendPresenter;
import org.ihci.itbs.presenter.UserPresenter;
import org.ihci.itbs.util.DateSelector;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Yukino Yukinoshita
 */

public class OverviewView extends AppCompatActivity implements CalendarContract.View, UserContract.View, RecommendContract.View {

    private CalendarContract.Presenter calendarPresenter;
    private UserContract.Presenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        initView();
    }

    @Override
    public void showUpdating() {
        // do nothing
    }

    @Override
    public void showStopUpdate() {
        // do nothing
    }

    @Override
    public void refreshRecommendItem() {
        // do nothing
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

        initMainView();
        initNavigationView();
    }

    private void initMainView() {
        initRecommendContent();
        initUser();
        initMouth();
        initCalendar();
        initGoal();
    }

    private void initNavigationView() {
        initNavUser();
        initNavRecommendSetting();
        initUiThemeSetting();
    }

    private void initRecommendContent() {
        new RecommendPresenter(this).updateLatestRecommendItem();
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
        initUserBackground();
    }

    private void initUserAvatar(User user) {
        ImageView userAvatar = findViewById(R.id.imageViewUserAvatar);
        if (user.getAvatar() != null) {
            userAvatar.setImageBitmap(user.getAvatar());
        } else {
            switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
                case BOY:
                    userAvatar.setImageDrawable(getDrawable(R.drawable.user_avatar_default_boy));
                    break;
                case GIRL:
                    userAvatar.setImageDrawable(getDrawable(R.drawable.user_avatar_default_girl));
                    break;
                default:
                    userAvatar.setImageDrawable(getDrawable(R.drawable.user_avatar_default_boy));
            }
        }

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
            userName = "User Name";
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

    private void initUserBackground() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutTop);

        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorBoyPrimary));
                break;
            case GIRL:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorGirlPrimary));
                break;
            default:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorBoyPrimary));
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
                topMouth.setImageDrawable(getDrawable(R.drawable.logo_boy_top));
                break;
            case GIRL:
                topMouth.setImageDrawable(getDrawable(R.drawable.logo_girl_top));
                break;
            default:
                topMouth.setImageDrawable(getDrawable(R.drawable.logo_boy_top));
        }
    }

    private void initCalendar() {
        TableLayout tableLayout = findViewById(R.id.tableLayoutCalendar);
        tableLayout.removeAllViews();
        initCalendarDayOfWeekText(tableLayout);
        initCalendarContent(tableLayout);
        initCalendarDateHint(tableLayout);
    }

    private void initCalendarDayOfWeekText(TableLayout tableLayout) {
        TableRow tableRow = new TableRow(this);
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
        tableLayout.addView(tableRow);
    }

    private void initCalendarContent(TableLayout tableLayout) {

    }

    private void initCalendarDateHint(TableLayout tableLayout) {
        TextView textViewTime = findViewById(R.id.textViewTimeDescription);
        textViewTime.setText(calendarPresenter.getCalendarDurationDescription());

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutTimeDescription);
        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                textViewTime.setTextColor(Color.BLACK);
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorBoyDeep));
                break;
            case GIRL:
                textViewTime.setTextColor(Color.WHITE);
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorGirlDeep));
                break;
            default:
                textViewTime.setTextColor(Color.BLACK);
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorBoyDeep));
        }
    }

    private void initGoal() {
        initGoalBackground();
        User user;
        if (userPresenter.cacheLogin()) {
            user = userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
        } else {
            user = new User();
        }
        TextView textViewGoal = findViewById(R.id.textViewGoalDescription);
        Goal goal = user.getGoal();
        if (goal == null) {
            textViewGoal.setText("来设置一个目标吧");
            return;
        }
        String goalContent = goal.getContent();
        if (goalContent == null || "".equals(goalContent)) {
            textViewGoal.setText("来设置一个目标吧");
        } else {
            textViewGoal.setText(goalContent);
        }

        // TODO Set goal onclick listener
    }

    private void initGoalBackground() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutGoal);
        ImageView imageViewGoal = findViewById(R.id.imageViewGoal);

        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                imageViewGoal.setImageDrawable(getDrawable(R.drawable.goal_boy));
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorBoyPrimary));
                break;
            case GIRL:
                imageViewGoal.setImageDrawable(getDrawable(R.drawable.goal_girl));
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorGirlPrimary));
                break;
            default:
                imageViewGoal.setImageDrawable(getDrawable(R.drawable.goal_boy));
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorBoyPrimary));
        }
    }

    private void initNavUser() {
        User user;
        if (userPresenter.cacheLogin()) {
            user = userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
        } else {
            user = new User();
        }
        initNavUserAvatar(user);
        initNavUserName(user);
        initNavUserAwardAndCurrency(user);
        initNavUserGetStar(user);
        initNavUserBackground();
    }

    private void initNavUserAvatar(User user) {
        NavigationView navigationView = findViewById(R.id.navigationViewMain);
        View header = navigationView.getHeaderView(0);

        ImageView userAvatar = header.findViewById(R.id.imageViewNavUserAvatar);
        if (user.getAvatar() != null) {
            userAvatar.setImageBitmap(user.getAvatar());
        } else {
            switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
                case BOY:
                    userAvatar.setImageDrawable(getDrawable(R.drawable.user_avatar_default_boy));
                    break;
                case GIRL:
                    userAvatar.setImageDrawable(getDrawable(R.drawable.user_avatar_default_girl));
                    break;
                default:
                    userAvatar.setImageDrawable(getDrawable(R.drawable.user_avatar_default_boy));
            }
        }
    }

    private void initNavUserName(User user) {
        NavigationView navigationView = findViewById(R.id.navigationViewMain);
        View header = navigationView.getHeaderView(0);

        String userName = user.getUserName();
        if (userName == null || "".equals(userName)) {
            userName = "User Name";
        }

        TextView textViewUserName = header.findViewById(R.id.textViewNavUserName);

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

    private void initNavUserAwardAndCurrency(User user) {
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

        NavigationView navigationView = findViewById(R.id.navigationViewMain);
        View header = navigationView.getHeaderView(0);

        TextView textViewAward = header.findViewById(R.id.textViewNavAwardNumber);
        TextView textViewSenior = header.findViewById(R.id.textViewNavSeniorCurrency);
        TextView textViewJunior = header.findViewById(R.id.textViewNavJuniorCurrency);

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

    private void initNavUserGetStar(User user) {
        if (user == null) {
            return;
        }
        NavigationView navigationView = findViewById(R.id.navigationViewMain);
        View header = navigationView.getHeaderView(0);

        TextView textViewNumOfLastDayStar = header.findViewById(R.id.textViewNavLastDayStar);
        TextView textViewNumOfTodayStar = header.findViewById(R.id.textViewNavTodayStar);
        TextView textViewLabelLastDayStar = header.findViewById(R.id.textViewNavLastDayStarLabel);
        TextView textViewLabelTodayStar = header.findViewById(R.id.textViewNavTodayStarLabel);

        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                textViewNumOfLastDayStar.setTextColor(Color.BLACK);
                textViewNumOfTodayStar.setTextColor(Color.BLACK);
                textViewLabelLastDayStar.setTextColor(Color.BLACK);
                textViewLabelTodayStar.setTextColor(Color.BLACK);
                break;
            case GIRL:
                textViewNumOfLastDayStar.setTextColor(Color.WHITE);
                textViewNumOfTodayStar.setTextColor(Color.WHITE);
                textViewLabelLastDayStar.setTextColor(Color.WHITE);
                textViewLabelTodayStar.setTextColor(Color.WHITE);
                break;
            default:
                textViewNumOfLastDayStar.setTextColor(Color.BLACK);
                textViewNumOfTodayStar.setTextColor(Color.BLACK);
                textViewLabelLastDayStar.setTextColor(Color.BLACK);
                textViewLabelTodayStar.setTextColor(Color.BLACK);
        }

        List<HistoryUse> lastDayUse = calendarPresenter.listHistoryUse(new DateSelector().getDaysAfter(-1));
        List<HistoryUse> todayUse = calendarPresenter.listHistoryUse(new Date());

        textViewNumOfLastDayStar.setText(String.valueOf(0));
        textViewNumOfTodayStar.setText(String.valueOf(0));

        if (lastDayUse != null) {
            int star = 0;
            for (HistoryUse historyUse : lastDayUse) {
                Currency gainCurrency = historyUse.getGainCurrency();
                if (gainCurrency == null) {
                    continue;
                }
                star += gainCurrency.getSeniorCurrency() * 3 + gainCurrency.getJuniorCurrency();
            }
            textViewNumOfLastDayStar.setText(String.valueOf(star));
        }

        if (todayUse != null) {
            int star = 0;
            for (HistoryUse historyUse : todayUse) {
                Currency gainCurrency = historyUse.getGainCurrency();
                if (gainCurrency == null) {
                    continue;
                }
                star += gainCurrency.getSeniorCurrency() * 3 + gainCurrency.getJuniorCurrency();
            }
            textViewNumOfTodayStar.setText(String.valueOf(star));
        }

    }

    private void initNavUserBackground() {
        NavigationView navigationView = findViewById(R.id.navigationViewMain);
        View header = navigationView.getHeaderView(0);
        ConstraintLayout constraintLayout = header.findViewById(R.id.constraintLayoutUserHeader);

        switch (GlobalSettingModel.getInstance().getCurrentTheme()) {
            case BOY:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorBoyPrimary));
                break;
            case GIRL:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorGirlPrimary));
                break;
            default:
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorBoyPrimary));
        }
    }

    private void initNavRecommendSetting() {
        NavigationView navigationView = findViewById(R.id.navigationViewMain);
        View header = navigationView.getHeaderView(0);
        final Switch recommendSwitch = header.findViewById(R.id.switchNavRecommendSetting);
        recommendSwitch.setChecked(GlobalSettingModel.getInstance().isRecommend());

        recommendSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalSettingModel.getInstance().setRecommend(!GlobalSettingModel.getInstance().isRecommend());
                recommendSwitch.setChecked(GlobalSettingModel.getInstance().isRecommend());
            }
        });
    }

    private void initUiThemeSetting() {
        NavigationView navigationView = findViewById(R.id.navigationViewMain);
        View header = navigationView.getHeaderView(0);
        Button buttonBoyTheme = header.findViewById(R.id.buttonNavBoyTheme);
        Button buttonGirlTheme = header.findViewById(R.id.buttonNavGirlTheme);

        GradientDrawable boyDrawable = (GradientDrawable) buttonBoyTheme.getBackground();
        boyDrawable.setColor(getResources().getColor(R.color.colorBoyPrimary));
        buttonBoyTheme.setBackground(boyDrawable);

        GradientDrawable girlDrawable = (GradientDrawable) buttonGirlTheme.getBackground();
        girlDrawable.setColor(getResources().getColor(R.color.colorGirlPrimary));
        buttonGirlTheme.setBackground(girlDrawable);

        buttonBoyTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalSettingModel.getInstance().setCurrentTheme(GlobalSettingModel.UiTheme.BOY);
                initMainView();
                initNavigationView();
            }
        });

        buttonGirlTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalSettingModel.getInstance().setCurrentTheme(GlobalSettingModel.UiTheme.GIRL);
                initMainView();
                initNavigationView();
            }
        });
    }

}
