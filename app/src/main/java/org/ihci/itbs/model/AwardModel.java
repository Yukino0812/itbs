package org.ihci.itbs.model;

import android.support.annotation.NonNull;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.repo.AwardLocalRepo;
import org.ihci.itbs.model.repo.AwardRemoteRepo;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class AwardModel {

    private BasePresenter presenter;
    private ThreadPoolExecutor singleThread;

    public AwardModel(BasePresenter presenter) {
        this.presenter = presenter;
        initThread();
    }

    public List<Award> listAward() {
        syncRepo();
        return AwardLocalRepo.getInstance().listAllAward();
    }

    public Award getAward(String awardName) {
        if(awardName==null||"".equals(awardName)){
            return null;
        }
        Award award = AwardLocalRepo.getInstance().getAward(awardName);
        if (award == null) {
            award = AwardRemoteRepo.getInstance().getAward(awardName);
            syncRepo();
        }
        try {
            return award.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return award;
        }
    }

    private void syncRepo() {
        singleThread.execute(new Runnable() {
            @Override
            public void run() {
                if (AwardLocalRepo.getInstance().syncRemoteRepo(AwardRemoteRepo.getInstance().listAllAward())) {
                    presenter.notifyUpdate();
                }
            }
        });
    }

    private void initThread() {
        singleThread = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "AwardModel Single Thread");
                    }
                });
    }

}
