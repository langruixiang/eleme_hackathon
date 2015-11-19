package server;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/*************************************************************************
	> File Name: ConstValue.java
	> Author: 
	> Mail: 
	> Created Time: 2015年11月15日 星期日 10时47分37秒
 ************************************************************************/

public class ConstValue {
	public static int requestCounter = 0;
	
	public static final String APP_HOST ;
	public static final String APP_PORT;
	public static final String DB_HOST;
	public static final String DB_PORT;
	public static final String DB_NAME;
	public static final String DB_USER;
	public static final String DB_PASS;
	public static final String REDIS_HOST;
	public static final String REDIS_PORT;
	
	public static final Gson gons = new Gson();
	public static final JsonParser jsonParser= new JsonParser();
	
    public static final String TokenUnaothorized = "{\"code\":\"INVALID_ACCESS_TOKEN\",\"message\":\"无效的令牌\"}";
    public static final String JsonEmpty = "{\"code\": \"EMPTY_REQUEST\",\"message\": \"请求体为空\"}";
    public static final String JsonWrongFormat = "{\"code\":\"MALFORMED_JSON\",\"message\": \"格式错误\"}";
    public static final String ForbiddenLogin = "{\"code\":\"USER_AUTH_FAIL\",\"message\": \"用户名或密码错误\"}";
    public static final String CartsNotExists = "{\"code\": \"CART_NOT_FOUND\",\"message\": \"篮子不存在\"}";
    public static final String CartsUnauthorized = "{\"code\": \"NOT_AUTHORIZED_TO_ACCESS_CART\",\"message\": \"无权限访问指定的篮子\"}";
    public static final String CartsOverFlow = "{\"code\": \"FOOD_OUT_OF_LIMIT\",\"message\": \"篮子中食物数量超过了三个\"}";
    public static final String FoodsNotExists = "{\"code\": \"FOOD_NOT_FOUND\",\"message\": \"食物不存在\"}";
    public static final String OrderCartsUnauthorized = "{\"code\": \"NOT_AUTHORIZED_TO_ACCESS_CART\",\"message\": \"无权限访问指定的篮子\"}"; 
    public static final String FoodsNotEnough = "{\"code\": \"FOOD_OUT_OF_STOCK\",\"message\": \"食物库存不足\"}";
    public static final String OnlyOneOrder = "{\"code\": \"ORDER_OUT_OF_LIMIT\",\"message\": \"每个用户只能下一单\"}";
    public static final String NotFound = "";
    
    public static JedisPoolConfig config = new JedisPoolConfig();
    public static  JedisPool jedisPool;
    
    static{
    	Map<String,String> envMap = System.getenv();
    	
    	APP_HOST = envMap.get("APP_HOST");
    	APP_PORT = envMap.get("APP_PORT");//"1234";
    	DB_HOST = envMap.get("DB_HOST");
    	DB_PORT = envMap.get("DB_PORT");//;"3306"
    	DB_NAME = envMap.get("DB_NAME");
    	DB_USER = envMap.get("DB_USER");
    	DB_PASS =  envMap.get("DB_PASS");
    	REDIS_HOST = envMap.get("REDIS_HOST");
    	REDIS_PORT = envMap.get("REDIS_PORT");//;"6379"
    	
    	//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
    			config.setBlockWhenExhausted(true);
    			 
    			//设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
    			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
    			 
    			//是否启用pool的jmx管理功能, 默认true
    			config.setJmxEnabled(true);
    			 
    			//MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默 认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
    			config.setJmxNamePrefix("pool");
    			 
    			//是否启用后进先出, 默认true
    			config.setLifo(true);
    			 
    			//最大空闲连接数, 默认8个
    			config.setMaxIdle(1000);
    			 
    			//最大连接数, 默认8个
    			config.setMaxTotal(1000);
    			 
    			//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
    			config.setMaxWaitMillis(-1);
    			 
    			//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
    			config.setMinEvictableIdleTimeMillis(3000);
    			 
    			//最小空闲连接数, 默认0
    			config.setMinIdle(0);
    			 
    			//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
    			config.setNumTestsPerEvictionRun(-1);
    			 
    			//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)   
    			config.setSoftMinEvictableIdleTimeMillis(1800000);
    			 
    			//在获取连接的时候检查有效性, 默认false
    			config.setTestOnBorrow(false);
    			 
    			//在空闲时检查有效性, 默认false
    			config.setTestWhileIdle(false);
    			 
    			//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
    			config.setTimeBetweenEvictionRunsMillis(10000);
    			jedisPool = new JedisPool(config, ConstValue.REDIS_HOST,Integer.parseInt(ConstValue.REDIS_PORT));
    			
    	
    	
    	//System.out.println();
    	//System.out.println(APP_HOST );
    	//System.out.println(APP_PORT);
    	//System.out.println(DB_HOST );
    	//System.out.println(DB_PORT );
    	//System.out.println(DB_NAME);
    	//System.out.println(DB_USER);
    	//System.out.println(DB_PASS);
    	//System.out.println(REDIS_HOST);
    	//System.out.println(REDIS_PORT);
    }
}

