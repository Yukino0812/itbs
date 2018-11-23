package org.ihci.itbs.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.ihci.itbs.R;
import org.ihci.itbs.contract.CalendarContract;
import org.ihci.itbs.contract.UserContract;
import org.ihci.itbs.presenter.CalendarPresenter;
import org.ihci.itbs.presenter.UserPresenter;

/**
 * @author Yukino Yukinoshita
 */

public class OverviewView extends AppCompatActivity implements CalendarContract.View, UserContract.View {

    private CalendarContract.Presenter calendarPresenter;
    private UserContract.Presenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_view);
    }

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

    @Override
    public void refreshCalendar() {

    }

    private void initView() {
        calendarPresenter = new CalendarPresenter(this);
        userPresenter = new UserPresenter(this);
    }
}
