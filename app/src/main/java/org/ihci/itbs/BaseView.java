package org.ihci.itbs;

/**
 * @author Yukino Yukinoshita
 */

public interface BaseView {

    /**
     * Presenter call this method to do something on Ui thread.
     *
     * @param action something need to do on ui thread.
     */
    void runOnViewThread(Runnable action);

}
