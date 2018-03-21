package com.threadPool;

import java.util.concurrent.Future;

//线程Bean
public class ThreadBean {
	// 线程开始时间
	private long startTime;
	// 线程未执行次数
	private int unFinishedCount;
	// 线程状态
	private boolean finished;
	// 线程信息
	private Future<?> future;

	public ThreadBean() {
		startTime = 0l;
		unFinishedCount = 0;
		finished = true;
		future = null;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getUnFinishedCount() {
		return unFinishedCount;
	}

	public void setUnFinishedCount(int unFinishedCount) {
		this.unFinishedCount = unFinishedCount;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}
}
