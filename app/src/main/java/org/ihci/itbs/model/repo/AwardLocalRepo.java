package org.ihci.itbs.model.repo;

import android.content.Context;
import android.support.annotation.NonNull;

import org.ihci.itbs.model.pojo.Award;
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

public class AwardLocalRepo implements AwardRepo, Serializable {

    private static AwardLocalRepo INSTANCE = null;
    private ArrayList<Award> localAwardArrayList;

    private AwardLocalRepo() {

    }

    public static AwardLocalRepo getInstance() {
        if (INSTANCE == null) {
            read();
        }
        if (INSTANCE == null) {
            INSTANCE = new AwardLocalRepo();
            INSTANCE.localAwardArrayList = new ArrayList<>();
        }
        return INSTANCE;
    }

    @Override
    public List<Award> listAllAward() {
        if (localAwardArrayList == null) {
            localAwardArrayList = new ArrayList<>();
        }
        return localAwardArrayList;
    }

    @Override
    public Award getAward(String awardName) {
        if (localAwardArrayList == null) {
            localAwardArrayList = new ArrayList<>();
            return null;
        }
        if (awardName == null || "".equals(awardName)) {
            return null;
        }
        for (Award award : localAwardArrayList) {
            if (award.getAwardName() == null) {
                continue;
            }
            if (award.getAwardName().equals(awardName)) {
                return award;
            }
        }
        return null;
    }

    public boolean syncRemoteRepo(List<Award> awards) {
        realSync(awards);
        return true;
    }

    private void realSync(List<Award> awards) {
        if (awards == null) {
            localAwardArrayList = new ArrayList<>();
        } else {
            localAwardArrayList = new ArrayList<>(awards);
        }

        save();
    }

    public static void save() {
        final String fileName = "award_local_repo.txt";

        ThreadPoolExecutor singleThreadForSave = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "AwardLocalRepo Single Thread For Save");
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
        final String fileName = "award_local_repo.txt";

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(ItbsApplication.getContext().openFileInput(fileName));
            INSTANCE = (AwardLocalRepo) objectInputStream.readObject();
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
