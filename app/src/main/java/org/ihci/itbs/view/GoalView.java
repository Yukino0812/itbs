package org.ihci.itbs.view;

import android.app.Activity;

import org.ihci.itbs.contract.GoalContract;
import org.ihci.itbs.model.pojo.Goal;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class GoalView extends Activity implements GoalContract.View {

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

    @Override
    public void showGoalList(List<Goal> goals) {

    }
}
