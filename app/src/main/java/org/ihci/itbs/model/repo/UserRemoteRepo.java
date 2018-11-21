package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.User;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class UserRemoteRepo implements UserRepo {

    private static UserRemoteRepo INSTANCE = null;

    private UserRemoteRepo() {

    }

    public static UserRemoteRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRemoteRepo();
        }
        return INSTANCE;
    }

    public boolean checkPassword(String userName, String userPassword){
        return false;
    }

    @Override
    public List<User> listLocalUser() {
        return null;
    }


    @Override
    public User getUser(String userName) {
        return null;
    }

    @Override
    public void updateUser(String oldUserName, User newUser) {

    }

    @Override
    public boolean addUser(User user) {
        return false;
    }
}
