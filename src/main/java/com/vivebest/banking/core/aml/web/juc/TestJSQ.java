package com.vivebest.banking.core.aml.web.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 启动两个线程，第一个线程从0一直加到10，第二个线程监视第一个线程,当值等于5的时候停止监视
 * @author Administrator
 *
 */
public class TestJSQ {

	public static int num = 0;
	
	public static void main(String[] args) {
		
		
		
		Lock lock = new ReentrantLock();
		Condition  condition = lock.newCondition();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				lock.lock();
				try {
					if(num != 5) {
						try {
							condition.await();
						} catch (InterruptedException e2) {
							e2.printStackTrace();
						}
					}
					System.out.println("已经监视到值变成5了！"); 
					condition.signal();
				} catch (Exception e) {
					
				}finally {
					lock.unlock();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					for(int i = num;i<10;i++) {
						if(num == 5){
							condition.signal();
							condition.await();
						}
						System.out.println(num++);
					}
				} catch (Exception e) {
				}finally {
					lock.unlock();
				}
				
			}
		}).start();
		
	}
}
