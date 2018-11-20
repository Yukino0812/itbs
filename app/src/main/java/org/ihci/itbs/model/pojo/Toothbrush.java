package org.ihci.itbs.model.pojo;

/**
 * @author Yukino Yukinoshita
 */

public class Toothbrush {

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
}
