package Process;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import entityLayer.Order;
import io.netty.handler.codec.http.FullHttpResponse;
import server.ConstValue;

public class ProcessAddOrders {
	private String token = null;
	private String cartid = null;
	
	private FullHttpResponse response = null;
	
	public ProcessAddOrders(String token,String jsonString){
		 if(jsonString == null || jsonString.length() == 0){
	 			response = HttpResponseFactory.getNoJsonResponse();
	 			return ;
	 		}
		
		this.token = token;
		
		try {
			JsonObject root = ConstValue.jsonParser.parse(jsonString).getAsJsonObject();  
			cartid = root.get("cart_id").toString().replace("\"", "");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response= HttpResponseFactory.getBadFormatResponse();
			return ;
		}	
        
        if(cartid == null || cartid.length() == 0){
        	response= HttpResponseFactory.getBadFormatResponse();
        }
	}
	
	public FullHttpResponse GetHttpResponse(){
		if(response != null){
			return response;
		}
		
		String res = new Order().placeOrder(token, cartid);
//		System.out.println("Add Order token:" + token);
//		System.out.println("Add Order cartid:" + cartid + "!!!!");
//		
//		System.out.println("Add Order res:" + res);
		
		if(res.equals("1")){
			response = HttpResponseFactory.getCartsNotExistsResponse();
		}else if(res.equals("2")){
			response = HttpResponseFactory.getOrderCartsUnthorizedResponse();
		}else if(res.equals("3")){
			response = HttpResponseFactory.getFoodsNotEnoughResponse();
		}else if(res.equals("4")){
			response = HttpResponseFactory.getOnlyOneOrderResponse();
		}else{
			response = HttpResponseFactory.getHttpResponse(OK, ConstValue.gons.toJson(new BackJsonObject(res)));
		}
		
		return response;
	}
	
	private class BackJsonObject{
		String id ;
		
		public BackJsonObject(String id){
			this.id =  id;
		}
	}

}
