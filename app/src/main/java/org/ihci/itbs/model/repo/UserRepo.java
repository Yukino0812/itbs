package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.User;

/**
 * @author Yukino Yukinoshita
 */

public interface UserRepo {

    boolean addUser(User user);

    User getUser(String userName);

    void updateUser(String oldUserName, User newUser);

}
