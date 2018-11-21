package org.ihci.itbs.presenter;

import org.ihci.itbs.contract.GoalContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.GoalModel;
import org.ihci.itbs.model.UserModel;
import org.ihci.itbs.model.pojo.Goal;
import org.ihci.itbs.model.pojo.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class GoalPresenter implements GoalContract.Presenter {

    private WeakReference<GoalContract.View> viewWeakReference;
    private GoalModel model;

    public GoalPresenter(GoalContract.View view) {
        this.viewWeakReference = new WeakReference<>(view);
        model = new GoalModel(this);
    }

    @Override
    public List<Goal> listAllGoal() {
        List<Goal> goals = model.listGoal();
        if (goals == null) {
            return new ArrayList<>();
        }
        Collections.sort(goals, new Comparator<Goal>() {
            @Override
            public int compare(Goal o1, Goal o2) {
                return o1.getGoalId() > o2.getGoalId() ? 1 : o1.getGoalId() == o2.getGoalId() ? 0 : -1;
            }
        });
        return goals;
    }

    @Override
    public Goal getGoal(int goldId) {
        return model.getGoal(goldId);
    }

    @Override
    public Goal getUserGoal() {
        UserModel userModel = new UserModel(this);
        User user = userModel.getLocalUser(GlobalSettingModel.getInstance().getCurrentUserName());
        return model.getGoal(user.getGoal().getGoalId());
    }

    @Override
    public void setUserGoal(Goal goal) {
        UserModel userModel = new UserModel(this);
        User user = userModel.getLocalUser(GlobalSettingModel.getInstance().getCurrentUserName());
        user.setGoal(goal);
        userModel.updateUser(user.getUserName(), user);
    }

    @Override
    public void notifyUpdate() {
        viewWeakReference.get().showGoalList(listAllGoal());
    }
}
