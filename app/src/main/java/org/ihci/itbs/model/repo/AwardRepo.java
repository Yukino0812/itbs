package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.Award;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public interface AwardRepo {

    List<Award> listAllAward();

    Award getAward(String awardName);

}
