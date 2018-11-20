package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.User;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface UserRepo {

    List<User> listLocalUser();

    boolean addUser(User user);

    User getUser(String userName);

    void updateUser(String oldUserName, User newUser);

}
