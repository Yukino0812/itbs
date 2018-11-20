package org.ihci.itbs.model.pojo;

import java.io.Serializable;

/**
 * @author Yukino Yukinoshita
 */

public class Toothbrush implements Cloneable, Serializable {

    private int toothbrushId;
    private HistoryUse historyUse;

    public Toothbrush() {
    }

    public int getToothbrushId() {
        return toothbrushId;
    }

    public void setToothbrushId(int toothbrushId) {
        this.toothbrushId = toothbrushId;
    }

    public HistoryUse getHistoryUse() {
        return historyUse;
    }

    public void setHistoryUse(HistoryUse historyUse) {
        this.historyUse = historyUse;
    }

    @Override
    public Toothbrush clone() throws CloneNotSupportedException {
        return (Toothbrush) super.clone();
    }
}
