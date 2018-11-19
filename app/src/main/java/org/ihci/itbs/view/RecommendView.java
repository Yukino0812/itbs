package org.ihci.itbs.view;

import android.app.Activity;

import org.ihci.itbs.contract.RecommendContract;

/**
 * @author Yukino Yukinoshita
 */

public class RecommendView extends Activity implements RecommendContract.View {

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

}
