package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class AwardRemoteRepo implements AwardRepo {

    private static AwardRemoteRepo INSTANCE = null;
    private List<Award> awardList;

    private AwardRemoteRepo() {

    }

    public static AwardRemoteRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AwardRemoteRepo();
        }
        return INSTANCE;
    }

    @Override
    public List<Award> listAllAward() {
        if (awardList != null) {
            return awardList;
        }

        ArrayList<Award> awards = new ArrayList<>();
        awards.add(getNewAward("duck", "little", 0, 2));
        awards.add(getNewAward("car", "little", 1, 0));
        awards.add(getNewAward("block", "little", 0, 1));
        awards.add(getNewAward("rabbit", "little", 1, 0));
        awards.add(getNewAward("ball", "medium", 1, 0));
        awards.add(getNewAward("windmill", "medium", 1, 1));
        awards.add(getNewAward("transformer", "medium", 2, 2));
        awards.add(getNewAward("hello_kitty", "medium", 2, 0));
        awards.add(getNewAward("yellow_duck", "big", 9999, 0));
        awards.add(getNewAward("bear", "big", 3, 0));
        awards.add(getNewAward("superman", "big", 4, 0));
        awards.add(getNewAward("doraemon", "big", 3, 2));

        awardList = awards;

        return awards;
    }

    @Override
    public Award getAward(String awardName) {
        if (awardList == null) {
            listAllAward();
        }
        for (Award award : awardList) {
            if (award.getAwardName().equals(awardName)) {
                return award;
            }
        }
        return null;
    }

    private Award getNewAward(String awardName, String awardType, int seniorValue, int juniorValue) {
        Currency value = new Currency();
        value.setJuniorCurrency(juniorValue);
        value.setSeniorCurrency(seniorValue);
        Award award = new Award();
        award.setAwardName(awardName);
        award.setAwardType(awardType);
        award.setAwardValue(value);
        return award;
    }
}
