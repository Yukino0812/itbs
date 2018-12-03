package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Goal;

import java.util.List;

/**
 * Contract defined the communication between {@code View} and {@code Presenter}.
 *
 * @author Yukino Yukinoshita
 */

public interface GoalContract {

    interface View extends BaseView {

        /**
         * Prompt {@code View} to refresh goal list
         *
         * @param goals
         */
        void showGoalList(List<Goal> goals);

        /**
         * Notify view to refresh goal state
         *
         * @param star
         * @param award
         */
        void notifyGoalFinish(int star, Award award);

    }

    interface Presenter extends BasePresenter {

        /**
         * list all goals
         *
         * @return goals
         */
        List<Goal> listAllGoal();

        /**
         * get a specified goal
         *
         * @param goalId id
         * @return goal
         */
        Goal getGoal(int goalId);

        /**
         * get user's goal
         *
         * @return goal
         */
        Goal getUserGoal();

        /**
         * set user's goal
         *
         * @param goal goal
         */
        void setUserGoal(Goal goal);

        /**
         * check whether user finish goal
         */
        void checkIsGoalFinish();

        /**
         * user finish goal and gain award
         *
         * @param star star gain
         * @param award award gain
         */
        void userFinishGoal(int star, Award award);

    }

}
