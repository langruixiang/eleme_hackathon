package Process;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import entityLayer.Cart;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import server.ConstValue;

public class ProcessAddFoods {
	private String token = null;
	private String cartid = null;
	private String foodid = null;
	private String foodcount = null;
	
	private FullHttpResponse successResponce = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT , Unpooled.wrappedBuffer("".getBytes()));
	
	private FullHttpResponse response = null;
	
	public ProcessAddFoods(String cartid,String token,String jsonString){
		 if(jsonString == null || jsonString.length() == 0){
 			response = HttpResponseFactory.getNoJsonResponse();
 			return ;
 		}
		
		this.token = token;
		this.cartid = cartid;
		
		try {
			JsonObject root = ConstValue.jsonParser.parse(jsonString).getAsJsonObject();  
			foodid = root.get("food_id").toString();
			foodid.replace("\"", "");
			foodcount = root.get("count").toString();
			foodcount.replace("\"", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response= HttpResponseFactory.getBadFormatResponse();
			return ;
		}		
        
        if(foodid == null || foodid.length() == 0 ||  foodcount == null ||  foodcount.length() == 0){
        	response= HttpResponseFactory.getBadFormatResponse();
        }
	}
	
	public FullHttpResponse GetHttpResponce(){
		if(response != null){
			return response;
		}
		
		String res = new Cart().addFoodToCart(foodid, foodcount, token, cartid);
//		System.out.println("Add foods cartid :" + cartid);
//		System.out.println("Add foods res :" + res);
		
		if(res.equals("1")){
			response = successResponce;
		}else if(res.equals("2")){
			response = HttpResponseFactory.getCartsNotExistsResponse();
		}else if(res.equals("3")){
			response = HttpResponseFactory.getCartsUnAuthorizedResponse();
		}else if(res.equals("4")){
			response = HttpResponseFactory.getFoodsNotExists();
		}else if(res.equals("5")){
			response = HttpResponseFactory.getCartsOverFolwResponse();
		}
		
		return response;
	}

}
