package com.threadPool;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.log.Log;
/**
 * 线程池
 * 
 * @author fgq 20120815
 * 
 */
public class ThreadPool {
	private static ThreadPool theadpool = null;
	private ExecutorService cachedThreadPool;

	public static ConcurrentHashMap<String, Future<?>> MapFuture = new ConcurrentHashMap<String, Future<?>>();

	// 创建线程池
	public ThreadPool() {
		cachedThreadPool = Executors.newCachedThreadPool();
	}

	public static ThreadPool getPool() {
		if (theadpool == null)
			theadpool = new ThreadPool();
		return theadpool;
	}

	// 装载执行线程
	public void submit(String key, Callable<?> thread) {
		if (cachedThreadPool.isShutdown())
			return;
		if (cachedThreadPool.isTerminated())
			return;
		Future<?> future = cachedThreadPool.submit(thread);
		ThreadPool.MapFuture.put(key, future);
	}

	// 装载执行线程
	public void submit(String key, Runnable thread) {
		try {
			if (cachedThreadPool.isShutdown())
				return;
			if (cachedThreadPool.isTerminated())
				return;
			Future<?> future = cachedThreadPool.submit(thread);
			ThreadPool.MapFuture.put(key, future);
		} catch (Exception e) {
			Log.logError("线程池装载线程错误:", e);
		}
	}

	// 装载执行线程
	public Future<?> submit(Runnable thread) {
		return cachedThreadPool.submit(thread);
	}

	// 装载执行线程
	public void execute(Runnable thread) {
		cachedThreadPool.execute(thread);
	}

	// 关闭线程池
	public void shutDown() {
		cachedThreadPool.shutdown();
	}

	// 关闭线程池
	public List<Runnable> shutDownNow() {
		return cachedThreadPool.shutdownNow();
	}

	// public static void main(String[] args) throws Exception {
	// for (int i = 0; i < 1; i++) {
	// Callable<Integer> callable = new MyThread(i);
	// if (callable instanceof Runnable) {
	// System.out.println("1");
	// }
	//
	// System.out.println(callable.call());
	// // // Thread t = new Thread(future);
	// // Future<?> f= ThreadPool.getInstance().submit(callable);
	// //
	// // Thread.sleep(2000);
	// // while(true){
	// // if(f.isDone()){
	// // System.out.println(f.get());
	// // break;
	// // }
	// // }
	// //
	//
	// }
	// ThreadPool.getPool().shutDown();
	// }
	//
	// }
	//
	// // class MyThread extends Thread {
	// class MyThread implements Callable<Integer> {
	// int result;
	//
	// public MyThread(int result) {
	// this.result = result;
	// }
	//
	// public Integer call() throws Exception {
	// Thread.sleep(1000);
	// System.out.println("succe");
	//		return this.result;
	//	}

}