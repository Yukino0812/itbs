package org.ihci.itbs.model.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Yukino Yukinoshita
 */

public class Toothbrush implements Cloneable, Serializable {

    private int toothbrushId;
    private ArrayList<HistoryUse> historyUseArrayList;

    public Toothbrush() {
    }

    public int getToothbrushId() {
        return toothbrushId;
    }

    public void setToothbrushId(int toothbrushId) {
        this.toothbrushId = toothbrushId;
    }

    public ArrayList<HistoryUse> getHistoryUseArrayList() {
        return historyUseArrayList;
    }

    public void setHistoryUseArrayList(ArrayList<HistoryUse> historyUseArrayList) {
        this.historyUseArrayList = historyUseArrayList;
    }

    @Override
    public Toothbrush clone() throws CloneNotSupportedException {
        return (Toothbrush) super.clone();
    }
}
