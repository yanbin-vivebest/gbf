package com.vivebest.banking.core.aml.web.juc;

public class VolatileTest {

	public static void main(String[] args) {
		Obj obj = new Obj();
		obj.start();
		for(;;) {
			synchronized (obj) {  //第一种
				if(obj.isFlag()) {
					System.out.println("有点东西...");
				}
				//System.out.println("111"); //第三种
			}
		}
	}
}
class Obj extends Thread{
	//private volatile boolean flag = false; //第二种
	private boolean flag = false;
	public boolean isFlag() {
		return flag;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		flag = true;
		System.out.println("flag = " + flag);
	}
	
}