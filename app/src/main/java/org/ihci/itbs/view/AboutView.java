package org.ihci.itbs.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.ihci.itbs.BuildConfig;
import org.ihci.itbs.R;
import org.ihci.itbs.util.StyleSelector;

/**
 * @author Yukino Yukinoshita
 */

public class AboutView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        TextView textViewAppName = findViewById(R.id.textViewAppName);
        TextView textViewAppVersion = findViewById(R.id.textViewAppVersion);
        TextView textViewAppOrg = findViewById(R.id.textViewAppOrg);

        textViewAppVersion.setText("版本号：" + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");

        textViewAppName.setTextColor(StyleSelector.getColorPrimary());
        textViewAppVersion.setTextColor(StyleSelector.getColorPrimary());
        textViewAppOrg.setTextColor(StyleSelector.getColorPrimary());
    }

    public void onClickBack(View view) {
        this.finish();
    }

}
