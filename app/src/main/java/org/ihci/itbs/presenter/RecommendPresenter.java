package org.ihci.itbs.presenter;

import android.support.annotation.NonNull;

import org.ihci.itbs.contract.RecommendContract;
import org.ihci.itbs.model.RecommendModel;
import org.ihci.itbs.model.pojo.RecommendItem;
import org.ihci.itbs.util.DateSelector;
import org.jetbrains.annotations.Contract;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
                if (o1.getUpdateDate() == null || o2.getUpdateDate() == null) {
                    return 0;
                }
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

    @Contract("null -> null")
    private List<RecommendItem> removeOutOfDateRecommendItems(List<RecommendItem> recommendItems) {
        if (recommendItems == null) {
            return null;
        }
        ArrayList<RecommendItem> recommendItemArrayList = new ArrayList<>();
        Date weekAgo = DateSelector.getStartTimeThisDay(new DateSelector().getDaysAfter(-90));
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
        if (recommendItems == null) {
            return getRecommendItemsFromInternet(null);
        }

        ArrayList<String> filterTitleList = new ArrayList<>(recommendItems.size());
        for (RecommendItem item : recommendItems) {
            if (item == null || item.getTitle() == null) {
                continue;
            }
            filterTitleList.add(item.getTitle());
        }
        ArrayList<RecommendItem> resultItems = new ArrayList<>(recommendItems);
        List<RecommendItem> newItems = getRecommendItemsFromInternet(filterTitleList);
        if (newItems == null || newItems.size() == 0) {
            return resultItems;
        }
        resultItems.addAll(newItems);

        return resultItems;
    }

    @Contract("null -> null")
    private List<RecommendItem> sortRecommendItems(List<RecommendItem> recommendItems) {
        if (recommendItems == null) {
            return null;
        }
        ArrayList<RecommendItem> resultList = new ArrayList<>(recommendItems);
        Collections.sort(resultList, new Comparator<RecommendItem>() {
            @Override
            public int compare(RecommendItem o1, RecommendItem o2) {
                if (o1.getUpdateDate() == null || o2.getUpdateDate() == null) {
                    return 0;
                }
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

    /**
     * Source:
     *
     * @return recommend items from source
     */
    private List<RecommendItem> getRecommendItemsFromInternet(List<String> filterSameTitle) {
        final String source = "正保医学教育网";
        final String sourceUrl = "http://www.med66.com/kouqiangchangshi/";

        ArrayList<RecommendItem> result = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(sourceUrl).get();
            Elements elements = doc.select("divnewslist").select("li");

            for (Element element : elements) {
                RecommendItem item = new RecommendItem();

                Elements elementTime = element.select("span[class]");
                Elements elementTitle = element.select("a[title][href]");

                for (Element element1 : elementTitle) {
                    item.setTitle(element1.text());
                    item.setLink("http://www.med66.com" + element1.attr("href"));
                    item.setContent("");
                }

                if (filterSameTitle != null && filterSameTitle.contains(item.getTitle())) {
                    continue;
                }

                if (item.getTitle() == null || "".equals(item.getTitle())) {
                    continue;
                }

                for (Element element1 : elementTime) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    try {
                        item.setUpdateDate(simpleDateFormat.parse(element1.text()));
                    } catch (ParseException e) {
                        item.setUpdateDate(new Date());
                    }
                }

                if (item.getUpdateDate() == null) {
                    continue;
                }

                item.setSource(source);

                result.add(item);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
