package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Goal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class GoalRemoteRepo implements GoalRepo {

    private static GoalRemoteRepo INSTANCE = null;
    private List<Goal> goalList;

    private GoalRemoteRepo() {

    }

    public static GoalRemoteRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoalRemoteRepo();
        }
        return INSTANCE;
    }

    @Override
    public List<Goal> listAllGoal() {
        if (goalList != null) {
            return goalList;
        }

        AwardRemoteRepo awardRepo = AwardRemoteRepo.getInstance();
        ArrayList<Goal> goals = new ArrayList<>();
        goals.add(getNewGoal(1, "今天刷牙了吗？", awardRepo.getAward("block")));
        goals.add(getNewGoal(2, "坚持刷牙3天", awardRepo.getAward("car")));
        goals.add(getNewGoal(3, "坚持刷牙一周", awardRepo.getAward("ball")));
        goals.add(getNewGoal(4, "半个月不过如此", awardRepo.getAward("windmill")));
        goals.add(getNewGoal(5, "21天消灭蛀牙", awardRepo.getAward("bear")));
        goals.add(getNewGoal(6, "一个月不间断", awardRepo.getAward("doraemon")));

        goalList = goals;

        return goals;
    }

    @Override
    public Goal getGoal(int id) {
        if (goalList == null) {
            listAllGoal();
        }
        for (Goal goal : goalList) {
            if (goal.getGoalId() == id) {
                return goal;
            }
        }
        return null;
    }

    private Goal getNewGoal(int goalId, String goalContent, Award award) {
        Goal goal = new Goal();
        goal.setGoalId(goalId);
        goal.setContent(goalContent);
        goal.setAward(award);
        return goal;
    }
}
