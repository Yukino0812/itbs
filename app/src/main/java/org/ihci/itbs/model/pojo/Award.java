package org.ihci.itbs.model.pojo;

/**
 * @author Yukino Yukinoshita
 */

public class Award {

    private String awardName;
    private String awardType;
    private Currency awardValue;

    public Award() {
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getAwardType() {
        return awardType;
    }

    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }

    public Currency getAwardValue() {
        return awardValue;
    }

    public void setAwardValue(Currency awardValue) {
        this.awardValue = awardValue;
    }
}
