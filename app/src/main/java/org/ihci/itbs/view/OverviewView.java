package org.ihci.itbs.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.ihci.itbs.R;
import org.ihci.itbs.contract.CalendarContract;
import org.ihci.itbs.contract.UserContract;

/**
 * @author Yukino Yukinoshita
 */

public class OverviewView extends AppCompatActivity implements CalendarContract.View, UserContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_view);
    }

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }
}
