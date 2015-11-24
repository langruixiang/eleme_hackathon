package entityLayer;
import java.util.Random;


public class Tool {
	final static int TOKEN_SIZE = 16;
	final static int MAX_SIZE = 62;
	
	public static String generateAccessToken() {
		StringBuilder accessToken = new StringBuilder();
		
		Random random = new Random();
		for(int i = 0; i < TOKEN_SIZE; ++ i) { 
			Character ch;
			int n = random.nextInt(MAX_SIZE);
			if(n < 10) {
				accessToken.append(String.valueOf(n));
				continue;
			}
			if(n < 36) {
				n -= 10;
				n = n + 65;
				ch = (char) n;
				accessToken.append(ch);
				continue;
			}
			
			n -= 36;
			n += 97;
			ch = (char)n;
			accessToken.append(ch);
		}
		return accessToken.toString();
	}
}
