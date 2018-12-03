package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.Toothbrush;

import java.util.List;

/**
 * Contract defined the communication between {@code View} and {@code Presenter}.
 *
 * @author Yukino Yukinoshita
 */

public interface BrushContract {

    interface View extends BaseView {

        /**
         * Prompt {@code View} to refresh brush list
         */
        void showBrushList();

    }

    interface Presenter extends BasePresenter {

        /**
         * list all toothbrush
         *
         * @return toothbrush list
         */
        List<Toothbrush> listToothbrush();

        /**
         * get current connected toothbrush
         *
         * @return toothbrush
         */
        Toothbrush getCurrentToothbrush();

        /**
         * remove a toothbrush connected before
         *
         * @param toothbrushId id
         */
        void removeToothbrush(int toothbrushId);

        /**
         * connect a toothbrush
         *
         * @param toothbrushId id
         * @return id
         */
        int connectBrush(int toothbrushId);

        /**
         * get specified toothbrush
         *
         * @param toothbrushId id
         * @return toothbrush
         */
        Toothbrush getToothbrush(int toothbrushId);

        /**
         * mark start to brush teeth
         */
        void startBrush();

        /**
         * mark stop to brush
         */
        void stopBrush();

        /**
         * get currency gain in a brush event
         *
         * @return currency
         */
        Currency getCurrencyGainThisBrush();

        /**
         * get award gain in a brush event
         *
         * @return award
         */
        Award getAwardGainThisBrush();

    }

}
