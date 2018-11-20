package org.ihci.itbs.model.pojo;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Yukino Yukinoshita
 */

public class User implements Cloneable, Serializable {

    private String userName;
    private String userPassword;
    private Bitmap avatar;
    private Date lastUpdate;

    private ArrayList<Award> awardArrayList;
    private Goal goal;
    private ArrayList<Toothbrush> toothbrushArrayList;
    private Currency currency;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ArrayList<Award> getAwardArrayList() {
        return awardArrayList;
    }

    public void setAwardArrayList(ArrayList<Award> awardArrayList) {
        this.awardArrayList = awardArrayList;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public ArrayList<Toothbrush> getToothbrushArrayList() {
        return toothbrushArrayList;
    }

    public void setToothbrushArrayList(ArrayList<Toothbrush> toothbrushArrayList) {
        this.toothbrushArrayList = toothbrushArrayList;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }
}
