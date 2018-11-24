package org.ihci.itbs.presenter;

import org.ihci.itbs.contract.UserContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.UserModel;
import org.ihci.itbs.model.pojo.User;

import java.lang.ref.WeakReference;
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
        if(currentUserName == null){
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
        boolean success = userModel.checkPassword(userName, userPassword);
        if (success) {
            GlobalSettingModel.getInstance().setCurrentUserName(userName);
            userModel.getUser(userName);
        }
        return success;
    }

    @Override
    public boolean register(String userName, String userPassword) {
        if (userModel.isExistUser(userName)) {
            return false;
        }
        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        userModel.addUser(user);
        GlobalSettingModel.getInstance().setCurrentUserName(userName);
        return true;
    }

    @Override
    public void changePassword(String userName, String newUserPassword) {
        User user = userModel.getUser(userName);
        user.setUserPassword(newUserPassword);
        userModel.updateUser(userName, user);
    }

    @Override
    public void removeLocalUser(String userName) {
        userModel.removeLocalUser(userName);
    }

    @Override
    public void notifyUpdate() {

    }
}
