package org.ihci.itbs.view;

import android.app.Activity;

import org.ihci.itbs.contract.GlobalSettingContract;

/**
 * @author Yukino Yukinoshita
 */

@Deprecated
public class GlobalSettingView extends Activity implements GlobalSettingContract.View {

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

}