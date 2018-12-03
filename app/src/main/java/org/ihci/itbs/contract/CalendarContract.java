package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.HistoryUse;

import java.util.Date;
import java.util.List;

/**
 * Contract defined the communication between {@code View} and {@code Presenter}.
 *
 * @author Yukino Yukinoshita
 */

public interface CalendarContract {

    interface View extends BaseView {

        /**
         * Prompt {@code View} to refresh calendar
         */
        void refreshCalendar();

    }

    interface Presenter extends BasePresenter {

        /**
         * get the middle date of calendar
         * <p>
         * Middle date is placed on the middle row of 3-row calendar, and it is
         * often today. When middle date is today, the first row of calendar is
         * last week, and the last row is next week.
         *
         * @return date
         */
        Date getCalendarMiddleDate();

        /**
         * set the middle date of calendar
         *
         * @param date date
         * @see #getCalendarMiddleDate()
         */
        void setCalendarMiddleDate(Date date);

        /**
         * change the start date of calendar to change the range of date
         * calendar show.
         *
         * @param days date difference
         */
        void addCalendarStartDate(int days);

        /**
         * list all history use of user
         *
         * @return history use list
         */
        List<HistoryUse> listHistoryUse();

        /**
         * list history user in a day
         *
         * @param date date
         * @return history user in a day
         */
        List<HistoryUse> listHistoryUse(Date date);

        /**
         * list history user from a range
         *
         * @param from start day
         * @param to end day
         * @return history user in a range
         */
        List<HistoryUse> listHistoryUse(Date from, Date to);

        /**
         * count currency get in a day
         *
         * @param date date
         * @return junior currency number, and senior currency is transferred
         * to junior.
         */
        int getCurrency(Date date);

        /**
         * list awards get in a day
         *
         * @param date date
         * @return awards
         */
        List<Award> listAward(Date date);

        /**
         * get calendar start date
         *
         * @return start date
         */
        Date getCalendarStartDate();

        /**
         * get calendar end date
         *
         * @return end date
         */
        Date getCalendarEndDate();

        /**
         * get the description string about calendar duration, which is from
         * start date to end date
         *
         * @return duration string
         */
        String getCalendarDurationDescription();

    }

}
