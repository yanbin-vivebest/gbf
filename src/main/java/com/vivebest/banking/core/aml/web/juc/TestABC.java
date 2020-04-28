package com.vivebest.banking.core.aml.web.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 启动三个线程，名字分别为ABC 打印自己线程名字，并且是ABCABCABC顺序循环打印10次。
 * 
 * @author Administrator
 *
 */
public class TestABC {

	public static  int num = 1;
	
	public static void main(String[] args) {
	
		Lock lock = new ReentrantLock();
		Condition condition1 = lock.newCondition();
		Condition condition2 = lock.newCondition();
		Condition condition3 = lock.newCondition();
		
		Thread a = new Thread(new Runnable() {

			@Override
			public void run() {
				for(int i = 0;i < 10; i++) {
					lock.lock();
					try {
						if(num != 1) {
							try {
								condition1.await();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						System.out.println(Thread.currentThread().getName());
						num = 2;
						condition2.signal();
					} catch (Exception e) {
					}finally {
						lock.unlock();
					}
					
				}
			}
		}, "A");
		

		Thread b = new Thread(new Runnable() {

			@Override
			public void run() {
				for(int i = 0;i < 10; i++) {
					lock.lock();
					try {
						if(num != 2) {
							try {
								condition2.await();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						System.out.println(Thread.currentThread().getName());
						num = 3;
						condition3.signal();
					} catch (Exception e) {
					}finally {
						lock.unlock();
					}
					
				}
			}
		}, "B");
		

		Thread c = new Thread(new Runnable() {

			@Override
			public void run() {
				for(int i = 0;i < 10; i++) {
					lock.lock();
					try {
						if(num != 3) {
							try {
								condition3.await();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						System.out.println(Thread.currentThread().getName());
						num = 1;
						condition1.signal();
					} catch (Exception e) {
					}finally {
						lock.unlock();
					}
					
				}
			}
		}, "C");
		
		a.start();
		b.start();
		c.start();

	}
}
