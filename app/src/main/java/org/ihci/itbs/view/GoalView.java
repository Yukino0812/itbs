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

import org.ihci.itbs.R;
import org.ihci.itbs.contract.GoalContract;
import org.ihci.itbs.model.pojo.Goal;
import org.ihci.itbs.presenter.GoalPresenter;
import org.ihci.itbs.view.adapter.GoalAdapter;

import java.util.List;

/**
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
    public void showGoalList(List<Goal> goals) {

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
