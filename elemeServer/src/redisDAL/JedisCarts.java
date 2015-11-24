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
	}
	
	public Boolean isCartBelongToUser(final String userAccessToken, final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
//		System.out.println("isCartBelongToUser");
		Boolean ret = redis.sismember(userAccessToken, cartId);
		redis.close();
		return ret;
		
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
		redis.srem("cartId", cartId);
		redis.close();
	}
	
}
