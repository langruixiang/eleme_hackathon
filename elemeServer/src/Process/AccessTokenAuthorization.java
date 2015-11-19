package Process;
import model.User;
import redis.clients.jedis.Jedis;
import redisDAL.RedisDAL;

public class AccessTokenAuthorization {
	RedisDAL redisDAL = new RedisDAL();
	Jedis redis = new Jedis("localhost");
	
	public String getAccessToken(final String username, final String password)
	{
		String accessToken = new String("");
		
		if(!redisDAL.IsExistUser(username, password)) return "-1";
		
		User user = redisDAL.GetUser(username, password);
		
		//accessToken = Tool.generateAccessToken();
		accessToken = String.valueOf(user.id);
		
		redis.lpush("customer", accessToken+'p');
		
		return accessToken;
	}
}
