package org.ihci.itbs.view;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
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
import org.ihci.itbs.util.StyleSelector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Yukino Yukinoshita
 */

public class OverviewView extends AppCompatActivity implements CalendarContract.View, UserContract.View, RecommendContract.View {

    private CalendarContract.Presenter calendarPresenter;
    private UserContract.Presenter userPresenter;

    private float lastPressDownX;
    private float lastPressDownY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastPressDownX = ev.getX();
                lastPressDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_overview);
                if (ev.getX() > lastPressDownX + 100) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else if (ev.getX() < lastPressDownX - 100) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    View root = getWindow().getDecorView();
                    View tableLayout = findTableLayout(root, x, y);
                    if (tableLayout instanceof TableLayout) {
                        if (ev.getY() > lastPressDownY + 100) {
                            findViewById(R.id.buttonTimeDescriptionToLeft).performClick();
                        } else if (ev.getY() < lastPressDownY - 100) {
                            findViewById(R.id.buttonTimeDescriptionToRight).performClick();
                        }
                    }
                }

            default:
        }
        return super.dispatchTouchEvent(ev);
    }

    private View findTableLayout(View view, int x, int y) {
        View targetView = null;
        if (view instanceof ViewGroup) {

            ViewGroup v = (ViewGroup) view;
            for (int i = 0; i < v.getChildCount(); i++) {
                targetView = findTableLayout(v.getChildAt(i), x, y);
                if (targetView != null) {
                    break;
                }
            }
        } else {
            targetView = getTouchTarget(view, x, y);
        }
        if (targetView != null && view instanceof TableLayout) {
            return view;
        } else {
            return targetView;
        }
    }

    private View getTouchTarget(View view, int x, int y) {
        View targetView = null;

        ArrayList<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y)) {
                targetView = child;
                break;
            }
        }
        return targetView;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();
        return view.isClickable() && y >= top && y <= bottom && x >= left
                && x <= right;
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

        initNavToAwardView();
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
        if (user != null && user.getAvatar() != null) {
            userAvatar.setImageBitmap(user.getAvatar());
        } else {
            userAvatar.setImageDrawable(StyleSelector.getDefaultAvatar());
        }

        // Set Avatar Onclick Listener
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_overview);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void initUserName(User user) {
        String userName = "";
        if (user != null) {
            userName = user.getUserName();
        }
        if (userName == null || "".equals(userName)) {
            userName = "User Name";
        }

        TextView textViewUserName = findViewById(R.id.textViewUserName);

        // Set Text
        textViewUserName.setText(userName);

        // Set Color
        textViewUserName.setTextColor(StyleSelector.getTextColor());

        textViewUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_overview);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void initUserAwardAndCurrency(User user) {
        TextView textViewAward = findViewById(R.id.textViewAwardNumber);
        TextView textViewSenior = findViewById(R.id.textViewSeniorCurrency);
        TextView textViewJunior = findViewById(R.id.textViewJuniorCurrency);

        if (user == null) {
            textViewAward.setText(String.valueOf(0));
            textViewSenior.setText(String.valueOf(0));
            textViewJunior.setText(String.valueOf(0));
            textViewAward.setTextColor(StyleSelector.getTextColor());
            textViewSenior.setTextColor(StyleSelector.getTextColor());
            textViewJunior.setTextColor(StyleSelector.getTextColor());
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

        // Set Text
        textViewAward.setText(String.valueOf(numOfAwards));
        textViewSenior.setText(String.valueOf(seniorCurrency));
        textViewJunior.setText(String.valueOf(juniorCurrency));

        // Set Color
        textViewAward.setTextColor(StyleSelector.getTextColor());
        textViewSenior.setTextColor(StyleSelector.getTextColor());
        textViewJunior.setTextColor(StyleSelector.getTextColor());
    }

    private void initUserBackground() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutTop);
        constraintLayout.setBackgroundColor(StyleSelector.getColorPrimary());
    }

    private void initMouth() {
        ImageView topMouth = findViewById(R.id.imageViewLogoTop);
        ImageView bottomMouth = findViewById(R.id.imageViewLogoBottom);

        // Set OnClickListener
        View.OnClickListener mouthOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toBrushView();
            }
        };
        topMouth.setOnClickListener(mouthOnClickListener);
        bottomMouth.setOnClickListener(mouthOnClickListener);

        topMouth.setImageDrawable(StyleSelector.getTopMouth());
    }

    private void initCalendar() {
        TableLayout tableLayout = findViewById(R.id.tableLayoutCalendar);
        tableLayout.removeAllViews();
        initCalendarDayOfWeekText(tableLayout);
        initCalendarContent(tableLayout);
        initCalendarDateHint();
    }

    private void initCalendarDayOfWeekText(TableLayout tableLayout) {
        TableRow tableRow = new TableRow(this);
        String[] dayOfWeekString = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (String dayOfWeek : dayOfWeekString) {
            TextView textView = new TextView(this);
            textView.setText(dayOfWeek);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextColor(StyleSelector.getColorPrimary());
            textView.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);
    }

    private void initCalendarContent(TableLayout tableLayout) {
        Date date = calendarPresenter.getCalendarStartDate();
        for (int week = 0; week < 3; ++week) {
            TableRow tableRow = new TableRow(this);
            for (int dayOfWeek = 0; dayOfWeek < 7; ++dayOfWeek) {
                boolean isPass = date.compareTo(DateSelector.getStartTimeThisDay(new Date())) < 0;
                Button button = new Button(this);
                button.setText(String.valueOf(DateSelector.getDayOfMonth(date)));

                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(20f);
                gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);
                gradientDrawable.setStroke(4, getResources().getColor(R.color.backgroundWhite));

                if (isPass) {
                    gradientDrawable.setColor(StyleSelector.getColorLight());
                } else {
                    gradientDrawable.setColor(StyleSelector.getColorPrimary());
                }
                button.setTextColor(StyleSelector.getTextColor());

                button.setBackground(gradientDrawable);
                button.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200, 1f));
                tableRow.addView(button);

                date = DateSelector.getDaysAfter(date, 1);
            }
            tableRow.setPadding(2, 2, 2, 2);
            tableLayout.addView(tableRow);
        }
    }

    private void initCalendarDateHint() {
        TextView textViewTime = findViewById(R.id.textViewTimeDescription);
        textViewTime.setText(calendarPresenter.getCalendarDurationDescription());

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutTimeDescription);

        textViewTime.setTextColor(StyleSelector.getTextColor());
        constraintLayout.setBackgroundColor(StyleSelector.getColorDeep());

        Button buttonLeft = findViewById(R.id.buttonTimeDescriptionToLeft);
        Button buttonRight = findViewById(R.id.buttonTimeDescriptionToRight);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPresenter.addCalendarStartDate(-7);
                initCalendar();
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarPresenter.addCalendarStartDate(7);
                initCalendar();
            }
        });
    }

    private void initGoal() {
        initGoalBackground();
        User user;
        if (userPresenter.cacheLogin()) {
            user = userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
        } else {
            user = new User();
        }

        findViewById(R.id.constraintLayoutGoalInner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGoalView();
            }
        });

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

    }

    private void initGoalBackground() {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutGoal);
        ImageView imageViewGoal = findViewById(R.id.imageViewGoal);

        imageViewGoal.setImageDrawable(StyleSelector.getGoal());
        constraintLayout.setBackgroundColor(StyleSelector.getColorPrimary());
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
        if (user != null && user.getAvatar() != null) {
            userAvatar.setImageBitmap(user.getAvatar());
        } else {
            userAvatar.setImageDrawable(StyleSelector.getDefaultAvatar());
        }

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUserView();
            }
        });
    }

    private void initNavUserName(User user) {
        NavigationView navigationView = findViewById(R.id.navigationViewMain);
        View header = navigationView.getHeaderView(0);

        String userName = "";
        if (user != null) {
            userName = user.getUserName();
        }
        if (userName == null || "".equals(userName)) {
            userName = "User Name";
        }

        TextView textViewUserName = header.findViewById(R.id.textViewNavUserName);

        // Set Text
        textViewUserName.setText(userName);

        // Set Color
        textViewUserName.setTextColor(StyleSelector.getTextColor());

        textViewUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUserView();
            }
        });
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
        textViewAward.setTextColor(StyleSelector.getTextColor());
        textViewSenior.setTextColor(StyleSelector.getTextColor());
        textViewJunior.setTextColor(StyleSelector.getTextColor());
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

        textViewNumOfLastDayStar.setTextColor(StyleSelector.getTextColor());
        textViewNumOfTodayStar.setTextColor(StyleSelector.getTextColor());
        textViewLabelLastDayStar.setTextColor(StyleSelector.getTextColor());
        textViewLabelTodayStar.setTextColor(StyleSelector.getTextColor());

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

        constraintLayout.setBackgroundColor(StyleSelector.getColorPrimary());
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

        ConstraintLayout constraintLayoutRecommendSetting = header.findViewById(R.id.constraintLayoutNavRecommendSetting);
        constraintLayoutRecommendSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendSwitch.performClick();
            }
        });

        ConstraintLayout constraintLayoutRecommendEntry = header.findViewById(R.id.constraintLayoutNavGoToRecommendSetting);
        constraintLayoutRecommendEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO go to recommend
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

    private void initNavToAwardView() {
        NavigationView navigationView = findViewById(R.id.navigationViewMain);
        View header = navigationView.getHeaderView(0);

        TextView textViewToAwardView = header.findViewById(R.id.textViewNavGoToAward);
        textViewToAwardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAwardView();
            }
        });
    }

    private void toUserView() {
        Intent intent = new Intent();
        intent.setClass(org.ihci.itbs.view.OverviewView.this, org.ihci.itbs.view.UserView.class);
        startActivity(intent);
    }

    private void toAwardView() {
        Intent intent = new Intent();
        intent.setClass(org.ihci.itbs.view.OverviewView.this, org.ihci.itbs.view.AwardView.class);
        startActivity(intent);
    }

    private void toBrushView() {
        Intent intent = new Intent();
        intent.setClass(org.ihci.itbs.view.OverviewView.this, org.ihci.itbs.view.BrushView.class);
        startActivity(intent);
    }

    private void toGoalView() {
        Intent intent = new Intent();
        intent.setClass(org.ihci.itbs.view.OverviewView.this, org.ihci.itbs.view.GoalView.class);
        startActivity(intent);
    }

}
