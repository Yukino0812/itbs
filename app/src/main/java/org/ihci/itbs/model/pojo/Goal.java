package org.ihci.itbs.model.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yukino Yukinoshita
 */

public class Goal implements Cloneable, Serializable {

    private int goalId;
    private String content;
    private Award award;
    private Date startDate;

    public Goal() {
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public Goal clone() throws CloneNotSupportedException {
        return (Goal) super.clone();
    }
}
