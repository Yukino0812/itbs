package org.ihci.itbs.presenter;

import org.ihci.itbs.contract.CalendarContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.UserModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.HistoryUse;
import org.ihci.itbs.model.pojo.Toothbrush;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.util.DateSelector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class CalendarPresenter implements CalendarContract.Presenter {

    private WeakReference<CalendarContract.View> viewWeakReference;
    private UserModel userModel;
    private Date calendarMiddleDate;

    public CalendarPresenter(CalendarContract.View view) {
        this.viewWeakReference = new WeakReference<>(view);
        this.userModel = new UserModel(this);
        calendarMiddleDate = new Date();
    }

    @Override
    public Date getCalendarMiddleDate() {
        return calendarMiddleDate;
    }

    @Override
    public void setCalendarMiddleDate(Date date) {
        calendarMiddleDate = date;
    }

    @Override
    public void addCalendarStartDate(int days) {
        DateSelector dateSelector = new DateSelector(calendarMiddleDate);
        setCalendarMiddleDate(dateSelector.getDaysAfter(days));
    }

    @Override
    public List<HistoryUse> listHistoryUse() {
        User user = userModel.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
        if(user==null){
            return null;
        }
        ArrayList<Toothbrush> toothbrushes = user.getToothbrushArrayList();
        if (toothbrushes == null) {
            return null;
        }
        ArrayList<HistoryUse> uses = new ArrayList<>();
        for (Toothbrush toothbrush : toothbrushes) {
            List<HistoryUse> historyUses = toothbrush.getHistoryUseArrayList();
            uses.addAll(historyUses);
        }
        Collections.sort(uses, new Comparator<HistoryUse>() {
            @Override
            public int compare(HistoryUse o1, HistoryUse o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return uses;
    }

    @Override
    public List<HistoryUse> listHistoryUse(Date date) {
        return listHistoryUse(date, date);
    }

    @Override
    public List<HistoryUse> listHistoryUse(Date from, Date to) {
        Date start = DateSelector.getStartTimeThisDay(from);
        Date end = DateSelector.getEndTimeThisDay(to);
        List<HistoryUse> uses = listHistoryUse();
        if (uses == null) {
            return null;
        }
        ArrayList<HistoryUse> useArrayList = new ArrayList<>();
        for (HistoryUse use : uses) {
            if (use.getDate().compareTo(start) >= 0 && use.getDate().compareTo(end) <= 0) {
                useArrayList.add(use);
            } else if (use.getDate().compareTo(end) > 0) {
                break;
            }
        }
        return useArrayList;
    }

    @Override
    public int getCurrency(Date date) {
        List<HistoryUse> uses = listHistoryUse(date);
        if (uses == null) {
            return 0;
        }
        int totalCurrency = 0;
        for (HistoryUse use : uses) {
            totalCurrency += use.getGainCurrency().getSeniorCurrency() * 3
                    + use.getGainCurrency().getJuniorCurrency();
        }
        return totalCurrency;
    }

    @Override
    public List<Award> listAward(Date date) {
        List<HistoryUse> uses = listHistoryUse(date);
        if (uses == null) {
            return null;
        }
        ArrayList<Award> awards = new ArrayList<>();
        for (HistoryUse use : uses) {
            awards.add(use.getGainAward());
        }
        return awards;
    }

    @Override
    public Date getCalendarStartDate() {
        DateSelector dateSelector = new DateSelector();
        dateSelector.setCurrentDate(DateSelector.getFirstDayCurrentWeek(calendarMiddleDate));
        return DateSelector.getStartTimeThisDay(dateSelector.getDaysAfter(-7));
    }

    @Override
    public Date getCalendarEndDate() {
        DateSelector dateSelector = new DateSelector();
        dateSelector.setCurrentDate(DateSelector.getFirstDayCurrentWeek(calendarMiddleDate));
        return DateSelector.getStartTimeThisDay(dateSelector.getDaysAfter(13));
    }

    @Override
    public String getCalendarDurationDescription() {
        String startString = DateSelector.dateToStringWithoutTime(getCalendarStartDate());
        String endString = DateSelector.dateToStringWithoutTime(getCalendarEndDate());
        return startString + "-" + endString;
    }

    @Override
    public void notifyUpdate() {
        viewWeakReference.get().runOnViewThread(new Runnable() {
            @Override
            public void run() {
                viewWeakReference.get().refreshCalendar();
            }
        });
    }
}
