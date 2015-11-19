package Process;

import entityLayer.Cart;
import io.netty.handler.codec.http.FullHttpResponse;
import server.ConstValue;

import static io.netty.handler.codec.http.HttpResponseStatus.*;

public class ProcessCreateCarts {
	private String token = null;
	private FullHttpResponse response = null;
	
	public ProcessCreateCarts(String token){
		this.token = token;
	}
	
	public FullHttpResponse GetHttpResponce(){
		if(response != null){
			return response;
		}
		
		String res = new Cart().createCart(token);
//		System.out.println("Create cart res:" + res);
		
		response = HttpResponseFactory.getHttpResponse(OK, ConstValue.gons.toJson(new BackJsonObject(res)));
		
		return response;
	}
	
	private class BackJsonObject{
		String cart_id = null;
		
		public BackJsonObject(String cart_id){
			this.cart_id = cart_id;
		}
	}
}
