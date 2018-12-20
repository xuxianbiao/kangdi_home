package com.yd.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {

    private static ThreadPoolManager instance;
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    
    public ExecutorService getFixedThreadPool() {
		return fixedThreadPool;
	}

	private ThreadPoolManager() {
    }
    
    public static ThreadPoolManager getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolManager.class) {
                if (instance == null) {
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }
    
}