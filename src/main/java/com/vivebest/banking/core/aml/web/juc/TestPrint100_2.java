package com.vivebest.banking.core.aml.web.juc;

import java.util.concurrent.locks.ReentrantLock;

//E
public class TestPrint100_2 {

	
	private volatile static int a = 0;
	public static void main(String[] args) {
		ReentrantLock lock = new ReentrantLock();
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(a <= 100 && a % 2 == 0) {
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
		
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				while(a <= 100 && a % 2 == 1) {
					try {
						lock.lock();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("t2:" + a);
					a = a + 1;
					lock.unlock();
				}
			}
		},"t2");
		
		t1.start();
		t2.start();
	}
	
}
