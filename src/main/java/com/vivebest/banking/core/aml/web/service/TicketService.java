package com.vivebest.banking.core.aml.web.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


//主缓存 + 备份缓存 + 细粒度锁 +  缓存降级 
//拿到锁的线程去更新缓存,未拿到的则采用降级 或者 从备份缓从里面获取。
//备份缓存一般是持久化的,后台异步更新数据
@Service
public class TicketService {
	
	@Autowired
	private JedisPool jedisPool; //TODO
	
	@Autowired
	private JedisPool bakJedisPool; //TODO
	
	
	@Autowired
	private DataBaseService dbs;
	
	ConcurrentHashMap<String , String> mapLock = new ConcurrentHashMap<String , String>();
	
	public String queryTicketStock(String ticketSeqNo) {
		
		Jedis jedis = jedisPool.getResource();
		
		String value = jedis.get(ticketSeqNo);
		
		if(value != null) {
			System.out.println("从缓存中取数据");
			return value;
		}
		
		boolean lock = false;
		try {
			lock = mapLock.putIfAbsent(ticketSeqNo, ticketSeqNo+"AAA") == null;
			//获取到了锁
			if(lock) {
				//1.再次查询
				value = jedis.get(ticketSeqNo);
				
				if(value != null) {
					System.out.println("从缓存中取数据");
					return value;
				}
				
				//2.缓存中没有则从数据库中查询
				value = dbs.queryData();
				System.out.println("从库中获取数据:" + value);
				
				//3. 塞到主缓存
				jedis.setex(ticketSeqNo, 120, value);
				
				//4.塞到备份缓存 ,不要设置过期时间
				Jedis bakJedis = bakJedisPool.getResource();
				bakJedis.set(ticketSeqNo,value);
			}else {
				//缓存降级
				Jedis bakJedis = bakJedisPool.getResource();
				value = bakJedis.get(ticketSeqNo);
				if(value != null) {
					System.out.println("使用备份缓存" + value);
				}else {
					value = "系统繁忙请重试"; //返回固定数据
				}
				
			}
		}finally {
			mapLock.remove(ticketSeqNo); //释放锁
		}
		
		return value;
	}
}
