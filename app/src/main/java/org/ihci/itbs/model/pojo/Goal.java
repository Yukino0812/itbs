package org.ihci.itbs.model.pojo;

import java.util.Date;

/**
 * @author Yukino Yukinoshita
 */

public class Goal {

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
}
