package org.ihci.itbs.view;

import android.app.Activity;

import org.ihci.itbs.contract.BrushContract;

/**
 * @author Yukino Yukinoshita
 */

public class BrushView extends Activity implements BrushContract.View {

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

}
