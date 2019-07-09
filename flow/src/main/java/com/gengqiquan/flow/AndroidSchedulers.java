package com.gengqiquan.flow;

import android.os.Handler;
import android.os.Looper;

import com.gengqiquan.flow.interfaces.Scheduler;

import java.util.concurrent.atomic.AtomicReference;

 class AndroidSchedulers {
    /**
     * A {@link Scheduler} which executes actions on the Android UI thread.
     */
    public static Scheduler mainThread() {
        return getInstance().mainThreadScheduler;
    }

    private static final AtomicReference<AndroidSchedulers> INSTANCE = new AtomicReference<>();

    private final Scheduler mainThreadScheduler;

    private static AndroidSchedulers getInstance() {
        for (; ; ) {
            AndroidSchedulers current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new AndroidSchedulers();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    private AndroidSchedulers() {
        mainThreadScheduler = new MainThreadExecutor();
    }

    static class MainThreadExecutor implements Scheduler {
        private final Handler handler = new Handler(Looper.getMainLooper());

        public void schedule(Runnable r) {
            handler.post(r);
        }
    }
}