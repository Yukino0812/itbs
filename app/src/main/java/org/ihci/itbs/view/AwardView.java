package org.ihci.itbs.view;

import android.app.Activity;

import org.ihci.itbs.contract.AwardContract;
import org.ihci.itbs.model.pojo.Award;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class AwardView extends Activity implements AwardContract.View {

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

    @Override
    public void showAwardList(List<Award> awards) {

    }
}
