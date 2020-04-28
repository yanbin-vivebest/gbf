package com.vivebest.banking.core.aml.web.juc;

import java.util.concurrent.locks.ReentrantLock;

//E
public class TestPrint100_1 {

	private volatile static int a = 0;
	public static void main(String[] args) {
		ReentrantLock lock = new ReentrantLock();
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(a <= 100) {
					try {
						lock.lock();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("t1:" + a);
					a = a + 1;
					lock.unlock();
				}
			}
		},"t1");
		
		t1.start();
	}
	
}
