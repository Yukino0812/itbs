package org.ihci.itbs.model;

import android.content.Context;
import android.support.annotation.NonNull;

import org.ihci.itbs.view.ItbsApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class GlobalSettingModel implements Serializable {

    private static GlobalSettingModel INSTANCE = null;

    private String currentUserName;
    private UiTheme currentTheme;
    private boolean recommendSetting;

    private GlobalSettingModel() {

    }

    public static GlobalSettingModel getInstance() {
        if (INSTANCE == null) {
            read();
        }
        if (INSTANCE == null) {
            INSTANCE = new GlobalSettingModel();
        }
        return INSTANCE;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
        save();
    }

    public UiTheme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(UiTheme currentTheme) {
        this.currentTheme = currentTheme;
        save();
    }

    public boolean isRecommendSetting() {
        return recommendSetting;
    }

    public void setRecommendSetting(boolean recommendSetting) {
        this.recommendSetting = recommendSetting;
        save();
    }

    private static void save() {
        final String fileName = "global_setting_model.txt";

        ThreadPoolExecutor singleThreadForSave = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "GlobalSettingModel Single Thread For Save");
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
        final String fileName = "global_setting_model.txt";

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(ItbsApplication.getContext().openFileInput(fileName));
            INSTANCE = (GlobalSettingModel) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public enum UiTheme {

        /**
         * UI theme boy style
         */
        BOY,

        /**
         * UI theme girl style
         */
        GIRL,

    }

}
