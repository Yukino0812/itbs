package org.ihci.itbs.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ihci.itbs.R;
import org.ihci.itbs.contract.AwardContract;
import org.ihci.itbs.contract.UserContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.presenter.AwardPresenter;
import org.ihci.itbs.presenter.UserPresenter;
import org.ihci.itbs.util.StyleSelector;
import org.ihci.itbs.view.adapter.AwardAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class AwardView extends Activity implements AwardContract.View, UserContract.View {

    private AwardContract.Presenter awardPresenter;
    private UserContract.Presenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        initView();
    }

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

    @Override
    public void showAwardList() {

    }

    public void onClickBack(View view) {
        this.finish();
    }

    private void initView() {
        awardPresenter = new AwardPresenter(this);
        userPresenter = new UserPresenter(this);

        initTopLayoutBackground();
        initUser();
        initAward();
    }

    private void initTopLayoutBackground() {
        findViewById(R.id.constraintLayoutTopBar).setBackgroundColor(StyleSelector.getColorLight());
        findViewById(R.id.constraintLayoutUserInfo).setBackgroundColor(StyleSelector.getColorLight());
    }

    private void initUser() {
        User user = userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
        if (user == null) {
            user = new User();
        }
        initUserAvatar(user);
        initUserName(user);
        initUserAwardAndCurrency(user);
    }

    private void initUserAvatar(User user) {
        ImageView userAvatar = findViewById(R.id.imageViewUserAvatar);
        if (user != null && user.getAvatar() != null) {
            userAvatar.setImageBitmap(user.getAvatar());
        } else {
            userAvatar.setImageDrawable(StyleSelector.getDefaultAvatar());
        }
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
    }

    private void initUserAwardAndCurrency(User user) {
        TextView textViewAward = findViewById(R.id.textViewAwardNumber);
        TextView textViewSenior = findViewById(R.id.textViewSeniorCurrency);
        TextView textViewJunior = findViewById(R.id.textViewJuniorCurrency);

        if (user == null) {
            textViewAward.setText(String.valueOf(0));
            textViewSenior.setText(String.valueOf(0));
            textViewJunior.setText(String.valueOf(0));
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
    }

    private void initAward() {
        initUserAward();

        List<Award> allAward = awardPresenter.listAllAward();
        initBigAward(allAward);
        initMediumAward(allAward);
        initLittleAward(allAward);
    }

    private void initUserAward() {
        User user = userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
        if (user == null || user.getAwardArrayList() == null || user.getAwardArrayList().size() == 0) {
            findViewById(R.id.constraintLayoutUserAward).setVisibility(View.GONE);
            return;
        }
        List<Award> awards = user.getAwardArrayList();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewUserAwardList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        AwardAdapter awardAdapter = new AwardAdapter(awards, false);

        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(awardAdapter);

    }

    private void initBigAward(List<Award> allAward) {
        ArrayList<Award> bigAward = new ArrayList<>();
        if (allAward == null) {
            findViewById(R.id.constraintLayoutBigAward).setVisibility(View.GONE);
        } else {
            for (Award award : allAward) {
                if ("big".equals(award.getAwardType())) {
                    bigAward.add(award);
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewBigAwardList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        AwardAdapter awardAdapter = new AwardAdapter(bigAward);

        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(awardAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initBuyAwardButtonListener();
            }
        }, 20);
    }

    private void initMediumAward(List<Award> allAward) {
        ArrayList<Award> mediumAward = new ArrayList<>();
        if (allAward == null) {
            findViewById(R.id.constraintLayoutMediumAward).setVisibility(View.GONE);
        } else {
            for (Award award : allAward) {
                if ("medium".equals(award.getAwardType())) {
                    mediumAward.add(award);
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMediumAwardList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        AwardAdapter awardAdapter = new AwardAdapter(mediumAward);

        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(awardAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initBuyAwardButtonListener();
            }
        }, 20);
    }

    private void initLittleAward(List<Award> allAward) {
        ArrayList<Award> littleAward = new ArrayList<>();
        if (allAward == null) {
            findViewById(R.id.constraintLayoutLittleAward).setVisibility(View.GONE);
        } else {
            for (Award award : allAward) {
                if ("little".equals(award.getAwardType())) {
                    littleAward.add(award);
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewLittleAwardList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        AwardAdapter awardAdapter = new AwardAdapter(littleAward);

        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(awardAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initBuyAwardButtonListener();
            }
        }, 20);
    }

    private void initBuyAwardButtonListener() {
        RecyclerView[] recyclerViewAry = new RecyclerView[3];
        recyclerViewAry[0] = findViewById(R.id.recyclerViewBigAwardList);
        recyclerViewAry[1] = findViewById(R.id.recyclerViewMediumAwardList);
        recyclerViewAry[2] = findViewById(R.id.recyclerViewLittleAwardList);

        for (RecyclerView recyclerView : recyclerViewAry) {
            for (int i = 0; i < recyclerView.getAdapter().getItemCount(); ++i) {
                ConstraintLayout layout = (ConstraintLayout) recyclerView.getChildAt(i);
                Button buyButton = layout.findViewById(R.id.buttonBuyAward);
                final TextView textViewSenior = layout.findViewById(R.id.textViewSeniorCurrency);
                final TextView textViewJunior = layout.findViewById(R.id.textViewJuniorCurrency);
                final TextView textViewAwardName = layout.findViewById(R.id.textViewAwardName);

                buyButton.setVisibility(View.VISIBLE);
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int totalVal;
                        try {
                            totalVal = Integer.parseInt(textViewSenior.getText().toString()) * 3 + Integer.parseInt(textViewJunior.getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            totalVal = 0;
                        }

                        User user = userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName());

                        if (totalVal == 0 || user == null || user.getCurrency() == null) {
                            Toast.makeText(getApplicationContext(), "暂时无法兑换该奖品", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (awardPresenter.buyAward(user, textViewAwardName.getHint().toString())) {
                            Toast.makeText(getApplicationContext(), "兑换成功", Toast.LENGTH_SHORT).show();
                            onCreate(null);
                        } else {
                            Toast.makeText(getApplicationContext(), "兑换失败，您的积分余额可能不足哦", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }
    }

}
