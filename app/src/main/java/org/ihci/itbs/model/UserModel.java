package org.ihci.itbs.model;

import android.support.annotation.NonNull;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.model.repo.UserLocalRepo;
import org.ihci.itbs.model.repo.UserRemoteRepo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class UserModel {

    private BasePresenter presenter;
    private ThreadPoolExecutor singleThread;

    public UserModel(BasePresenter presenter) {
        this.presenter = presenter;
        initThread();
    }

    public List<User> listLocalUser() {
        return UserLocalRepo.getInstance().listLocalUser();
    }

    public User getLocalUser(String userName) {
        User localUser = UserLocalRepo.getInstance().getUser(userName);
        try {
            return localUser.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return localUser;
        }
    }

    public User getUser(String userName) {
        User localUser = UserLocalRepo.getInstance().getUser(userName);
        User remoteUser = UserRemoteRepo.getInstance().getUser(userName);
        syncUser(localUser, remoteUser);
        try {
            return remoteUser.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return remoteUser;
        }
    }

    public boolean checkPassword(String userName, String userPassword) {
        return UserRemoteRepo.getInstance().checkPassword(userName, userPassword);
    }

    public void addUser(User user) {
        User newUser;
        try {
            newUser = user.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            newUser = user;
        }
        newUser.setLastUpdate(new Date());
        UserLocalRepo.getInstance().addUser(newUser);
        final User finalNewUser = newUser;
        singleThread.execute(new Runnable() {
            @Override
            public void run() {
                UserRemoteRepo.getInstance().addUser(finalNewUser);
            }
        });
    }

    public void removeLocalUser(User user) {
        if (user == null) {
            return;
        }
        removeLocalUser(user.getUserName());
    }

    public void removeLocalUser(String userName) {
        UserLocalRepo.getInstance().removeUserFromLocal(userName);
    }

    public void updateUser(String userName, User user) {
        if (user == null) {
            return;
        }
        User newUser;
        try {
            newUser = user.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            newUser = user;
        }
        newUser.setLastUpdate(new Date());
        UserLocalRepo.getInstance().updateUser(userName, newUser);
    }

    private void syncUser(User localUser, User remoteUser) {
        if (localUser == null) {
            UserLocalRepo.getInstance().syncRemoteUser(remoteUser);
            return;
        }
        int compareValue = localUser.getLastUpdate().compareTo(remoteUser.getLastUpdate());
        if (compareValue > 0) {
            UserRemoteRepo.getInstance().updateUser(remoteUser.getUserName(), localUser);
        } else if (compareValue < 0) {
            UserLocalRepo.getInstance().updateUser(localUser.getUserName(), remoteUser);
        } else {
            if (!isConsistent(localUser, remoteUser)) {
                UserLocalRepo.getInstance().updateUser(localUser.getUserName(), remoteUser);
            }
        }
    }

    private boolean isConsistent(User localUser, User remoteUser) {
        return true;
    }

    private void initThread() {
        singleThread = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "UserModel Single Thread");
                    }
                });
    }

}
