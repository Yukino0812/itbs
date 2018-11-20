package org.ihci.itbs.model.pojo;

/**
 * @author Yukino Yukinoshita
 */

public class Currency {

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
}
