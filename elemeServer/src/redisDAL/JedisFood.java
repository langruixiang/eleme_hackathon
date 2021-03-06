package redisDAL;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import server.ConstValue;

public class JedisFood {
	//Jedis redis = ConstValue.jedisPool.getResource();
	
	public int getFoodNumByCartId(final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
		//System.out.println("getFoodNumByCartId");

		Map<String, String> res = redis.hgetAll(cartId);
		redis.close();
		int count = 0;
		for(Map.Entry<String, String> entry : res.entrySet()) {
			count += Integer.parseInt((String) entry.getValue());
		}
		return count;
	}
	
	public int getFoodNumByFoodIdInCartId(final String cartId, final String foodId) {
		//System.out.println("getFoodNumByCartId");
		Jedis redis = ConstValue.jedisPool.getResource();
		int ret = 0;
		if(redis.hexists(cartId, foodId)) {
			ret = Integer.parseInt(redis.hget(cartId,  foodId));
		}
		redis.close();
		return ret;
	}
	
	public List<String> getCartFoodList(final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
		//System.out.println("getCartFoodList");
		Map<String, String> userCartMap = redis.hgetAll(cartId);
		redis.close();
		List<String> list = new LinkedList<String>();
		for(Map.Entry<String, String> entry : userCartMap.entrySet()) {
			list.add(entry.getKey());
		}
		return list;
	}
}
