package org.ihci.itbs.model.pojo;

import java.util.Date;

/**
 * @author Yukino Yukinoshita
 */

public class HistoryUse {

    private Date date;
    private int duration;
    private Currency gainCurrency;

    public HistoryUse() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Currency getGainCurrency() {
        return gainCurrency;
    }

    public void setGainCurrency(Currency gainCurrency) {
        this.gainCurrency = gainCurrency;
    }
}
