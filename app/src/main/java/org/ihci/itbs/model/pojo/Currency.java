package org.ihci.itbs.model.pojo;

import java.io.Serializable;

/**
 * @author Yukino Yukinoshita
 */

public class Currency implements Cloneable, Serializable {

    private int seniorCurrency;
    private int juniorCurrency;

    public Currency() {
    }

    public int getSeniorCurrency() {
        return seniorCurrency;
    }

    public void setSeniorCurrency(int seniorCurrency) {
        this.seniorCurrency = seniorCurrency;
    }

    public int getJuniorCurrency() {
        return juniorCurrency;
    }

    public void setJuniorCurrency(int juniorCurrency) {
        this.juniorCurrency = juniorCurrency;
    }

    @Override
    public Currency clone() throws CloneNotSupportedException {
        return (Currency) super.clone();
    }
}
