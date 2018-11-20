package org.ihci.itbs.model.repo;

import org.ihci.itbs.model.pojo.Toothbrush;

/**
 * @author Yukino Yukinoshita
 */

public interface ToothbrushRepo {

    Toothbrush getToothbrush(int toothbrushId);

    void updateToothbrush(Toothbrush toothbrush);

}
