package org.ihci.itbs.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ihci.itbs.R;
import org.ihci.itbs.contract.GoalContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Goal;
import org.ihci.itbs.presenter.GoalPresenter;
import org.ihci.itbs.view.adapter.GoalAdapter;
import org.ihci.itbs.widget.AwardDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Show all of the goals user can set.
 *
 * @author Yukino Yukinoshita
 */

public class GoalView extends Activity implements GoalContract.View {

    private GoalContract.Presenter goalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        initView();
    }

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

    @Override
    public void showGoalList() {

    }

    @Override
    public void notifyGoalFinish(final int star, final Award award) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewGoalList);
        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); ++i) {
            ConstraintLayout layout = (ConstraintLayout) recyclerView.getChildAt(i);
            if (layout == null) {
                return;
            }
            Button buttonSetGoal = layout.findViewById(R.id.buttonSetGoal);
            TextView textViewGoalDescription = layout.findViewById(R.id.textViewGoalDescription);
            int goalId;
            try {
                goalId = Integer.parseInt(textViewGoalDescription.getHint().toString());
            } catch (NumberFormatException e) {
                goalId = 0;
            }
            if (goalId == 0) {
                buttonSetGoal.setVisibility(View.GONE);
                continue;
            }
            Goal userCurrentGoal = goalPresenter.getUserGoal();
            if (userCurrentGoal != null && userCurrentGoal.getGoalId() == goalId) {
                buttonSetGoal.setBackground(getDrawable(R.drawable.radius_rect_button_selector));
                buttonSetGoal.setText("领取奖励");
                buttonSetGoal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goalPresenter.userFinishGoal(star, award);

                        ArrayList<Award> awardList = new ArrayList<>(1);
                        awardList.add(award);
                        AwardDialog awardDialog = new AwardDialog(GoalView.this, star, awardList);
                        awardDialog.create();
                        awardDialog.show();

                        initRecyclerView();
                    }
                });
            } else {
                buttonSetGoal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "请先领取已完成目标的奖励", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void onClickBack(View view) {
        this.finish();
    }

    public void initView() {
        goalPresenter = new GoalPresenter(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        List<Goal> goalList = goalPresenter.listAllGoal();
        if (goalList == null) {
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewGoalList);
        GoalAdapter goalAdapter = new GoalAdapter(goalList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(goalAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRecyclerViewItem();
                goalPresenter.checkIsGoalFinish();
            }
        }, 20);
    }

    private void initRecyclerViewItem() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewGoalList);
        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); ++i) {
            ConstraintLayout layout = (ConstraintLayout) recyclerView.getChildAt(i);
            if (layout == null) {
                return;
            }
            Button buttonSetGoal = layout.findViewById(R.id.buttonSetGoal);
            TextView textViewGoalDescription = layout.findViewById(R.id.textViewGoalDescription);
            int goalId;
            try {
                goalId = Integer.parseInt(textViewGoalDescription.getHint().toString());
            } catch (NumberFormatException e) {
                goalId = 0;
            }
            if (goalId == 0) {
                buttonSetGoal.setVisibility(View.GONE);
                continue;
            }
            Goal userCurrentGoal = goalPresenter.getUserGoal();
            if (userCurrentGoal != null && userCurrentGoal.getGoalId() == goalId) {
                buttonSetGoal.setBackground(null);
                buttonSetGoal.setText("正在进行");
            } else {
                final int finalGoalId = goalId;
                if (GlobalSettingModel.getInstance().getCurrentUserName() == null
                        || "".equals(GlobalSettingModel.getInstance().getCurrentUserName())) {
                    buttonSetGoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    buttonSetGoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goalPresenter.setUserGoal(goalPresenter.getGoal(finalGoalId));
                            initRecyclerView();
                        }
                    });
                }
            }
        }
    }

}
