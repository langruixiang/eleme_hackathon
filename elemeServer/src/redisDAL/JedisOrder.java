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
		String userId = redis.get(userAccessToken + "uid");
		Boolean ret = redis.sismember("orderUserU", userId);
		redis.close();
		return ret;
	}
	
	public void userOrderedSuccess(final String accessToken, 
			final String orderId, final String cartId, Map<String, String> foodMap) {
		Jedis redis = ConstValue.jedisPool.getResource();
		// get user id
		String userId = redis.get(accessToken + "uid");
		// 用户已下单list
		redis.sadd("orderUserU", userId);
		// 用户下单对应orderId
		redis.sadd(userId + "mp", orderId);
		// 该单对应的食品清单
		redis.hmset(orderId, foodMap);
		redis.close();
	}
	
	// 返回用户订单 admin
	public List<JsonAdminOrder> queryOrderFoodListByAdmin(final String accessToken1) {	
		Jedis redis = ConstValue.jedisPool.getResource();
		List<JsonAdminOrder> ansList = new LinkedList<JsonAdminOrder>();
		
		List<String> userList = jedisUser.getUserList();
		
		for(String userId : userList) {
			Set<String> userOrderedSet = redis.smembers(userId + "mp");
			Iterator<String> iter = userOrderedSet.iterator();
			
			while(iter.hasNext()) {
				List<JsonSubFood> lstFood = new LinkedList<JsonSubFood>();
				String orderId = iter.next();
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
		}
		redis.close();
		
		return ansList;
	}
	
	public JsonUserOrder queryOrderFoodList(final String accessToken) {
		Jedis redis = ConstValue.jedisPool.getResource();
		List<JsonSubFood> lstFood = new LinkedList<JsonSubFood>();
		
		String userId = redis.get(accessToken + "uid");
		Set<String> userOrderedSet = redis.smembers(userId + "mp");
		Iterator<String> iter = userOrderedSet.iterator();
		
		if(iter.hasNext()) {
			String orderId = iter.next();
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
		redis.close();
		//System.out.println("queryOrderFoodList");
		return null;
	}
	
}
