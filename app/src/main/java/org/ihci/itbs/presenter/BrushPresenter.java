package org.ihci.itbs.presenter;

import android.support.annotation.NonNull;

import org.ihci.itbs.contract.BrushContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.ToothbrushModel;
import org.ihci.itbs.model.UserModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.HistoryUse;
import org.ihci.itbs.model.pojo.Toothbrush;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.model.repo.AwardRemoteRepo;

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
        if (toothbrushes == null) {
            return new ArrayList<>();
        } else {
            return toothbrushes;
        }
    }

    @Override
    public Toothbrush getCurrentToothbrush() {
        return currentToothbrush;
    }

    @Override
    public void removeToothbrush(int toothbrushId) {
        model.removeLocalToothbrush(toothbrushId);
    }

    @Override
    public int connectBrush(int toothbrushId) {
        UserModel userModel = new UserModel(this);
        User user = userModel.getLocalUser(GlobalSettingModel.getInstance().getCurrentUserName());
        List<Toothbrush> localToothbrushList = user.getToothbrushArrayList();
        if (localToothbrushList != null && localToothbrushList.size() != 0) {
            currentToothbrush = model.getToothbrush(localToothbrushList.get(0).getToothbrushId());
        } else {
            currentToothbrush = model.getToothbrush(new Random().nextInt(500000) + 1);
            localToothbrushList = new ArrayList<>();
            localToothbrushList.add(currentToothbrush);
        }
        user.setToothbrushArrayList(new ArrayList<>(localToothbrushList));
        userModel.updateUser(user.getUserName(), user);

        if (currentToothbrush == null) {
            return 0;
        } else {
            return currentToothbrush.getToothbrushId();
        }
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
        viewWeakReference.get().runOnViewThread(new Runnable() {
            @Override
            public void run() {
                viewWeakReference.get().showBrushList();
            }
        });
    }

    private void evaluateBrush() {
        gainCurrency = new Currency();
        if (startDate == null || stopDate == null || stopDate.compareTo(startDate) < 0) {
            return;
        }
        long timeDiff = stopDate.getTime() - startDate.getTime();

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

        if (timeDiff >= 30 * 1000) {
            gainAward();
        }
    }

    private void gainAward() {
        double gainAwardProbability = 0.1;
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        boolean properBrushHour = (hour > 5 && hour < 9) || (hour > 11 && hour < 14)
                || (hour > 16 && hour < 20) || (hour > 20 && hour < 23);
        if (properBrushHour) {
            gainAwardProbability += 0.1;
        }
        if (dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SATURDAY) {
            gainAwardProbability += 0.1;
        }

        if (Math.random() < gainAwardProbability) {
            List<Award> awards = AwardRemoteRepo.getInstance().listAllAward();
            ArrayList<Award> littleAwards = new ArrayList<>();
            for (Award award : awards) {
                if ("little".equals(award.getAwardType())) {
                    littleAwards.add(award);
                }
            }
            Random random = new Random();
            int randomInt = random.nextInt(littleAwards.size());
            gainAward = littleAwards.get(randomInt);
        }
    }

    private void updateToothbrush() {
        ThreadPoolExecutor singleThread = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "BrushPresenter Single Thread For Update Toothbrush");
                    }
                });

        singleThread.execute(new Runnable() {
            @Override
            public void run() {
                if (currentToothbrush == null) {
                    return;
                }
                Toothbrush toothbrush;
                try {
                    toothbrush = currentToothbrush.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    toothbrush = new Toothbrush();
                    toothbrush.setHistoryUseArrayList(currentToothbrush.getHistoryUseArrayList());
                    toothbrush.setToothbrushId(currentToothbrush.getToothbrushId());
                }
                ArrayList<HistoryUse> historyUses = new ArrayList<>(toothbrush.getHistoryUseArrayList());
                HistoryUse use = new HistoryUse();
                use.setDate(startDate);
                use.setDuration((int) (stopDate.getTime() / 1000 - startDate.getTime() / 1000));
                use.setGainCurrency(gainCurrency);
                use.setGainAward(gainAward);
                historyUses.add(use);
                toothbrush.setHistoryUseArrayList(historyUses);
                model.updateToothbrush(toothbrush);

                UserModel userModel = new UserModel(BrushPresenter.this);
                String currentUserName = GlobalSettingModel.getInstance().getCurrentUserName();

                User user = userModel.getLocalUser(currentUserName);

                if (gainAward != null) {
                    ArrayList<Award> userAward = user.getAwardArrayList();
                    if (userAward == null) {
                        userAward = new ArrayList<>();
                    }

                    userAward.add(gainAward);
                    user.setAwardArrayList(userAward);
                }

                Currency userCurrency = user.getCurrency();
                if (userCurrency == null) {
                    userCurrency = new Currency();
                }
                if (gainCurrency == null) {
                    gainCurrency = new Currency();
                }
                int junior = userCurrency.getJuniorCurrency() + gainCurrency.getJuniorCurrency();
                int senior = userCurrency.getSeniorCurrency() + gainCurrency.getSeniorCurrency();
                senior += junior / 3;
                junior %= 3;
                userCurrency.setJuniorCurrency(junior);
                userCurrency.setSeniorCurrency(senior);
                user.setCurrency(userCurrency);

                ArrayList<Toothbrush> userToothbrushList = user.getToothbrushArrayList();
                if (userToothbrushList == null) {
                    userToothbrushList = new ArrayList<>();
                    userToothbrushList.add(toothbrush);
                }
                for (Toothbrush toothbrush1 : userToothbrushList) {
                    if (toothbrush1.getToothbrushId() == toothbrush.getToothbrushId()) {
                        userToothbrushList.remove(toothbrush1);
                        userToothbrushList.add(toothbrush);
                    }
                }
                user.setToothbrushArrayList(userToothbrushList);

                userModel.updateUser(currentUserName, user);
            }
        });
    }

}
