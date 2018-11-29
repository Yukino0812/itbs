package org.ihci.itbs.model.repo;

import android.content.Context;
import android.support.annotation.NonNull;

import org.ihci.itbs.model.pojo.HistoryUse;
import org.ihci.itbs.model.pojo.Toothbrush;
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

public class ToothbrushRemoteRepo implements ToothbrushRepo, Serializable {

    private static ToothbrushRemoteRepo INSTANCE = null;
    private ArrayList<Toothbrush> toothbrushArrayList;

    private ToothbrushRemoteRepo() {

    }

    public static ToothbrushRemoteRepo getInstance() {
        if (INSTANCE == null) {
            read();
        }
        if (INSTANCE == null) {
            INSTANCE = new ToothbrushRemoteRepo();
        }
        return INSTANCE;
    }

    @Override
    public Toothbrush getToothbrush(int toothbrushId) {
        if (toothbrushId < 1 || toothbrushId > 500000) {
            return null;
        }
        if (toothbrushArrayList == null) {
            toothbrushArrayList = new ArrayList<>();
        }
        for (Toothbrush toothbrush : toothbrushArrayList) {
            if (toothbrush.getToothbrushId() == toothbrushId) {
                return toothbrush;
            }
        }
        Toothbrush newToothbrush = new Toothbrush();
        newToothbrush.setToothbrushId(toothbrushId);
        newToothbrush.setHistoryUseArrayList(new ArrayList<HistoryUse>());
        toothbrushArrayList.add(newToothbrush);
        save();
        return newToothbrush;
    }

    @Override
    public void updateToothbrush(Toothbrush toothbrush) {
        if (toothbrushArrayList == null) {
            toothbrushArrayList = new ArrayList<>();
        }
        for (Toothbrush toothbrush1 : toothbrushArrayList) {
            if (toothbrush1.getToothbrushId() == toothbrush.getToothbrushId()) {
                toothbrush1.setHistoryUseArrayList(new ArrayList<>(toothbrush.getHistoryUseArrayList()));
                save();
                return;
            }
        }
        toothbrushArrayList.add(toothbrush);
        save();
    }

    private static void save() {
        final String fileName = "toothbrush_remote_repo.txt";

        ThreadPoolExecutor singleThreadForSave = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "ToothbrushRemoteRepo Single Thread For Save");
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
        final String fileName = "toothbrush_remote_repo.txt";

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(ItbsApplication.getContext().openFileInput(fileName));
            INSTANCE = (ToothbrushRemoteRepo) objectInputStream.readObject();
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
