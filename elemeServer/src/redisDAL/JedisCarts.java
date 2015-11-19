package redisDAL;

import redis.clients.jedis.Jedis;
import server.ConstValue;

public class JedisCarts {
	//Jedis redis = ConstValue.jedisPool.getResource();
	
	public Boolean isExistCart(final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
//		System.out.println("isExistCart");
		Boolean ret = redis.sismember("cartId", cartId);
		redis.close();
		return ret;
/*		
		Set<String> set = redis.smembers("cartId");   
		Iterator<String> iter = set.iterator() ;   
        while(iter.hasNext()){   
            if(cartId.equals(iter.next())) return true;
        }   
        
		return false;
*/
	}
	
	public Boolean isCartBelongToUser(final String userAccessToken, final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
//		System.out.println("isCartBelongToUser");
		Boolean ret = redis.sismember(userAccessToken, cartId);
		redis.close();
		return ret;
/*
		Set<String> cartSet = redis.smembers(userAccessToken);
		Iterator<String> iter = cartSet.iterator() ;   
        while(iter.hasNext()){   
            if(cartId.equals(iter.next())) return true;
        }   
		
		return false;
*/
		
	}
	
	public void deleteUserCartId(final String userAccessToken, final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
//		System.out.println("deleteUserCartId");
		redis.srem(userAccessToken, cartId);
		redis.close();
	}
	
	public void deleteCart(final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
//		System.out.println("deleteCart");
		redis.srem("Cart", cartId);
		redis.close();
	}
	
}
