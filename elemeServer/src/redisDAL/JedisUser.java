package redisDAL;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import server.ConstValue;

public class JedisUser {
	RedisDAL radisDAL = new RedisDAL();
	
	public List<String> getUserList() {
		Jedis redis = ConstValue.jedisPool.getResource();
		
		List<String> userList = redis.lrange("orderUserU", 0, -1);   

        redis.close();
		return userList;
	}
	
		
}
