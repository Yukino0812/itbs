package org.ihci.itbs.view;

import android.app.Activity;

import org.ihci.itbs.contract.AwardContract;

/**
 * @author Yukino Yukinoshita
 */

public class AwardView extends Activity implements AwardContract.View {

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

}
