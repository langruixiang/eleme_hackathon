package redisDAL;
import entityLayer.Tool;
import redis.clients.jedis.Jedis;
import server.ConstValue;

public class AccessTokenAuthorization {
	
	
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
			redis.set(userId, accessToken);
			redis.set(accessToken+"uid", userId);
		}

		redis.close();
		return userId+","+accessToken;
	}
}
