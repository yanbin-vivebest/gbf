package com.vivebest.banking.core.aml.web.thread;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class FixedSizeThreadPool {
	//仓库
	private BlockingQueue<Runnable> taskQueue;
	
	//工作线程
	private List<Thread> workers;
	
	//线程池工作的标识
	private volatile boolean working = true;
	
	// poolSize固定大小
	// taskQueueSize 队列容量大小
	public FixedSizeThreadPool(int poolSize,int taskQueueSize) {
		if(poolSize <= 0 || taskQueueSize <= 0) {
			throw new IllegalArgumentException("参数异常!");
		}
		taskQueue = new LinkedBlockingDeque<>(taskQueueSize);
		//包装成线程安全
		this.workers = Collections.synchronizedList(new ArrayList<>());
		
		for(int i = 0;i<poolSize;i++) {
			Workers work = new Workers(this);
			work.start();
			this.workers.add(work);
		}
	}
	
	public boolean submit(Runnable task) {
		if(this.working) {
			return this.taskQueue.offer(task);
		}
		return false;
	}
	
	public void shutdown() {
		this.working = false;
		
		//把阻塞的线程中断
		for(Thread t: this.workers) {
			if(t.getState().equals(State.BLOCKED) || t.getState().equals(State.WAITING)  || t.getState().equals(State.TIMED_WAITING)) {
				t.interrupt();//中断
			}
		}
	}
	
	//提供别的可以阻塞、等待一段时间的方法 TODO
	
	private static class Workers extends Thread{
		private FixedSizeThreadPool pool;
		public Workers(FixedSizeThreadPool pool) {
			super();
			this.pool = pool;
		}
		@Override
		public void run() {
			int taskCount = 0;
			while(this.pool.working || pool.taskQueue.size() > 0) {
				Runnable task = null;
				try {
					if(this.pool.working) {
						task = pool.taskQueue.take();
					}else {
						task = pool.taskQueue.poll();
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//有可能会被中断，中断之后就是空，所以要判断一下是不是空
				if(task != null) {
					task.run();
					System.out.println(Thread.currentThread().getName() + "执行完了:" + (++taskCount) + "个任务!");
				}
			}
		}
	}
	
	public static void main(String[] args) {
		FixedSizeThreadPool pool = new FixedSizeThreadPool(3, 6);
		
		for(int i = 0;i<6;i++) {
			pool.submit(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("任务开始执行.....");
					try {
						Thread.sleep(2000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		pool.shutdown();
	}
	
}
