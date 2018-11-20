package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.User;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface AwardRepo {

    List<Award> listAllAward();

    Award getAward(String awardName);

    String getAwardType(String awardName);

    Currency getAwardValue(String awardName);

}
