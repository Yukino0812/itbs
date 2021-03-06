package org.ihci.itbs.model;

import android.support.annotation.NonNull;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.model.pojo.Toothbrush;
import org.ihci.itbs.model.repo.ToothbrushLocalRepo;
import org.ihci.itbs.model.repo.ToothbrushRemoteRepo;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class ToothbrushModel {

    private BasePresenter presenter;
    private ThreadPoolExecutor singleThread;

    public ToothbrushModel(BasePresenter presenter) {
        this.presenter = presenter;
        initThread();
    }

    public List<Toothbrush> listLocalToothbrush() {
        return ToothbrushLocalRepo.getInstance().listLocalToothbrush();
    }

    public Toothbrush getToothbrush(int toothbrushId) {
        Toothbrush toothbrush = ToothbrushLocalRepo.getInstance().getToothbrush(toothbrushId);
        if (toothbrush == null) {
            toothbrush = ToothbrushRemoteRepo.getInstance().getToothbrush(toothbrushId);
            if (toothbrush == null) {
                return null;
            } else {
                ToothbrushLocalRepo.getInstance().addToothbrush(toothbrush);
                presenter.notifyUpdate();
            }
        }
        try {
            return toothbrush.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return toothbrush;
        }
    }

    public void removeLocalToothbrush(int toothbrushId) {
        ToothbrushLocalRepo.getInstance().removeToothbrush(toothbrushId);
    }

    public void removeLocalToothbrush(Toothbrush toothbrush) {
        if (toothbrush == null) {
            return;
        }
        removeLocalToothbrush(toothbrush.getToothbrushId());
    }

    public void updateToothbrush(Toothbrush toothbrush) {
        if (toothbrush == null) {
            return;
        }
        Toothbrush newToothbrush;
        try {
            newToothbrush = toothbrush.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            newToothbrush = toothbrush;
        }
        ToothbrushLocalRepo.getInstance().updateToothbrush(newToothbrush);
        final Toothbrush finalNewToothbrush = newToothbrush;
        singleThread.execute(new Runnable() {
            @Override
            public void run() {
                ToothbrushRemoteRepo.getInstance().updateToothbrush(finalNewToothbrush);
            }
        });
    }

    private void initThread() {
        singleThread = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "ToothbrushModel Single Thread");
                    }
                });
    }

}
