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
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class UserLocalRepo implements UserRepo, Serializable {

    private static UserLocalRepo INSTANCE = null;
    private ArrayList<User> localUserArrayList = null;

    private UserLocalRepo() {

    }

    public static UserLocalRepo getInstance() {
        if (INSTANCE == null) {
            read();
        }
        if (INSTANCE == null) {
            INSTANCE = new UserLocalRepo();
        }
        return INSTANCE;
    }

    public List<User> listLocalUser() {
        if (localUserArrayList == null) {
            localUserArrayList = new ArrayList<>();
        }
        return localUserArrayList;
    }

    @Override
    public boolean addUser(User user) {
        if (localUserArrayList == null) {
            localUserArrayList = new ArrayList<>();
            try {
                localUserArrayList.add(user.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                localUserArrayList.add(user);
            }
            save();
            return true;
        }
        if (user == null) {
            return false;
        }
        for (User user1 : localUserArrayList) {
            if (user1.getUserName().equals(user.getUserName())) {
                return false;
            }
        }
        try {
            localUserArrayList.add(user.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            localUserArrayList.add(user);
        }
        save();
        return true;
    }

    @Override
    public User getUser(String userName) {
        if (localUserArrayList == null) {
            localUserArrayList = new ArrayList<>();
            return null;
        }
        if (userName == null || "".equals(userName)) {
            return null;
        }
        for (User user : localUserArrayList) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void updateUser(String oldUserName, User newUser) {
        if (localUserArrayList == null) {
            addUser(newUser);
            return;
        }
        if (oldUserName == null || "".equals(oldUserName)) {
            addUser(newUser);
        }
        if (newUser == null) {
            return;
        }

        for (int i = 0; i < localUserArrayList.size(); ++i) {
            if (localUserArrayList.get(i).getUserName().equals(oldUserName)) {
                try {
                    localUserArrayList.set(i, newUser.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    localUserArrayList.set(i, newUser);
                }
            }
        }
        save();
    }

    @Override
    public boolean isExistUser(String userName) {
        if (localUserArrayList == null) {
            localUserArrayList = new ArrayList<>();
            return false;
        }
        if (userName == null || "".equals(userName)) {
            return true;
        }
        for (User user : localUserArrayList) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void removeUserFromLocal(String userName) {
        if (userName == null || "".equals(userName)) {
            return;
        }
        User userToRemove = null;
        for (User user : localUserArrayList) {
            if (user.getUserName().equals(userName)) {
                userToRemove = user;
                break;
            }
        }
        if (userToRemove != null) {
            localUserArrayList.remove(userToRemove);
        }
    }

    public void syncRemoteUser(User user) {
        if (localUserArrayList == null) {
            localUserArrayList = new ArrayList<>();
        }
        if (user == null) {
            return;
        }
        for (User user1 : localUserArrayList) {
            if (user1.getUserName().equals(user.getUserName())) {
                updateUser(user1.getUserName(), user);
                return;
            }
        }
        try {
            localUserArrayList.add(user.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            localUserArrayList.add(user);
        }
        save();
    }

    public static void save() {
        final String fileName = "user_local_repo.txt";

        ThreadPoolExecutor singleThreadForSave = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "UserLocalRepo Single Thread For Save");
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
        final String fileName = "user_local_repo.txt";

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(ItbsApplication.getContext().openFileInput(fileName));
            INSTANCE = (UserLocalRepo) objectInputStream.readObject();
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
