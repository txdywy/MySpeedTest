package com.num.myspeedtest.controller.helpers;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolHelper {
    private static ThreadPoolExecutor tpe;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static final int KEEP_ALIVE_TIME = 20;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    public ThreadPoolHelper() {
        tpe = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT, new LinkedBlockingQueue<Runnable>());
    }

    public void execute(Runnable task) {
        tpe.execute(task);
    }
}
