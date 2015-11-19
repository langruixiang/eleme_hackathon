package entityLayer;
import java.util.Random;


public class Tool {
	final static int TOKEN_SIZE = 31;
	final static int MAX_SIZE = 62;
	
	// 2 --> cartId 随机生成
	public static String generateAccessToken() {
		String accessToken = new String("2");
		
		Random random = new Random();
		for(int i = 0; i < TOKEN_SIZE; ++ i) { 
			Character ch;
			int n = random.nextInt(MAX_SIZE);
			if(n < 10) {
				accessToken += String.valueOf(n);
				continue;
			}
			if(n < 36) {
				n -= 10;
				ch = (char) (65 + n);
				accessToken += ch.toString();
				continue;
			}
			
			n -= 36;
			ch = (char)(97 + n);
			accessToken += ch.toString();
		}
		return accessToken;
	}
	
	// user access token 随机生成
	public static String generateAccessToken(final String userId, final String userName) {
		String accessToken = new String("1");
		
		Random random = new Random();
		for(int i = 0; i < TOKEN_SIZE; ++ i) { 
			Character ch;
			int n = random.nextInt(MAX_SIZE);
			if(n < 10) {
				accessToken += String.valueOf(n);
				continue;
			}
			if(n < 36) {
				n -= 10;
				ch = (char) (65 + n);
				accessToken += ch.toString();
				continue;
			}
			
			n -= 36;
			ch = (char)(97 + n);
			accessToken += ch.toString();
		}
		
		return accessToken;
	}
}
