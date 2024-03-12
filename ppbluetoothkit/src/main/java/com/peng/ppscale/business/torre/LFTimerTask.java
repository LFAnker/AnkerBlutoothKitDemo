package com.peng.ppscale.business.torre;

import android.os.Looper;
import android.util.Log;

import com.peng.ppscale.util.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class LFTimerTask {

    private static volatile LFTimerTask instance = null;

    private static String TAG = LFTimerTask.class.getSimpleName();

    private final static long DELAY_TIME = 10000;
    private TimerTask timerTask;
    private Timer timer;
    RunCallBack runCallBack;
    boolean isRun = false;

    private LFTimerTask() {
        initTimer();
    }

    private void initTimer() {
        Log.d(TAG, "initTimer");
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (runCallBack != null) {
                    runCallBack.run();
                }
            }
        };
        timer = new Timer();
    }

    public static LFTimerTask getInstance() {
        if (instance == null) {
            synchronized (LFTimerTask.class) {
                if (instance == null) {
                    instance = new LFTimerTask();
                }
            }
        }
        return instance;
    }

    public void startDelay() {
//        if (Looper.myLooper() != null) {
        Log.d(TAG, "startDelay");
        if (timerTask == null || timer == null) {
            initTimer();
        }
        if (timerTask != null && timer != null) {
            isRun = true;
            Log.d(TAG, "startDelay isRun:" + isRun);
            try {
                timer.schedule(timerTask, DELAY_TIME, DELAY_TIME);
            } catch (Exception e) {
                Log.e(TAG, "startDelay e:" + e.getMessage());
            }
        } else {
            Log.e(TAG, "startDelay timerTask or timer is null");
        }
//        } else {
//            Log.e(TAG, "startDelay Looper.myLooper() is null");
//        }
    }

    public void stopDelay() {
        isRun = false;
        if (Looper.myLooper() != null) {
            if (timerTask != null && timer != null) {
                Log.d(TAG, "stopDelay isRun:" + isRun);
                timer.cancel();
                timerTask.cancel();
                timer.purge();
                timer = null;
                timerTask = null;
            } else {
                Log.e(TAG, "stopDelay timerTask or timer is null");
            }
        } else {
            Log.e(TAG, "stopDelay Looper.myLooper() is null");
        }
    }

    public interface RunCallBack {
        void run();
    }

    public void setRunCallBack(RunCallBack runCallBack) {
        this.runCallBack = runCallBack;
    }
}
