package com.example.smartshopper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class AppApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private HandlerThread ioThread;
    private Handler ioHandler;
    private Handler mainHandler;

    private Runnable stopIoThread = new Runnable() {
        @Override
        public void run() {
            if (ioThread != null) {
                ioThread.quitSafely();
                ioThread = null;
                ioHandler = null;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        this.registerActivityLifecycleCallbacks(this);

        mainHandler = new Handler(Looper.getMainLooper());
        startIoThread();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // do nothing
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // do nothing
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mainHandler.removeCallbacks(stopIoThread);
        startIoThread();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mainHandler.postDelayed(stopIoThread, 1000);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // do nothing
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // do nothing
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // do nothing
    }

    public Handler getIoHandler() {
        startIoThread();
        return  ioHandler;
    }

    public Handler getMainHandler() {
        return mainHandler;
    }

    private void startIoThread() {
        if (ioThread == null) {
            ioThread = new HandlerThread("ioThread", HandlerThread.MAX_PRIORITY);
            ioThread.start();
            ioHandler = new Handler(ioThread.getLooper());
        }
    }
}
