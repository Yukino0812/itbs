package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.User;

import java.util.List;

/**
 * Contract defined the communication between {@code View} and {@code Presenter}.
 *
 * @author Yukino Yukinoshita
 */

public interface UserContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {

        /**
         * list users that have login before stored in local
         *
         * @return users
         */
        List<User> listLocalUser();

        /**
         * login user by cache
         *
         * @return true if login successfully
         */
        boolean cacheLogin();

        /**
         * get user
         *
         * @param userName name
         * @return user
         */
        User getUser(String userName);

        /**
         * login
         *
         * @param userName name
         * @param userPassword password
         * @return true if login successfully
         */
        boolean login(String userName, String userPassword);

        /**
         * register a new user
         *
         * @param userName name
         * @param userPassword password
         * @return true if register successfully
         */
        boolean register(String userName, String userPassword);

        /**
         * change user password
         *
         * @param userName name
         * @param newUserPassword new password
         */
        void changePassword(String userName, String newUserPassword);

        /**
         * remove an user from local
         *
         * @param userName name
         */
        void removeLocalUser(String userName);

    }

}
