package org.ihci.itbs.presenter;

import org.ihci.itbs.contract.GoalContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.GoalModel;
import org.ihci.itbs.model.UserModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.Goal;
import org.ihci.itbs.model.pojo.HistoryUse;
import org.ihci.itbs.model.pojo.Toothbrush;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.util.DateSelector;
import org.jetbrains.annotations.Contract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class GoalPresenter implements GoalContract.Presenter {

    private WeakReference<GoalContract.View> viewWeakReference;
    private GoalModel model;
    private UserModel userModel;

    public GoalPresenter(GoalContract.View view) {
        this.viewWeakReference = new WeakReference<>(view);
        model = new GoalModel(this);
        userModel = new UserModel(this);
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
        User user = userModel.getLocalUser(GlobalSettingModel.getInstance().getCurrentUserName());
        if (user == null) {
            return null;
        }
        return user.getGoal();
    }

    @Override
    public void setUserGoal(Goal goal) {
        if (goal == null) {
            return;
        }
        User user = userModel.getLocalUser(GlobalSettingModel.getInstance().getCurrentUserName());
        goal.setStartDate(new Date());
        user.setGoal(goal);
        userModel.updateUser(user.getUserName(), user);
    }

    @Override
    public void checkIsGoalFinish() {
        Goal goal = getUserGoal();
        if (goal == null) {
            return;
        }
        int days;
        switch (goal.getGoalId()) {
            case 1:
                days = 1;
                break;
            case 2:
                days = 3;
                break;
            case 3:
                days = 7;
                break;
            case 4:
                days = 15;
                break;
            case 5:
                days = 21;
                break;
            case 6:
                days = 30;
                break;
            default:
                days = 0;
        }
        if (days == 0) {
            return;
        }
        if (checkBrushContinuous(goal.getStartDate(), days)) {
            viewWeakReference.get().notifyGoalFinish(0, goal.getAward());
        }
    }

    @Override
    public void userFinishGoal(int star, Award award) {
        if (award == null) {
            return;
        }
        User user = userModel.getLocalUser(GlobalSettingModel.getInstance().getCurrentUserName());
        if (user == null) {
            return;
        }
        Currency currency = user.getCurrency();
        if (currency == null) {
            currency = new Currency();
        }
        int totalStar = currency.getSeniorCurrency() * 3 + currency.getJuniorCurrency();
        totalStar += star;
        currency.setSeniorCurrency(totalStar / 3);
        currency.setJuniorCurrency(totalStar % 3);

        ArrayList<Award> awardList = user.getAwardArrayList();
        if (awardList == null) {
            awardList = new ArrayList<>();
        }
        awardList.add(award);

        user.setGoal(null);
        user.setCurrency(currency);
        user.setAwardArrayList(awardList);
        userModel.updateUser(user.getUserName(), user);
    }

    @Override
    public void notifyUpdate() {
        viewWeakReference.get().runOnViewThread(new Runnable() {
            @Override
            public void run() {
                viewWeakReference.get().showGoalList();
            }
        });
    }

    @Contract("null, _ -> false")
    private boolean checkBrushContinuous(Date startDate, int days) {
        if (startDate == null) {
            return false;
        }
        User user = userModel.getLocalUser(GlobalSettingModel.getInstance().getCurrentUserName());
        if (user == null) {
            return false;
        }
        ArrayList<Toothbrush> toothbrushes = user.getToothbrushArrayList();
        if (toothbrushes == null) {
            return false;
        }
        ArrayList<HistoryUse> usesAll = new ArrayList<>();
        for (Toothbrush toothbrush : toothbrushes) {
            if (toothbrush == null) {
                continue;
            }
            List<HistoryUse> historyUses = toothbrush.getHistoryUseArrayList();
            usesAll.addAll(historyUses);
        }
        Collections.sort(usesAll, new Comparator<HistoryUse>() {
            @Override
            public int compare(HistoryUse o1, HistoryUse o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        ArrayList<HistoryUse> usesInGoal = new ArrayList<>();
        Date endDate = new Date();
        for (HistoryUse use : usesAll) {
            if (use.getDate().compareTo(startDate) >= 0 && use.getDate().compareTo(endDate) <= 0) {
                usesInGoal.add(use);
            } else if (use.getDate().compareTo(endDate) > 0) {
                break;
            }
        }

        if (usesInGoal.size() < days) {
            return false;
        }

        int continuousDays = 0;
        Date dateStart = startDate;
        Date dateEnd = DateSelector.getEndTimeThisDay(dateStart);
        for (HistoryUse use : usesAll) {
            if (use.getDate().compareTo(dateStart) >= 0 && use.getDate().compareTo(dateEnd) <= 0) {
                continuousDays++;
                dateStart = DateSelector.getStartTimeThisDay(DateSelector.getDaysAfter(dateStart, 1));
                dateEnd = DateSelector.getEndTimeThisDay(dateStart);
            }
            if (continuousDays >= days) {
                return true;
            }
        }
        return continuousDays >= days;
    }

}
