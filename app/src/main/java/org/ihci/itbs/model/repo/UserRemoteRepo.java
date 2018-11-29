package org.ihci.itbs.model.repo;

import android.content.Context;
import android.support.annotation.NonNull;

import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.view.ItbsApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class UserRemoteRepo implements UserRepo, Serializable {

    private static UserRemoteRepo INSTANCE = null;
    private ArrayList<User> userArrayList;

    private UserRemoteRepo() {

    }

    public static UserRemoteRepo getInstance() {
        if (INSTANCE == null) {
            read();
        }
        if (INSTANCE == null) {
            INSTANCE = new UserRemoteRepo();
        }
        return INSTANCE;
    }

    public boolean checkPassword(String userName, String userPassword) {
        if (userArrayList == null) {
            userArrayList = new ArrayList<>();
            save();
            return false;
        }
        for (User user : userArrayList) {
            if (user.getUserName().equals(userName)) {
                return user.getUserPassword().equals(userPassword);
            }
        }
        return false;
    }

    @Override
    public User getUser(String userName) {
        if (userArrayList == null) {
            userArrayList = new ArrayList<>();
            save();
            return null;
        }
        for (User user : userArrayList) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void updateUser(String oldUserName, User newUser) {
        if (userArrayList == null) {
            userArrayList = new ArrayList<>();
        }
        for (int i = 0; i < userArrayList.size(); ++i) {
            if (userArrayList.get(i).getUserName().equals(oldUserName)) {
                userArrayList.set(i, newUser);
                save();
                return;
            }
        }
        userArrayList.add(newUser);
        save();
    }

    @Override
    public boolean addUser(User user) {
        if (userArrayList == null) {
            userArrayList = new ArrayList<>();
        }
        for (User user1 : userArrayList) {
            if (user1.getUserName().equals(user.getUserName())) {
                return false;
            }
        }
        userArrayList.add(user);
        save();
        return true;
    }

    @Override
    public boolean isExistUser(String userName) {
        if (userArrayList == null) {
            userArrayList = new ArrayList<>();
            return false;
        }
        for (User user : userArrayList) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    private static void save() {
        final String fileName = "user_remote_repo.txt";

        ThreadPoolExecutor singleThreadForSave = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "UserRemoteRepo Single Thread For Save");
                    }
                });

        singleThreadForSave.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(ItbsApplication.getContext().openFileOutput(fileName, Context.MODE_PRIVATE));
                    objectOutputStream.writeObject(getInstance());
                    objectOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static void read() {
        final String fileName = "user_remote_repo.txt";

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(ItbsApplication.getContext().openFileInput(fileName));
            INSTANCE = (UserRemoteRepo) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
