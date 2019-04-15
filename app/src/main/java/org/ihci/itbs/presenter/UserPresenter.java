package org.ihci.itbs.presenter;

import org.ihci.itbs.contract.UserContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.UserModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.HistoryUse;
import org.ihci.itbs.model.pojo.Toothbrush;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.model.repo.AwardRemoteRepo;
import org.ihci.itbs.util.DateSelector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class UserPresenter implements UserContract.Presenter {

    private WeakReference<UserContract.View> viewWeakReference;
    private UserModel userModel;

    public UserPresenter(UserContract.View view) {
        this.viewWeakReference = new WeakReference<>(view);
        this.userModel = new UserModel(this);
    }

    @Override
    public List<User> listLocalUser() {
        return userModel.listLocalUser();
    }

    @Override
    public boolean cacheLogin() {
        String currentUserName = GlobalSettingModel.getInstance().getCurrentUserName();
        if (currentUserName == null) {
            return false;
        }
        User user = userModel.getLocalUser(currentUserName);
        boolean success = userModel.checkPassword(user.getUserName(), user.getUserPassword());
        if (!success) {
            userModel.removeLocalUser(user.getUserName());
            GlobalSettingModel.getInstance().setCurrentUserName(null);
        }
        return success;
    }

    @Override
    public User getUser(String userName) {
        return userModel.getUser(userName);
    }

    @Override
    public boolean login(String userName, String userPassword) {
        if (userName == null || "".equals(userName) || userPassword == null || "".equals(userPassword)) {
            return false;
        }
        boolean success = userModel.checkPassword(userName, userPassword);
        if (success) {
            GlobalSettingModel.getInstance().setCurrentUserName(userName);
            userModel.getUser(userName);
        }
        return success;
    }

    @Override
    public boolean register(String userName, String userPassword) {
        if (userName == null || "".equals(userName) || userPassword == null || "".equals(userPassword)) {
            return false;
        }
        if (userModel.isExistUser(userName)) {
            return false;
        }
        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAwardArrayList(new ArrayList<Award>());
        user.setToothbrushArrayList(new ArrayList<Toothbrush>());
        user.setCurrency(new Currency());
        userModel.addUser(user);
        GlobalSettingModel.getInstance().setCurrentUserName(userName);
        return true;
    }

    @Override
    public void changePassword(String userName, String newUserPassword) {
        if (userName == null || "".equals(userName) || newUserPassword == null || "".equals(newUserPassword)) {
            return;
        }
        User user = userModel.getUser(userName);
        user.setUserPassword(newUserPassword);
        userModel.updateUser(userName, user);
    }

    @Override
    public void removeLocalUser(String userName) {
        if (userName == null || "".equals(userName)) {
            return;
        }
        userModel.removeLocalUser(userName);
    }

    @Override
    public void notifyUpdate() {

    }

    @Override
    public void getDebugRes() {
        User user = userModel.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
        if (user == null) {
            return;
        }
        Date date = new Date();

        Award awardBlock = AwardRemoteRepo.getInstance().getAward("block");
        Award awardDuck = AwardRemoteRepo.getInstance().getAward("duck");
        addUserDebugRes(user, DateSelector.getDaysAfter(date, -7), 2, awardBlock);
        addUserDebugRes(user, DateSelector.getDaysAfter(date, -6), 7, awardBlock);
        addUserDebugRes(user, DateSelector.getDaysAfter(date, -5), 6, awardDuck);
        addUserDebugRes(user, DateSelector.getDaysAfter(date, -4), 4, awardBlock);
        addUserDebugRes(user, DateSelector.getDaysAfter(date, -3), 3, awardBlock);
        addUserDebugRes(user, DateSelector.getDaysAfter(date, -2), 1, null);
        addUserDebugRes(user, DateSelector.getDaysAfter(date, -1), 1, awardDuck);

        userModel.updateUser(user.getUserName(), user);
    }

    private void addUserDebugRes(User user, Date date, int star, Award award) {
        if (user == null) {
            return;
        }

        List<Toothbrush> toothbrushes = user.getToothbrushArrayList();
        if (toothbrushes == null) {
            return;
        }
        ArrayList<Toothbrush> toothbrushArrayList = new ArrayList<>(toothbrushes);
        Toothbrush toothbrush;
        if (toothbrushArrayList.size() == 0) {
            toothbrush = new Toothbrush();
        } else {
            toothbrush = toothbrushArrayList.get(0);
        }

        List<HistoryUse> historyUses = toothbrush.getHistoryUseArrayList();
        ArrayList<HistoryUse> newUses;
        if (historyUses == null) {
            newUses = new ArrayList<>();
        } else {
            newUses = new ArrayList<>(historyUses);
        }

        int restStar = star;
        while (restStar != 0) {
            HistoryUse historyUse = new HistoryUse();
            if (restStar <= 3) {
                Currency currency = new Currency();
                currency.setJuniorCurrency(restStar % 3);
                currency.setSeniorCurrency(restStar / 3);
                historyUse.setGainCurrency(currency);
                if (award != null) {
                    historyUse.setGainAward(award);
                }
                historyUse.setDate(date);
                historyUse.setDuration(50);
                restStar = 0;
            } else {
                restStar -= 3;
                Currency currency = new Currency();
                currency.setSeniorCurrency(1);
                historyUse.setGainCurrency(currency);
                if (restStar == 0 && award != null) {
                    historyUse.setGainAward(award);
                }
                historyUse.setDate(date);
                historyUse.setDuration(50);
            }
            newUses.add(historyUse);
        }
        toothbrush.setHistoryUseArrayList(newUses);
        user.setToothbrushArrayList(toothbrushArrayList);
    }

}
