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
		
		String user = redisDAL.GetUser(username, password);
		
		String accessToken;
		if(redis.get(user) != null) accessToken = redis.get(user);
		else  {
			accessToken = Tool.generateAccessToken();
			//redis.sadd("user", accessToken);
			redis.set(user, accessToken);
			redis.set(accessToken+"uid", user);
		}
		//accessToken = user;
		//String accessToken = redis.get("userIdU") + 'p';
		//redis.incr("userIdU");
		
		
		redis.close();
		return user+","+accessToken;
	}
}
