package com.gengqiquan.flow.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

public class Schedulers {
    static volatile ExecutorService io;
    static volatile ExecutorService calculate;
    static volatile ExecutorService scheduler;

    public static ExecutorService calculate() {
        if (calculate == null) {
            synchronized (Schedulers.class) {
                if (calculate == null) {
                    calculate = new ThreadPoolExecutor(cpuCount(), Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
                }
            }
        }
        return calculate;
    }

    public static ExecutorService scheduler() {
        if (scheduler == null) {
            synchronized (Schedulers.class) {
                if (scheduler == null) {
                    scheduler = new ThreadPoolExecutor(cpuCount() * 2, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
                }
            }
        }
        return scheduler;
    }

    public static ExecutorService io() {
        if (io == null) {
            synchronized (Schedulers.class) {
                if (io == null) {
                    io = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 30, TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>(), Util.threadFactory("Schedulers", false));

                }
            }
        }
        return io;
    }

    static int cpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }
}
