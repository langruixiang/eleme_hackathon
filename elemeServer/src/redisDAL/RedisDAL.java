package redisDAL;
 

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;

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
	
	public String GetUser(String name,String password){
		Jedis jedis = ConstValue.jedisPool.getResource();
		String userID =  jedis.hget("AllUserInfo", "userId"+name);
		jedis.close();
		return userID;
		//return userDAL.GetUser(name, password);
	}
	
	//获取全部的food信息
	public List<Food> GetAllFood(){
		Jedis jedis = ConstValue.jedisPool.getResource();
		List<Food> allFoods = new LinkedList<Food>();
		//Map<String, String> foodMap = jedis.hgetAll("AllFoodInfo");		
		List<String>allFoodIDs = jedis.lrange("allfoodIDs", 0, -1);
		
		if(allFoodIDs.size() == 0){
			return GetAllFoodFromSQL();
		}		
		Iterator<String> iterator = allFoodIDs.iterator();
		
		while(iterator.hasNext()){
			Food food = new Food();
			//是否用新的数据结构，不需要再进行类型转换
			String foodIdStr = iterator.next();
			food.id = Integer.parseInt(foodIdStr);
			food.stock = Integer.parseInt(jedis.get("foodStock"+foodIdStr));
			food.price = Integer.parseInt(jedis.get("foodPrice"+foodIdStr));
			allFoods.add(food);
		}
		
		jedis.close();
		return allFoods;		
	}	
	
	//判断用户是否存在
	//TODO:1 直接把全部用户加载到redis
	//2. 直接读数据库，不处理redis
	public boolean IsExistUser(String name,String password){
		//return  userDAL.IsExistUser(name, password);
		Jedis jedis = ConstValue.jedisPool.getResource();
		String pwd =  jedis.hget("AllUserInfo", "userPassword"+name) ;		
		jedis.close();
		if(pwd == null){
			
			return false;//userDAL.IsExistUser(name, password);
		}
		else{
			if(pwd.equals(password)) {
				return true;
			}
			else{				
				return false;
			}
		}
	}
	

	
	//下单后更新食物消费情况
	//TODO:
