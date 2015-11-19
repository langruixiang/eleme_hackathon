package redisDAL;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import server.ConstValue;

public class JedisUser {
	//Jedis redis = ConstValue.jedisPool.getResource();
	RedisDAL radisDAL = new RedisDAL();
	
	public Boolean IsExistUser(final String accessToken) {
		Jedis redis = ConstValue.jedisPool.getResource();
		//System.out.println("IsExistUser");
		Boolean ret =  redis.sismember("user", accessToken);
		redis.close();
		return ret;
		
/*
		Set<String> set = redis.smembers("user");   
		Iterator<String> iter = set.iterator() ;   
        while(iter.hasNext()){   
            if(accessToken.equals(iter.next())) return true;  
        }   
        
		return false;
*/
	}
	
	public List<String> getUserList() {
		Jedis redis = ConstValue.jedisPool.getResource();
		List<String> userList = new LinkedList<String>();
		Set<String> set = redis.smembers("user");   
		Iterator<String> iter = set.iterator() ;   
        while(iter.hasNext()){   
        	userList.add(iter.next());
        } 
        redis.close();
		return userList;
	}
	
		
}
