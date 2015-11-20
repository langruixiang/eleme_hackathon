package redisDAL;
import model.User;
import redis.clients.jedis.Jedis;
import redisDAL.RedisDAL;
import server.ConstValue;

public class AccessTokenAuthorization {
	
	
	public String getAccessToken(final String username, final String password)
	{
		RedisDAL redisDAL = new RedisDAL();
		Jedis redis = ConstValue.jedisPool.getResource();
		String accessToken = new String("");
		
		if(!redisDAL.IsExistUser(username, password)) return "-1";
		
		User user = redisDAL.GetUser(username, password);
		
		//accessToken = Tool.generateAccessToken();
		accessToken = String.valueOf(user.id);
		
		redis.lpush("customer", accessToken+'p');
		redis.close();
		return accessToken;
	}
}