//	public int UpdateFood(ConsumeFood consume){
//		Jedis jedis = ConstValue.jedisPool.getResource();
//		int result = foodDAL.UpdateFood(consume);
//		if(result==1){
//			String foodStockRedisStr = jedis.hget("AllFoodInfo","foodStock"+consume.id);
//			if (foodStockRedisStr !=null){
//				Map<String,String> foodConsumedMap = new TreeMap<String,String>();
//				int nowFoodStock = Integer.parseInt(foodStockRedisStr);			
//				foodConsumedMap.put("foodStock"+consume.id, String.valueOf(nowFoodStock-consume.cosumeCount));
//				jedis.hmset("AllFoodInfo",foodConsumedMap);
//			}
//			
//		}
//		jedis.close();
//		return result;
//		//TODO : update Redis
//	}
//	
	
	//下单后更新食物消费情况
	//TODO:
	public int UpdateFood(List<ConsumeFood> consumeLists){
		Jedis jedis = ConstValue.jedisPool.getResource();	
		//先更新数据库信息
		//int result = 1;//foodDAL.UpdateFood(consumeLists);
		//再对redis里面的信息进行更新
		List<ConsumeFood> changedFoods = new LinkedList<ConsumeFood>();
		for(ConsumeFood consume: consumeLists){
			if (jedis.decrBy("foodStock"+consume.id, consume.cosumeCount)>=0){
				changedFoods.add(consume);
			}else{
				jedis.incrBy("foodStock"+consume.id, consume.cosumeCount);
				for(ConsumeFood changed:changedFoods){
					jedis.incrBy("foodStock"+changed.id, changed.cosumeCount);
				}
				jedis.close();
				return -1;
			}
		}
		jedis.close();
		return 1;
		
		
//		if(result>=1){
//			for(ConsumeFood consume: consumeLists){
//				String foodStockRedisStr = jedis.hget("AllFoodInfo","foodStock"+consume.id);
//				//给reids的food加锁。  
//				//TODO: 等待时间太久，琐失效。
//				//可以尝试的办法，乐观锁，wathc+事务
//				String foodLockRedis = "foodStock"+consume.id+".lock";
//				//设置setnx锁,若要update的food被锁，则等待。
//				while(jedis.setnx(foodLockRedis, String.valueOf(System.currentTimeMillis()+300)) !=1){
//					
//					/*String time = jedis.get(foodLockRedis);
//					if(Long.parseLong(time) > System.currentTimeMillis()){						
//						jedis.getSet(foodLockRedis, String.valueOf(System.currentTimeMillis()+300));
//						break;
//					}*/
//					try {
//						Thread.currentThread();
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				//锁的时间设置为300毫秒
//				jedis.pexpireAt(foodLockRedis, System.currentTimeMillis()+300);
//				//更新redis里的food的库存量
//				
//				Map<String,String> foodConsumedMap = new TreeMap<String,String>();
//				int nowFoodStock = Integer.parseInt(foodStockRedisStr);			
//				int leftFoodSotck =(nowFoodStock-consume.cosumeCount);
//				//如果库存足够，则减去消费掉的食物。
//				if(leftFoodSotck >= 0){
//					foodConsumedMap.put("foodStock"+consume.id, String.valueOf(nowFoodStock-consume.cosumeCount));
//					jedis.hmset("AllFoodInfo",foodConsumedMap);	
//					changedFoods.add(consume);	
//					//释放锁
//					jedis.del(foodLockRedis);
//				}
//				
//				//库存不够，则回滚。
//				else{					
//					jedis.del(foodLockRedis);
//					for(ConsumeFood changedConsume:changedFoods){
//						foodLockRedis = "foodStock"+changedConsume.id+".lock";
//						//设置setnx锁,若要update的food被锁，则等待。
//						while(jedis.setnx(foodLockRedis, String.valueOf(System.currentTimeMillis()+300)) !=1){
//							try {
//								Thread.currentThread();
//								Thread.sleep(100);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//						jedis.pexpireAt(foodLockRedis, System.currentTimeMillis()+300);						
//						nowFoodStock = Integer.parseInt(foodStockRedisStr);			
//						leftFoodSotck =(nowFoodStock+changedConsume.cosumeCount);						
//						foodConsumedMap.put("foodStock"+changedConsume.id, String.valueOf(nowFoodStock+changedConsume.cosumeCount));
//						jedis.hmset("AllFoodInfo",foodConsumedMap);		
//						//释放锁
//						jedis.del(foodLockRedis);
//					}
//					jedis.close();
//					return  -1;
//				}
//			
//			}					
//		}
//		jedis.close();
//		return result;
		//TODO : update Redis
	}
	
	
	
	//通过id获取食物信息
	//若不存在返回null
	//TODO:
	public Food GetFoodById(int id){
		Jedis jedis = ConstValue.jedisPool.getResource();
		
		Food food = new Food();	
		food.id = id;
		String stock = jedis.get("foodStock"+id);//jedis.hget("AllFoodInfo", "foodStock"+id) ;
		
		if(stock == null){
			jedis.close();
			return foodDAL.GetFoodById(id);
		}
		
		//System.out.println("GetFoodByID");
		
		food.stock =  Integer.parseInt(stock);//jedis.hget("AllFoodInfo", "foodStock"+id));
		food.price = Integer.parseInt(jedis.get("foodPrice"+id));			
		jedis.close();
		return food;
		//
	}
	
	//从数据库读全部食物并写入Redis
	public static List<Food>  GetAllFoodFromSQL(){			
		List<Food>allfoods = FoodDAL.GetFood();//foodDAL.GetFood();
		Jedis jedis = ConstValue.jedisPool.getResource();
		if(jedis.setnx("GET_ALL_FOOD_INFO", "Begin") == 1){
			//String foodListKey = "AllFoodInfo";		
			for(Food food: allfoods){
				//TODO: 优化类型转换。	
				//Map<String,String> map = new HashMap<String, String>();
				jedis.set("foodId"+food.id,String.valueOf(food.id));
				jedis.set("foodStock"+food.id, String.valueOf(food.stock));
				jedis.set("foodPrice"+food.id, String.valueOf(food.price));
				/*
				map.put("foodId"+food.id,String.valueOf(food.id));
				map.put("foodStock"+food.id, String.valueOf(food.stock));
				map.put("foodPrice"+food.id, String.valueOf(food.price));
				*/
				jedis.rpush("allfoodIDs", String.valueOf(food.id));
				//jedis.hmset(foodListKey,map );			
			}
		}		
		jedis.close();
		return allfoods;
	}
	
	public static void GetAllUser(){
		Jedis jedis = ConstValue.jedisPool.getResource();
		if(jedis.setnx("GET_ALL_USER_INFO", "Begin") == 1){
			List<User> userList = UserDAL.GetAllUser();		
			String userListKey = "AllUserInfo";		
			for(User user: userList){
				//TODO: 优化类型转换。	
				Map<String,String> map = new HashMap<String, String>();
				map.put("userId"+user.name,String.valueOf(user.id));
				map.put("userName"+user.name, user.name);
				map.put("userPassword"+user.name, user.password);
				jedis.rpush("AllUserName", String.valueOf(user.name));
				jedis.hmset(userListKey,map );			
			}
		}		
		jedis.close();
	}
}
