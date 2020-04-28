package com.vivebest.banking.core.aml.web.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 启动两个线程，第一个线程从0一直加到10，第二个线程监视第一个线程,当值等于5的时候停止监视
 * @author Administrator
 *
 */
public class TestJSQ2 {

	public static int num = 0;
	
	public static void main(String[] args) {
		
		
		CountDownLatch latch = new CountDownLatch(1);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("T1 start");
				try {
					if(num != 5) {
						try {
							latch.await();
						} catch (InterruptedException e2) {
							e2.printStackTrace();
						}
					}
					System.out.println("已经监视到值变成5了！"); 
				} catch (Exception e) {
					
				}finally {
					
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("T2 start");
				try {
					for(int i = num;i<10;i++) {
						if(num == 5){
							latch.countDown();
							Thread.sleep(10);
						}
						
						System.out.println(num++);
					}
				} catch (Exception e) {
				}finally {
				}
				
			}
		}).start();
		
	}
}
