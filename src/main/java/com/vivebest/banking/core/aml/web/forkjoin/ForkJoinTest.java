package com.vivebest.banking.core.aml.web.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest extends RecursiveTask<Long> {

	private final static int EXCUTABLE_SIZE = 1000000;

	/**
	 * 要累加的数组
	 */
	private long[] arr;

	/**
	 * 第一个值
	 */
	private int start;

	/**
	 * 第二个值
	 */
	private int end;

	public ForkJoinTest(long[] arr, int start, int end) {
		super();
		this.arr = arr;
		this.start = start;
		this.end = end;
	}

	/**
	 * 局部统计
	 * 
	 * @return 局部统计的值
	 */
	private long subTotal() {
		long res = 0;
		for (int i = start; i < end; i++) {
			res = res + arr[i];
		}
		return res;
	}

	@Override
	protected Long compute() {
		if (end - start <= EXCUTABLE_SIZE) {
			return subTotal();
		} else {
			// 算出中间值
			int middle = (start + end) / 2;

			ForkJoinTest leftTask = new ForkJoinTest(arr, start, middle);
			ForkJoinTest rightTask = new ForkJoinTest(arr, middle, end);

			//左边任务
			leftTask.fork();
			
			//右边任务
			rightTask.fork();

			return leftTask.join() + rightTask.join();
		}
	}

	public static void main(String[] args) {

		int num = Runtime.getRuntime().availableProcessors();
		System.out.println(num);
		long time = System.currentTimeMillis();
		long[] arr = new long[10000000];
		for (int i = 0; i < 10000000; i++) {
			arr[i] = i + 1;
		}

		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTask<Long> result = pool.submit(new ForkJoinTest(arr, 0, arr.length));
		System.out.println("最终计算结果: " + result.invoke() + "耗时：" + (System.currentTimeMillis() - time) + "，毫秒");
		pool.shutdown();

	}

}
