package entityLayer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Process.JsonAdminOrder;
import Process.JsonUserOrder;
import model.ConsumeFood;
import model.Food;
import redis.clients.jedis.Jedis;
import redisDAL.JedisCarts;
import redisDAL.JedisFood;
import redisDAL.JedisOrder;
import redisDAL.JedisUser;
import redisDAL.RedisDAL;
import server.ConstValue;

public class Order {
	// 篮子不存在
	final static String CART_NOT_FOUND = "1";
	// 无权访问篮子
	final static String NOT_AUTHORIZED_TO_ACCESS_CART = "2";
	// 食物库存不足
	final static String FOOD_OUT_OF_STOCK = "3";
	// 每个用户只能下一单
	final static String ORDER_OUT_OF_LIMIT = "4";
	
	JedisCarts jedisCarts = new JedisCarts();
	JedisFood  jedisFood = new JedisFood();
	JedisOrder jedisOrder = new JedisOrder();
	JedisUser  jedisUser = new JedisUser();
	RedisDAL   jedisDAL = new RedisDAL();
	
	//Jedis redis = ConstValue.jedisPool.getResource();
	
	
	// 下单成功，返回orderId
	public String placeOrder(final String userAccessToken, final String cartId)
	{
//		System.out.println("placeOrder");
		//edis redis = ConstValue.jedisPool.getResource();
		//System.out.println(redis.smembers("user"));
		//System.out.println("hello world");
		//System.out.println(redis.smembers("cartId"));
		//System.out.println(redis.sismember("cartId",cartId));
		if(jedisOrder.userHasPlaceOrder(userAccessToken)) return ORDER_OUT_OF_LIMIT;
		// 篮子不存在
		if(!jedisCarts.isExistCart(cartId)) return CART_NOT_FOUND;
		// 无权访问篮子
		if(!jedisCarts.isCartBelongToUser(userAccessToken, cartId)) 
			return NOT_AUTHORIZED_TO_ACCESS_CART;
		
		// 用于存储本篮子食物
		Map<String, String> foodMap = new HashMap<String, String>();
		
		// 判断是否超出食物库存
		List<String> foodIdList = jedisFood.getCartFoodList(cartId);
		for(String foodId : foodIdList) {
			int curCnt = jedisFood.getFoodNumByFoodIdInCartId(cartId, foodId);
			foodMap.put(foodId, Integer.toString(curCnt));
			Food food = jedisDAL.GetFoodById(Integer.parseInt(foodId));
			if(curCnt > food.stock) return FOOD_OUT_OF_STOCK;
		}
		//System.out.println(foodIdList.size());
		// 每个用户只能下一单
		//if(jedisOrder.userHasPlaceOrder(userAccessToken)) return ORDER_OUT_OF_LIMIT;
		
		// 下单成功，生成订单编号
		String orderId = Tool.generateAccessToken();//jedisOrder.orderId();
		
		// 用户下单成功，将用户写入已下单列表
		jedisOrder.userOrderedSuccess(userAccessToken, orderId, cartId, foodMap);
		// 删除篮子
		jedisCarts.deleteCart(cartId);
		// 删除用户篮子列表中的篮子
		jedisCarts.deleteUserCartId(userAccessToken, cartId);
		
		// 更新数据库
		List<ConsumeFood> cfList = new LinkedList<>();
		for(String foodId : foodIdList) {
			ConsumeFood cf = new ConsumeFood();
			cf.cosumeCount = jedisFood.getFoodNumByCartId(cartId, foodId);
			cf.id = Integer.parseInt(foodId);
//			if(cf.cosumeCount > 0)
				//jedisDAL.UpdateFood(cf);
			cfList.add(cf);
		}
		if(jedisDAL.UpdateFood(cfList) == -1){
			return  FOOD_OUT_OF_STOCK;
		}		
		return orderId;
	}
	
	public List<JsonAdminOrder> getOrderListByAdmin(final String accessToken) {
//		System.out.println("--------------------------------------------------------------");
		return jedisOrder.queryOrderFoodListByAdmin(accessToken);
	}
	
	public JsonUserOrder queryOrderFoodList(final String accessToken) {
		return jedisOrder.queryOrderFoodList(accessToken);
	}
}
