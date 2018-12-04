package org.ihci.itbs.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.taobao.sophix.SophixManager;

import java.lang.ref.WeakReference;

/**
 * @author Yukino Yukinoshita
 */

public class ItbsApplication extends Application {

    private static Context providerContext;
    private volatile static WeakReference<Context> currentActivity = null;
    private volatile static WeakReference<Context> contextWeakReference = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // Hotfix
        SophixManager.getInstance().queryAndLoadNewPatch();

        providerContext = getApplicationContext();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                currentActivity = new WeakReference<Context>(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                currentActivity = new WeakReference<Context>(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = new WeakReference<Context>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static Context getContext() {
        if (contextWeakReference == null || contextWeakReference.get() == null) {
            synchronized (org.ihci.itbs.view.ItbsApplication.class) {
                if (contextWeakReference == null || contextWeakReference.get() == null) {
                    Context context = getCurrentContext();
                    if (context != null) {
                        contextWeakReference = new WeakReference<>(context);
                    } else {
                        contextWeakReference = new WeakReference<>(providerContext);
                    }
                }
            }
        }
        return contextWeakReference.get();
    }

    private static Context getCurrentContext() {
        if (currentActivity == null || currentActivity.get() == null) {
            return null;
        } else {
            return currentActivity.get();
        }
    }

}

