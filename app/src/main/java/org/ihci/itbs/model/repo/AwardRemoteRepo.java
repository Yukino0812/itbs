package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.Award;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class AwardRemoteRepo implements AwardRepo {

    private static AwardRemoteRepo INSTANCE = null;

    private AwardRemoteRepo(){

    }

    public static AwardRemoteRepo getInstance(){
        if(INSTANCE==null){
            INSTANCE = new AwardRemoteRepo();
        }
        return INSTANCE;
    }

    @Override
    public List<Award> listAllAward() {
        return null;
    }

    @Override
    public Award getAward(String awardName) {
        return null;
    }
}
