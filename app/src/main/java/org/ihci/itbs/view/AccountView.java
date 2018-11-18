package org.ihci.itbs.view;

import android.app.Activity;

import org.ihci.itbs.contract.AccountContract;

/**
 * @author Yukino Yukinoshita
 */

public class AccountView extends Activity implements AccountContract.View {

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

}
