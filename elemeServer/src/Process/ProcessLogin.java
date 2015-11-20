package Process;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.UnsupportedEncodingException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import server.ConstValue;

import redisDAL.AccessTokenAuthorization;

public class ProcessLogin {
	private FullHttpResponse response = null;
	
	private String username = null;
	private String passwd = null;
	
	private String responseJsonString = null;
	
	public ProcessLogin(String jsonString){	
		 if(jsonString == null || jsonString.length() == 0){
	 			response = HttpResponseFactory.getNoJsonResponse();
	 			return ;
	 	  }
		
		try {			
				JsonObject root  = ConstValue.jsonParser.parse(jsonString).getAsJsonObject();
				username = root.get("username").toString().replace("\"", "");;
				passwd = root.get("password").toString().replace("\"", "");;			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response = HttpResponseFactory.getBadFormatResponse();
			return ;
		}		
        
        if(username == null || username.length() == 0 || passwd == null || passwd.length() == 0){
        	response = HttpResponseFactory.getBadFormatResponse();
        }
	}
	
	public FullHttpResponse GetHttpResponse(){
		if(response != null){
			return response;
		}
		
		AccessTokenAuthorization access = new AccessTokenAuthorization();
		
		String res = access.getAccessToken(username, passwd);
//		System.out.println("Login res :" + res);
		
		if(res.equals("-1")){
			response = HttpResponseFactory.getWrongPassWord();
		}else{
			responseJsonString =  ConstValue.gons.toJson(new BackJsonObject(res, username, res)); 
			response = HttpResponseFactory.getHttpResponse(OK, responseJsonString);
		}
		
		return response;
	}
	
	private class BackJsonObject{
		String user_id = "";
		String username = "";
		String access_token = "";
		
		public BackJsonObject(String user_id,String username,String access_token){
			this.user_id = user_id;
			this.username = username;
			this.access_token = access_token;
		}		
	}

}
