package org.ihci.itbs.view;

import android.app.Activity;

import org.ihci.itbs.contract.GoalContract;

/**
 * @author Yukino Yukinoshita
 */

public class GoalView extends Activity implements GoalContract.View {

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

}
