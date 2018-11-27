package org.ihci.itbs.contract;

import org.ihci.itbs.BasePresenter;
import org.ihci.itbs.BaseView;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.Toothbrush;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface BrushContract {

    interface View extends BaseView {

        void showBrushList();

    }

    interface Presenter extends BasePresenter {

        List<Toothbrush> listToothbrush();

        void removeToothbrush(int toothbrushId);

        int connectBrush(int toothbrushId);

        Toothbrush getToothbrush(int toothbrushId);

        void startBrush();

        void stopBrush();

        Currency getCurrencyGainThisBrush();

        Award getAwardGainThisBrush();

    }

}
