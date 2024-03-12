package com.peng.ppscale.business.ble.threadpool;

public class PoolInfo {

    int poolSize;
    int queueSize;
    int activeCount;
    long completedTaskCount;
    boolean allowsCoreThreadTimeOut;
    int corePoolSize;
    int maximumPoolSize;
    int largestPoolSize;
    long keepAliveTime;

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public boolean isAllowsCoreThreadTimeOut() {
        return allowsCoreThreadTimeOut;
    }

    public void setAllowsCoreThreadTimeOut(boolean allowsCoreThreadTimeOut) {
        this.allowsCoreThreadTimeOut = allowsCoreThreadTimeOut;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getLargestPoolSize() {
        return largestPoolSize;
    }

    public void setLargestPoolSize(int largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
