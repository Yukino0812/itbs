package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.Goal;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class GoalRemoteRepo implements GoalRepo {

    private static GoalRemoteRepo INSTANCE = null;

    private GoalRemoteRepo(){

    }

    public static GoalRemoteRepo getInstance(){
        if(INSTANCE==null){
            INSTANCE = new GoalRemoteRepo();
        }
        return INSTANCE;
    }

    @Override
    public List<Goal> listAllGoal() {
        return null;
    }

    @Override
    public Goal getGoal(int id) {
        return null;
    }
}
