package org.ihci.itbs.presenter;

import android.support.annotation.NonNull;

import org.ihci.itbs.contract.BrushContract;
import org.ihci.itbs.model.AwardModel;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.ToothbrushModel;
import org.ihci.itbs.model.UserModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.HistoryUse;
import org.ihci.itbs.model.pojo.Toothbrush;
import org.ihci.itbs.model.repo.AwardLocalRepo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class BrushPresenter implements BrushContract.Presenter {

    private WeakReference<BrushContract.View> viewWeakReference;
    private ToothbrushModel model;

    private Toothbrush currentToothbrush;
    private Date startDate;
    private Date stopDate;
    private Currency gainCurrency;
    private Award gainAward;

    public BrushPresenter(BrushContract.View view) {
        this.viewWeakReference = new WeakReference<>(view);
        model = new ToothbrushModel(this);
    }

    @Override
    public List<Toothbrush> listToothbrush() {
        List<Toothbrush> toothbrushes = model.listLocalToothbrush();
        if(toothbrushes==null){
            return new ArrayList<>();
        }else {
            return toothbrushes;
        }
    }

    @Override
    public void removeToothbrush(int toothbrushId) {
        model.removeLocalToothbrush(toothbrushId);
    }

    @Override
    public int connectBrush(int toothbrushId) {
        List<Toothbrush> localToothbrushList = listToothbrush();
        if(localToothbrushList==null||toothbrushId==0){
            currentToothbrush = model.getToothbrush(new Random().nextInt(500000)+1);
        }else {
            currentToothbrush = model.getToothbrush(toothbrushId);
        }
        return currentToothbrush.getToothbrushId();
    }

    @Override
    public Toothbrush getToothbrush(int toothbrushId) {
        return model.getToothbrush(toothbrushId);
    }

    @Override
    public void startBrush() {
        this.startDate = new Date();
    }

    @Override
    public void stopBrush() {
        this.stopDate = new Date();
        evaluateBrush();
        gainAward();
        updateToothbrush();
    }

    @Override
    public Currency getCurrencyGainThisBrush() {
        if (gainCurrency != null) {
            return gainCurrency;
        } else {
            return new Currency();
        }
    }

    @Override
    public Award getAwardGainThisBrush() {
        return gainAward;
    }

    @Override
    public void notifyUpdate() {

    }

    private void evaluateBrush() {
        if (startDate == null || stopDate == null || stopDate.compareTo(startDate) > 0) {
            return;
        }
        long timeDiff = stopDate.getTime() - startDate.getTime();
        gainCurrency = new Currency();
        if (timeDiff < 15 * 1000) {
            // get 0 star
        } else if (timeDiff < 30 * 1000) {
            // get 0 or 1 star randomly
            if (Math.random() < 0.5) {
                gainCurrency.setJuniorCurrency(1);
            }
        } else if (timeDiff < 60 * 1000) {
            // get 1 or 2 star randomly
            if (Math.random() < 0.5) {
                gainCurrency.setJuniorCurrency(2);
            } else {
                gainCurrency.setJuniorCurrency(1);
            }
        } else if (timeDiff < 120 * 1000) {
            // get 2 or 3 star randomly
            if (Math.random() < 0.5) {
                gainCurrency.setJuniorCurrency(3);
            } else {
                gainCurrency.setJuniorCurrency(2);
            }
        } else {
            gainCurrency.setJuniorCurrency(3);
        }
    }

    private void gainAward(){
        double gainAwardProbability = 0.1;
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if ((hour > 5 && hour < 9)
                || (hour > 11 && hour < 14)
                || (hour > 16 && hour < 20)
                || (hour > 20 && hour < 23)) {
            gainAwardProbability += 0.1;
        }
        if(dayOfWeek==Calendar.FRIDAY||dayOfWeek==Calendar.SATURDAY){
            gainAwardProbability += 0.1;
        }

        if(Math.random()<gainAwardProbability){
            List<Award> awards = AwardLocalRepo.getInstance().listAllAward();
            ArrayList<Award> littleAwards = new ArrayList<>();
            for(Award award:awards){
                if(award.getAwardType().equals("little")){
                    littleAwards.add(award);
                }
            }
            Random random = new Random();
            int randomInt = random.nextInt(littleAwards.size());
            gainAward = littleAwards.get(randomInt);
        }
    }

    private void updateToothbrush(){
        ThreadPoolExecutor singleThreadForSave = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "BrushPresenter Single Thread For Update Toothbrush");
                    }
                });

        singleThreadForSave.execute(new Runnable() {
            @Override
            public void run() {
                Toothbrush toothbrush;
                try {
                    toothbrush = currentToothbrush.clone();
                }catch (CloneNotSupportedException e){
                    e.printStackTrace();
                    toothbrush = new Toothbrush();
                    toothbrush.setHistoryUseArrayList(currentToothbrush.getHistoryUseArrayList());
                    toothbrush.setToothbrushId(currentToothbrush.getToothbrushId());
                }
                ArrayList<HistoryUse> historyUses = new ArrayList<>(toothbrush.getHistoryUseArrayList());
                HistoryUse use = new HistoryUse();
                use.setDate(startDate);
                use.setDuration((int)(stopDate.getTime()/1000 - startDate.getTime()/1000));
                use.setGainCurrency(gainCurrency);
                historyUses.add(use);
                toothbrush.setHistoryUseArrayList(historyUses);
                model.updateToothbrush(toothbrush);

                UserModel userModel = new UserModel(BrushPresenter.this);
                String currentUserName = GlobalSettingModel.getInstance().getCurrentUserName();
                userModel.updateUser(currentUserName, userModel.getLocalUser(currentUserName));
            }
        });
    }

}
