package redisDAL;
 

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dal.FoodDAL;
import dal.UserDAL;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import server.ConstValue;
import model.*;

public class RedisDAL {
	
	
	//final 
	static UserDAL userDAL = new UserDAL();
	static FoodDAL foodDAL = new FoodDAL();
	

	public RedisDAL(){
		
	}
	
	public void JedisTest(){		
		Jedis jedis = ConstValue.jedisPool.getResource();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
	        String result = jedis.set("n" + i, "n" + i);
	    }
	    long end = System.currentTimeMillis();
	    //System.out.println("Simple SET: " + ((end - start)/1000.0) + " seconds");
	    jedis.disconnect();
	}
	
	public User GetUser(String name,String password){
		return userDAL.GetUser(name, password);
	}
	
	//获取全部的food信息
	public List<Food> GetAllFood(){
		Jedis jedis = ConstValue.jedisPool.getResource();
		List<Food> allFoods = new LinkedList<Food>();
		Map<String, String> foodMap = jedis.hgetAll("allfoodInfo");		
		List<String>allFoodIDs = jedis.lrange("allfoodIDs", 0, -1);		
		jedis.close();
		if(allFoodIDs.size() == 0){
			return GetAllFoodFromSQL();
		}		
		Iterator<String> iterator = allFoodIDs.iterator();
		
		while(iterator.hasNext()){
			Food food = new Food();
			//是否用新的数据结构，不需要再进行类型转换
			String foodIdStr = iterator.next();
			food.id = Integer.parseInt(foodIdStr);
			food.stock = Integer.parseInt(foodMap.get("foodStock"+foodIdStr));
			food.price = Integer.parseInt(foodMap.get("foodPrice"+foodIdStr));
			allFoods.add(food);
		}
		
		return allFoods;		
	}	
	
	//判断用户是否存在
	//TODO:1 直接把全部用户加载到redis
	//2. 直接读数据库，不处理redis
	public boolean IsExistUser(String name,String password){
		
		return  userDAL.IsExistUser(name, password);
		/*
		String pwd = jedis.get("name");
		Boolean result; 
		if(pwd == null){
			result = userDAL.IsExistUser(name, password);
		}
		else{
			if(pwd == password) {
				return true;
			}
			else{
				return false;
			}
		}
		return result;*/
	}
	
	
	//下单后更新食物消费情况
	//TODO:
	public int UpdateFood(ConsumeFood consume){
		Jedis jedis = ConstValue.jedisPool.getResource();
		int result = foodDAL.UpdateFood(consume);
		if(result==1){
			
			
			String foodStockRedisStr = jedis.hget("allfoodInfo","foodStock"+consume.id);
			if (foodStockRedisStr !=null){
				Map<String,String> foodConsumedMap = new TreeMap<String,String>();
				int nowFoodStock = Integer.parseInt(foodStockRedisStr);			
				foodConsumedMap.put("foodStock"+consume.id, String.valueOf(nowFoodStock-consume.cosumeCount));
				jedis.hmset("allfoodInfo",foodConsumedMap);
			}
			
		}
		jedis.close();
		return result;
		//TODO : update Redis
	}
	
	//通过id获取食物信息
	//若不存在返回null
	//TODO:
	public Food GetFoodById(int id){
		Jedis jedis = ConstValue.jedisPool.getResource();
		
		Food food = new Food();	
		food.id = id;
		String stock = jedis.hget("allfoodInfo", "foodStock"+id) ;
		
		if(stock == null){
			jedis.close();
			return foodDAL.GetFoodById(id);
		}
		
		//System.out.println("GetFoodByID");
		
		food.stock =  Integer.parseInt(stock);//jedis.hget("allfoodInfo", "foodStock"+id));
		food.price = Integer.parseInt(jedis.hget("allfoodInfo", "foodPrice"+id));			
		jedis.close();
		return food;
		//
	}
	
	//从数据库读全部食物并写入Redis
	private List<Food>  GetAllFoodFromSQL(){		
		Jedis jedis = ConstValue.jedisPool.getResource();
		List<Food>allfoods = foodDAL.GetFood();
		String foodListKey = "allfoodInfo";		
		for(Food food: allfoods){
			//TODO: 优化类型转换。	
			Map<String,String> map = new HashMap<String, String>();
			map.put("foodId"+food.id,String.valueOf(food.id));
			map.put("foodStock"+food.id, String.valueOf(food.stock));
			map.put("foodPrice"+food.id, String.valueOf(food.price));
			jedis.rpush("allfoodIDs", String.valueOf(food.id));
			jedis.hmset(foodListKey,map );			
		}
		jedis.close();
		return allfoods;
	}
}
