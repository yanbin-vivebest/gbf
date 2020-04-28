package com.vivebest.banking.core.aml.web.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//E
public class TestPrint100_5 {
	
	private volatile static int a = 0;
	public static void main(String[] args) {
		ReentrantLock lock = new ReentrantLock();
		
		Condition condition = lock.newCondition();
		
		//打印偶数
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				while(a <= 100  && a % 2 == 0) {
					System.out.println("t1:" + a);
					a = a + 1;
					try {
						condition.await();
						condition.signal();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				lock.unlock();
			}
		},"t1");
		
		//打印奇数
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				while(a <= 100 && a % 2 == 1) {
					System.out.println("t2:" + a);
					a = a + 1;
					try {
						condition.signal();
						condition.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				lock.unlock();
			}
		},"t2");
		
		t1.start();
		t2.start();
	}
	
}
