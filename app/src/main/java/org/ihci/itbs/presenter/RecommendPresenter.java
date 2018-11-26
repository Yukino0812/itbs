package org.ihci.itbs.presenter;

import android.support.annotation.NonNull;

import org.ihci.itbs.contract.RecommendContract;
import org.ihci.itbs.model.RecommendModel;
import org.ihci.itbs.model.pojo.RecommendItem;
import org.ihci.itbs.util.DateSelector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukino Yukinoshita
 */

public class RecommendPresenter implements RecommendContract.Presenter {

    private WeakReference<RecommendContract.View> viewWeakReference;
    private RecommendModel model;

    public RecommendPresenter(RecommendContract.View view) {
        this.viewWeakReference = new WeakReference<>(view);
        this.model = new RecommendModel(this);
    }

    @Override
    public List<RecommendItem> listRecommendItem() {
        List<RecommendItem> items = model.listRecommendItem();
        if (items == null) {
            return null;
        }
        ArrayList<RecommendItem> recommendItemArrayList = new ArrayList<>(items);
        Collections.sort(recommendItemArrayList, new Comparator<RecommendItem>() {
            @Override
            public int compare(RecommendItem o1, RecommendItem o2) {
                return -o1.getUpdateDate().compareTo(o2.getUpdateDate());
            }
        });
        return recommendItemArrayList;
    }

    @Override
    public void removeRecommendItem(String link) {
        ArrayList<RecommendItem> recommendItemArrayList = new ArrayList<>(listRecommendItem());
        for (RecommendItem recommendItem : recommendItemArrayList) {
            if (recommendItem.getLink().equals(link)) {
                recommendItemArrayList.remove(recommendItem);
                break;
            }
        }
        model.updateRecommendItem(recommendItemArrayList);
    }

    @Override
    public RecommendItem getRecommendItem(String link) {
        List<RecommendItem> recommendItemList = listRecommendItem();
        for (RecommendItem recommendItem : recommendItemList) {
            if (recommendItem.getLink().equals(link)) {
                return recommendItem;
            }
        }
        return null;
    }

    @Override
    public void updateLatestRecommendItem() {
        viewWeakReference.get().runOnViewThread(new Runnable() {
            @Override
            public void run() {
                viewWeakReference.get().showUpdating();
            }
        });

        ThreadPoolExecutor singleThread = new ThreadPoolExecutor(1, 1, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "RecommendPresenter Single Thread For Update Recommend Items");
                    }
                });

        singleThread.execute(new Runnable() {
            @Override
            public void run() {
                List<RecommendItem> fullList = listRecommendItem();
                List<RecommendItem> onTimeList = removeOutOfDateRecommendItems(fullList);
                List<RecommendItem> newFullList = addLatestRecommendItems(onTimeList);
                List<RecommendItem> resultList = sortRecommendItems(newFullList);
                model.updateRecommendItem(resultList);
                notifyUpdate();
            }
        });
    }

    private List<RecommendItem> removeOutOfDateRecommendItems(List<RecommendItem> recommendItems) {
        if (recommendItems == null) {
            return null;
        }
        ArrayList<RecommendItem> recommendItemArrayList = new ArrayList<>();
        Date weekAgo = DateSelector.getStartTimeThisDay(new DateSelector().getDaysAfter(-7));
        for (RecommendItem item : recommendItems) {
            if (item.getUpdateDate().compareTo(weekAgo) >= 0) {
                recommendItemArrayList.add(item);
            } else {
                break;
            }
        }
        return recommendItemArrayList;
    }

    private List<RecommendItem> addLatestRecommendItems(List<RecommendItem> recommendItems) {
        Date startDate;
        if (recommendItems == null || recommendItems.size() == 0) {
            startDate = DateSelector.getStartTimeThisDay(new DateSelector().getDaysAfter(-7));
        } else {
            startDate = recommendItems.get(0).getUpdateDate();
        }

        // TODO

        return recommendItems;
    }

    private List<RecommendItem> sortRecommendItems(List<RecommendItem> recommendItems) {
        if (recommendItems == null) {
            return null;
        }
        ArrayList<RecommendItem> resultList = new ArrayList<>(recommendItems);
        Collections.sort(resultList, new Comparator<RecommendItem>() {
            @Override
            public int compare(RecommendItem o1, RecommendItem o2) {
                return -o1.getUpdateDate().compareTo(o2.getUpdateDate());
            }
        });
        return resultList;
    }

    @Override
    public void notifyUpdate() {
        viewWeakReference.get().runOnViewThread(new Runnable() {
            @Override
            public void run() {
                viewWeakReference.get().showStopUpdate();
                viewWeakReference.get().refreshRecommendItem();
            }
        });
    }
}
