package entityLayer;
import model.User;
import redis.clients.jedis.Jedis;
import redisDAL.RedisDAL;
import server.ConstValue;

public class AccessTokenAuthorization {
	RedisDAL redisDAL = new RedisDAL();
	//Jedis redis = ConstValue.jedisPool.getResource();
	
	public String getAccessToken(final String username, final String password)
	{
		Jedis redis = ConstValue.jedisPool.getResource();
		String accessToken = new String("");
		
		if(!redisDAL.IsExistUser(username, password)) return "-1";
		
		User user = redisDAL.GetUser(username, password);
		
		//accessToken = Tool.generateAccessToken();
		accessToken = String.valueOf(user.id);
		
		redis.sadd("user", accessToken);
		redis.close();
		return accessToken;
	}
}
