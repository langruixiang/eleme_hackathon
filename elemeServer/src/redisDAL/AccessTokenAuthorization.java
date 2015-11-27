package redisDAL;
import entityLayer.Tool;
import redis.clients.jedis.Jedis;
import server.ConstValue;

public class AccessTokenAuthorization {
	
	static String userScript = 
			"redis.call('set', KEYS[1], KEYS[2]);\n" 
			+ "redis.call('set', KEYS[3], KEYS[1]);\n"
			+ "redis.call('set', KEYS[4], KEYS[5])";
	
	public String getAccessToken(final String username, final String password)
	{
		RedisDAL redisDAL = new RedisDAL();
		Jedis redis = ConstValue.jedisPool.getResource();
		//String accessToken = new String("");
		
		if(!redisDAL.IsExistUser(username, password)) return "-1";
		
		String userId = redisDAL.GetUser(username, password);
		
		String accessToken;
		if(redis.get(userId) != null) accessToken = redis.get(userId);
		else  {
			accessToken = Tool.generateAccessToken();
			redis.eval(userScript, 5, userId, accessToken, accessToken+"uid", accessToken+"cartNum", "0");
		}
		
		
		redis.close();
		return userId+","+accessToken;
	}
}
