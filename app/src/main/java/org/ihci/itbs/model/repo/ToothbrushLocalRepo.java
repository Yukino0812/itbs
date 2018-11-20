package org.ihci.itbs.model.repo;

import android.content.Context;
import android.support.annotation.NonNull;

import org.ihci.itbs.model.pojo.Toothbrush;
import org.ihci.itbs.view.ItbsApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class ToothbrushLocalRepo implements ToothbrushRepo {

    private static ToothbrushLocalRepo INSTANCE = null;
    private ArrayList<Toothbrush> localToothbrushArrayList;

    private ToothbrushLocalRepo() {

    }

    public static ToothbrushLocalRepo getInstance() {
        if (INSTANCE == null) {
            read();
        }
        if (INSTANCE == null) {
            INSTANCE = new ToothbrushLocalRepo();
        }
        return INSTANCE;
    }

    @Override
    public Toothbrush getToothbrush(int toothbrushId) {
        if (localToothbrushArrayList == null) {
            localToothbrushArrayList = new ArrayList<>();
            return null;
        }
        for (Toothbrush toothbrush : localToothbrushArrayList) {
            if (toothbrush.getToothbrushId() == toothbrushId) {
                return toothbrush;
            }
        }
        return null;
    }

    @Override
    public void updateToothbrush(Toothbrush toothbrush) {
        if (localToothbrushArrayList == null) {
            localToothbrushArrayList = new ArrayList<>();
            addToothbrush(toothbrush);
            return;
        }
        for (Toothbrush toothbrush1 : localToothbrushArrayList) {
            if (toothbrush1.getToothbrushId() == toothbrush.getToothbrushId()) {
                toothbrush1.setHistoryUse(toothbrush.getHistoryUse());
            }
        }
        save();
    }

    public void addToothbrush(Toothbrush toothbrush) {
        if (localToothbrushArrayList == null) {
            localToothbrushArrayList = new ArrayList<>();
        }
        localToothbrushArrayList.add(toothbrush);
        save();
    }

    public void removeToothbrush(Toothbrush toothbrush) {
        if (localToothbrushArrayList == null) {
            localToothbrushArrayList = new ArrayList<>();
            return;
        }
        for (Toothbrush toothbrush1 : localToothbrushArrayList) {
            if (toothbrush1.getToothbrushId() == toothbrush.getToothbrushId()) {
                localToothbrushArrayList.remove(toothbrush1);
            }
        }
        save();
    }

    public static void save() {
        final String fileName = "toothbrush_local_repo.txt";

        ThreadPoolExecutor singleThreadForSave = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "ToothbrushLocalRepo Single Thread For Save");
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
        final String fileName = "toothbrush_local_repo.txt";

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(ItbsApplication.getContext().openFileInput(fileName));
            INSTANCE = (ToothbrushLocalRepo) objectInputStream.readObject();
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
