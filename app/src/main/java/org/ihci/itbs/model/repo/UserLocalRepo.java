package org.ihci.itbs.model.repo;

import android.content.Context;
import android.support.annotation.NonNull;

import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.view.ItbsApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class UserLocalRepo implements UserRepo {

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

    @Override
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
        if("".equals(oldUserName)){
            addUser(newUser);
        }

        for (User user : localUserArrayList) {
            if (user.getUserName().equals(oldUserName)) {
                localUserArrayList.remove(user);
                try {
                    localUserArrayList.add(newUser.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    localUserArrayList.add(newUser);
                }
            }
        }
        save();
    }

    public void removeUserFromLocal(String userName) {
        for (User user : localUserArrayList) {
            if (user.getUserName().equals(userName)) {
                localUserArrayList.remove(user);
            }
        }
    }

    public void syncRemoteUser(User user){
        if(localUserArrayList==null){
            localUserArrayList = new ArrayList<>();
        }
        for(User user1:localUserArrayList){
            if(user1.getUserName().equals(user.getUserName())){
                updateUser(user1.getUserName(),user);
                return;
            }
        }
        try {
            localUserArrayList.add(user.clone());
        }catch (CloneNotSupportedException e){
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
