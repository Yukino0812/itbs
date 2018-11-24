package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.HistoryUse;

import java.util.Date;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface CalendarContract {

    interface View extends BaseView {

        void refreshCalendar();

    }

    interface Presenter extends BasePresenter {

        Date getCalendarMiddleDate();

        void setCalendarMiddleDate(Date date);

        void addCalendarStartDate(int days);

        List<HistoryUse> listHistoryUse();

        List<HistoryUse> listHistoryUse(Date date);

        List<HistoryUse> listHistoryUse(Date from, Date to);

        int getCurrency(Date date);

        List<Award> listAward(Date date);

        Date getCalendarStartDate();

        Date getCalendarEndDate();

        String getCalendarDurationDescription();

    }

}
