package com.vivebest.banking.core.aml.web.juc;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

	public static void main(String[] args) {
		AtomicInteger ai = new AtomicInteger(1);
		int res = ai.incrementAndGet();
		System.out.println(res);
		
		AtomicInteger ai2 = new AtomicInteger(4);
		int count = ai2.getAndIncrement();
		System.out.println(count);
	}
}
