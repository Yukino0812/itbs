package org.ihci.itbs.model;

import android.support.annotation.NonNull;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.model.pojo.Goal;
import org.ihci.itbs.model.repo.GoalLocalRepo;
import org.ihci.itbs.model.repo.GoalRemoteRepo;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class GoalModel {

    private BasePresenter presenter;
    private ThreadPoolExecutor singleThread;

    public GoalModel(BasePresenter presenter) {
        this.presenter = presenter;
        initThread();
    }

    public List<Goal> listGoal() {
        syncRepo();
        return GoalLocalRepo.getInstance().listAllGoal();
    }

    public Goal getGoal(int goalId) {
        Goal localGoal = GoalLocalRepo.getInstance().getGoal(goalId);
        if (localGoal == null) {
            localGoal = GoalRemoteRepo.getInstance().getGoal(goalId);
            syncRepo();
        }
        try {
            return localGoal.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return localGoal;
        }
    }

    private void syncRepo() {
        singleThread.execute(new Runnable() {
            @Override
            public void run() {
                if (GoalLocalRepo.getInstance().syncRemoteRepo(GoalRemoteRepo.getInstance().listAllGoal())) {
                    presenter.notifyUpdate();
                }
            }
        });
    }

    private void initThread() {
        singleThread = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "GoalModel Single Thread");
                    }
                });
    }

}
