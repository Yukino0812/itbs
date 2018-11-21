package org.ihci.itbs.model.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yukino Yukinoshita
 */

public class HistoryUse implements Cloneable, Serializable {

    private Date date;
    private int duration;
    private Currency gainCurrency;
    private Award gainAward;

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

    public Award getGainAward() {
        return gainAward;
    }

    public void setGainAward(Award gainAward) {
        this.gainAward = gainAward;
    }

    @Override
    public HistoryUse clone() throws CloneNotSupportedException {
        return (HistoryUse) super.clone();
    }
}
