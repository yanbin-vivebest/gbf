package com.vivebest.banking.core.aml.web.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


//弊端:线程阻塞 ,用户体验不太好,锁的颗粒度比较粗,没有根据id去加锁
//也适用很多场景
@Service
public class TicketService2 {
	
	@Autowired
	private JedisPool jedisPool; //TODO
	
	@Autowired
	private DataBaseService dbs;
	
	private Lock lock = new ReentrantLock();
	
	public String queryTicketStock(String ticketSeqNo) {
		
		Jedis jedis = jedisPool.getResource();
		
		String value = jedis.get(ticketSeqNo);
		
		if(value != null) {
			System.out.println("从缓存中取数据");
			return value;
		}
		
		lock.lock();//获取到了锁继续往下执行，没有获取到锁，则在该行代码等待，直到锁释放
		
		try {
			//1.再次查询
			value = jedis.get(ticketSeqNo);
			
			if(value != null) {
				System.out.println("从缓存中取数据"); //这里是为了前面没有获取到锁,等后面的锁释放后继续往下执行,所以需要再次查询
				return value;
			}
			
			//2.缓存中没有则从数据库中查询
			value = dbs.queryData();
			System.out.println("从库中获取数据:" + value);
			
			//3. 塞到主缓存
			jedis.setex(ticketSeqNo, 120, value);
			
		}finally {
			lock.unlock();//释放锁
		}
		
		return value;
	}
}
