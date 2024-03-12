package com.peng.ppscale.business.ble.threadpool;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接池管理类
 *
 * @author zqz
 */
public class BleThreadPool extends ThreadPoolExecutor {

    public static final AtomicBoolean lock = new AtomicBoolean();

    public static final Map<String, ThreadPoolExecutor> poolManager = new ConcurrentHashMap<String, ThreadPoolExecutor>();
    public static final ThreadFactory defaultThreadFactor = new DefaultThreadFactory();

    /**
     * 获取连接池实例
     *
     * @param poolName        自定义连接池名称前缀 ，更好的区分不同的连接池；
     * @param corePoolSize    核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   空闲线程存活时间，单位是秒
     * @param workQueue       任务队列
     * @return
     */
    public static ThreadPoolExecutor getInstance(String poolName, int corePoolSize, int maximumPoolSize,
                                                 long keepAliveTime, BlockingQueue<Runnable> workQueue) {
        while (!lock.compareAndSet(false, true))
            ;
        ThreadPoolExecutor pool = poolManager.get(poolName);
        try {
            if (pool == null) {
                pool = new BleThreadPool(poolName, corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                        workQueue, defaultThreadFactor);
                poolManager.put(poolName, pool);
                return pool;
            } else {
                return poolManager.get(poolName);
            }
        } finally {
            lock.compareAndSet(true, false);
        }
    }

    /**
     * 私有构造方法
     *
     * @param poolName
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     */
    private BleThreadPool(String poolName, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                          BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        if (!poolManager.containsKey(poolName)) {
            poolManager.put(poolName, this);
        }
    }

    /**
     * 返回指定线程池状态
     *
     * @param name
     * @return
     */
    public static PoolInfo monitor(String name) {
        ThreadPoolExecutor pool = poolManager.get(name);
        if (pool == null)
            return null;
        PoolInfo poolInfo = new PoolInfo();

        // 当前线程池中的工作线程数；
        poolInfo.setPoolSize(pool.getPoolSize());
        // 队列中的任务；
        poolInfo.setQueueSize(pool.getQueue().size());
        // 线程正在执行的任务;
        poolInfo.setActiveCount(pool.getActiveCount());
        // 已经执行完成的任务；
        poolInfo.setCompletedTaskCount(pool.getCompletedTaskCount());
        // 是否允许coreThread超时；
        poolInfo.setAllowsCoreThreadTimeOut(pool.allowsCoreThreadTimeOut());
        // 核心线程数；
        poolInfo.setCorePoolSize(pool.getCorePoolSize());
        // 最大线程数；
        poolInfo.setMaximumPoolSize(pool.getMaximumPoolSize());
        // 线程池中曾经最大的线程数量；
        poolInfo.setLargestPoolSize(pool.getLargestPoolSize());
        // 线程超时时间
        poolInfo.setKeepAliveTime(pool.getKeepAliveTime(TimeUnit.SECONDS));
        // 全部的任务数，队列任务+正在执行+已经执行完成
        pool.getTaskCount();
        return poolInfo;
    }

    /**
     * The default thread factory
     */
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private int stackSize = 0;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "zqz-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), stackSize);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        String poolName = "test";

        ThreadPoolExecutor pool = BleThreadPool.getInstance(poolName, 1, 10, 0, new LinkedBlockingQueue<Runnable>(10));

        pool.execute(new Runnable() {

            public void run() {
                System.out.println("test");
            }

        });
        PoolInfo poolInfo = BleThreadPool.monitor(poolName);
        System.out.println(poolInfo.getPoolSize());
    }
}
