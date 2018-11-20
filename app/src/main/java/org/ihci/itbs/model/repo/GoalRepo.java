package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.Goal;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface GoalRepo {

    List<Goal> listAllGoal();

    Goal getGoal(int id);


}
