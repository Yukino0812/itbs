package org.ihci.itbs.view;

import android.app.Activity;

import org.ihci.itbs.contract.UserContract;

/**
 * @author Yukino Yukinoshita
 */

public class UserView extends Activity implements UserContract.View {

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

}