package redisDAL;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Process.JsonAdminOrder;
import Process.JsonSubFood;
import Process.JsonUserOrder;
import entityLayer.Tool;
import model.Food;
import redis.clients.jedis.Jedis;
import server.ConstValue;

public class JedisOrder {
	//Jedis redis = ConstValue.jedisPool.getResource();
	JedisFood jedisFood = new JedisFood();
	JedisUser jedisUser = new JedisUser();
	RedisDAL redisDAL = new RedisDAL();
	
	public Boolean userHasPlaceOrder(final String userAccessToken) {
		Jedis redis = ConstValue.jedisPool.getResource();
		//System.out.println("userHasPlaceOrder");
		String userId = redis.get(userAccessToken + "uid");
		String orderId = redis.get(userId + "mp");
		if(orderId == null)
		{
			redis.close();
			return false;
		}
		return true;
	}
	
	static String userOrderScript = 
			"local userId = redis.call('get', KEYS[1]..KEYS[3]);\n"
			+ "redis.call('lpush', KEYS[4], userId);\n"
			+ "redis.call('set', userId..KEYS[5], KEYS[2]);\n"
			+ "redis.call('del', KEYS[6]..KEYS[7])";
	
	public void userOrderedSuccess(final String accessToken, 
			final String orderId, final String cartId, Map<String, String> foodMap) {
		Jedis redis = ConstValue.jedisPool.getResource();
		
		redis.eval(userOrderScript, 7, accessToken, orderId, "uid", "orderUserU", "mp", cartId, "Unique");
		redis.hmset(orderId, foodMap);
		redis.close();
	}
	
	// 返回用户订单 admin
	public List<JsonAdminOrder> queryOrderFoodListByAdmin() {	
		Jedis redis = ConstValue.jedisPool.getResource();
		List<JsonAdminOrder> ansList = new LinkedList<JsonAdminOrder>();
		
		List<String> userList = jedisUser.getUserList();
		
		for(String userId : userList) {
			String orderId = redis.get(userId + "mp");
			
			if(orderId == null) {
				continue;
			}
			
			List<JsonSubFood> lstFood = new LinkedList<JsonSubFood>();
			int total = 0;
			Map<String, String> res = redis.hgetAll(orderId);
			for(Map.Entry<String, String> entry : res.entrySet()) {
				JsonSubFood tmp = new JsonSubFood(entry.getKey() , entry.getValue());
				Food  food = redisDAL.GetFoodById(Integer.parseInt(entry.getKey() ));
				total += food.price * Integer.parseInt(entry.getValue());
				lstFood.add(tmp);
			}
			ansList.add(new JsonAdminOrder(orderId, userId, lstFood, String.valueOf(total)));
		}
		redis.close();
		return ansList;
	}
	
	public JsonUserOrder queryOrderFoodList(final String accessToken) {
		Jedis redis = ConstValue.jedisPool.getResource();
		List<JsonSubFood> lstFood = new LinkedList<JsonSubFood>();
		
		String userId = redis.get(accessToken + "uid");
		String orderId = redis.get(userId + "mp");
		
		if(orderId == null) {
			redis.close();
			return null;
		}
		
		int total = 0;
		Map<String, String> res = redis.hgetAll(orderId);
		for(Map.Entry<String, String> entry : res.entrySet()) {
			JsonSubFood tmp = new JsonSubFood(entry.getKey() , entry.getValue());
			Food  food = redisDAL.GetFoodById(Integer.parseInt(entry.getKey() ));
			total += food.price * Integer.parseInt(entry.getValue());
			lstFood.add(tmp);
		}
		redis.close();
		return new JsonUserOrder(orderId, lstFood, String.valueOf(total));
		
	}
}
