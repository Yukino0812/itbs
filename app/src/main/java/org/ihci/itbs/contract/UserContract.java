package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.User;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface UserContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {

        List<User> listLocalUser();

        User getUser(String userName);

        boolean login(String userName, String userPassword);

        boolean register(String userName, String userPassword);

        void changePassword(String userName, String newUserPassword);

        void removeLocalUser(String userName);

    }

}
