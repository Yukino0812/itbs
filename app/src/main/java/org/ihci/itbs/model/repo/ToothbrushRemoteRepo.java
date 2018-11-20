package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.Toothbrush;

/**
 * @author Yukino Yukinoshita
 */

public class ToothbrushRemoteRepo implements ToothbrushRepo {

    private static ToothbrushRemoteRepo INSTANCE = null;

    private ToothbrushRemoteRepo() {

    }

    public static ToothbrushRemoteRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ToothbrushRemoteRepo();
        }
        return INSTANCE;
    }

    @Override
    public Toothbrush getToothbrush(int toothbrushId) {
        return null;
    }

    @Override
    public void updateToothbrush(Toothbrush toothbrush) {

    }
}
