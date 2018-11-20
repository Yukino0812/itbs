package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.Goal;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface GoalContract {

    interface View extends BaseView {

        void showGoalList(List<Goal> goals);

    }

    interface Presenter extends BasePresenter {

        List<Goal> listAllGoal();

        Goal getGoal(int goalId);

        Goal getUserGoal();

        void setUserGoal(Goal goal);

    }

}
