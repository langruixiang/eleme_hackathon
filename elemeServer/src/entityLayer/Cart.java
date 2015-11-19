package entityLayer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Food;
import redis.clients.jedis.Jedis;
import redisDAL.JedisCarts;
import redisDAL.JedisFood;
import redisDAL.RedisDAL;
import server.ConstValue;

public class Cart {	
	final static String SUCCESS = "1";
	// 篮子不存在
	final static String CART_NOT_FOUND = "2";
	// 篮子不属于当前用户
	final static String NOT_AUTHORIZED_TO_ACCESS_CART = "3";
	// 食物不存在
	final static String FOOD_NOT_FOUND = "4";
	// 篮子中食物数量超过三个
	final static String FOOD_OUT_OF_LIMIT = "5";
	// 篮子中的食物最大数
	final static int MAX_FOOD_SIZE = 3;
	
	//Jedis redis = ConstValue.jedisPool.getResource();
	JedisCarts jedisCarts = new JedisCarts();
	RedisDAL redisDAL = new RedisDAL();
	JedisFood jedisFood = new JedisFood();
	
	// 新建篮子
	public String createCart(final String accessToken) {
		Jedis redis = ConstValue.jedisPool.getResource();
//		System.out.println("createCart");
		
		String cartId = Tool.generateAccessToken();
		
		redis.sadd("cartId", cartId);
		redis.sadd(accessToken, cartId);
		redis.close();
		return cartId;
	}
	
	// 添加食物
	public String addFoodToCart(final String foodId, final String count, 
			final String userAccessToken, final String cartId) {
		Jedis redis = ConstValue.jedisPool.getResource();
//		System.out.println("addFoodToCart");
		
		if(!jedisCarts.isExistCart(cartId)) return CART_NOT_FOUND;
		if(!jedisCarts.isCartBelongToUser(userAccessToken, cartId)) 
			return NOT_AUTHORIZED_TO_ACCESS_CART;
		
//		System.out.println("----------");
	
		int idx = Integer.parseInt(foodId);
		if(redisDAL.GetFoodById(idx) == null)  return FOOD_NOT_FOUND;
		
		int cnt = Integer.parseInt(count);
		int curFoodNum = jedisFood.getFoodNumByCartId(cartId, String.valueOf(foodId));
		if(curFoodNum + cnt > MAX_FOOD_SIZE) return FOOD_OUT_OF_LIMIT;
	
		Map<String, String> map = new HashMap<String, String>();
		map.put(foodId,count);
		redis.hmset(cartId, map);
		redis.close();
		return SUCCESS;
	}
	
	public List<Food> getAllFoodList() {
		return redisDAL.GetAllFood();
	}
		
}
