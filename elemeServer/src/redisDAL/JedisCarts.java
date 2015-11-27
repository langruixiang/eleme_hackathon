package redisDAL;

import redis.clients.jedis.Jedis;
import server.ConstValue;

public class JedisCarts {
	
	public Boolean isExistCart(final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
//		System.out.println("isExistCart");
		Boolean flag = false;
		String ret = redis.get(cartId+"Unique");
		if(ret != null) {
			if (ret.equals("1")) flag = true;
		}
		redis.close();
		return flag;
	}
	
	public Boolean isCartBelongToUser(final String userAccessToken, final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
		
		int cnt = Integer.parseInt(redis.get(userAccessToken + "cartNum"));
		for(int i = 0; i < cnt; i ++) {
			String cartName = redis.get(userAccessToken + i);
			if(cartName != null && cartName.equals(cartId)) {
				redis.close();
				return true;
			}
		}
		
		redis.close();
		return false;
		
	}
	
	public void deleteUserCartId(final String userAccessToken, final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
		int cnt = Integer.parseInt(redis.get(userAccessToken + "cartNum")) ;
		for(int i = 0; i < cnt; i ++) {
			String cartName = redis.get(userAccessToken + i);
			if(cartName != null && cartName.equals(cartId)) {
				redis.del(userAccessToken + i);
				redis.close();
				return;
			}
		}
		redis.close();
	}
	
	
}
