package org.ihci.itbs.model.repo;

import android.graphics.Bitmap;

import org.ihci.itbs.model.pojo.User;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface UserRepo {

    List<User> listLocalUser();

    void userLogin(String userName, String userPassword);

    void userRegister(String userName, String userPassword);

    User getUser(String userName);

    void updateUser(String oldUserName, User newUser);

    void removeUserFromLocal(String userName);

}
