package org.ihci.itbs.model.repo;

import android.content.Context;
import android.support.annotation.NonNull;

import org.ihci.itbs.model.pojo.Goal;
import org.ihci.itbs.view.ItbsApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class GoalLocalRepo implements GoalRepo, Serializable {

    private static GoalLocalRepo INSTANCE = null;
    private ArrayList<Goal> localGoalArrayList;

    private GoalLocalRepo() {

    }

    public static GoalLocalRepo getInstance() {
        if (INSTANCE == null) {
            read();
        }
        if (INSTANCE == null) {
            INSTANCE = new GoalLocalRepo();
        }
        return INSTANCE;
    }

    @Override
    public List<Goal> listAllGoal() {
        if (localGoalArrayList == null) {
            localGoalArrayList = new ArrayList<>();
        }
        return localGoalArrayList;
    }

    @Override
    public Goal getGoal(int id) {
        if (localGoalArrayList == null) {
            localGoalArrayList = new ArrayList<>();
            return null;
        }
        for (Goal goal : localGoalArrayList) {
            if (goal.getGoalId() == id) {
                return goal;
            }
        }
        return null;
    }

    public boolean syncRemoteRepo(final List<Goal> goals) {
        if (goals == null) {
            return false;
        }
        if (localGoalArrayList == null) {
            realSync(goals);
            return true;
        }
        ArrayList<Goal> arrayList1 = new ArrayList<>(goals);
        Collections.sort(arrayList1, new Comparator<Goal>() {
            @Override
            public int compare(Goal o1, Goal o2) {
                return o1.getGoalId() > o2.getGoalId() ? 1 : o1.getGoalId() == o2.getGoalId() ? 0 : -1;
            }
        });
        ArrayList<Goal> arrayList2 = new ArrayList<>(localGoalArrayList);
        Collections.sort(arrayList2, new Comparator<Goal>() {
            @Override
            public int compare(Goal o1, Goal o2) {
                return o1.getGoalId() > o2.getGoalId() ? 1 : o1.getGoalId() == o2.getGoalId() ? 0 : -1;
            }
        });
        if (arrayList1.size() != arrayList2.size()) {
            realSync(goals);
            return true;
        }
        for (int index = 0; index < arrayList1.size(); ++index) {
            if (arrayList1.get(index).getGoalId() != arrayList2.get(index).getGoalId()) {
                realSync(goals);
                return true;
            }
        }
        return false;
    }

    private void realSync(List<Goal> goals) {
        if (goals == null) {
            localGoalArrayList = new ArrayList<>();
        } else {
            localGoalArrayList = new ArrayList<>(goals);
        }

        save();
    }

    public static void save() {
        final String fileName = "goal_local_repo.txt";

        ThreadPoolExecutor singleThreadForSave = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "GoalLocalRepo Single Thread For Save");
                    }
                });

        singleThreadForSave.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(ItbsApplication.getContext().openFileOutput(fileName, Context.MODE_PRIVATE));
                    objectOutputStream.writeObject(getInstance());
                    objectOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static void read() {
        final String fileName = "goal_local_repo.txt";

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(ItbsApplication.getContext().openFileInput(fileName));
            INSTANCE = (GoalLocalRepo) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
