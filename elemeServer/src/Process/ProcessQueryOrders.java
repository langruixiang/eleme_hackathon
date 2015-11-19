package Process;

import java.util.List;

import com.google.gson.JsonNull;

import entityLayer.Order;
import io.netty.handler.codec.http.FullHttpResponse;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import server.ConstValue;

public class ProcessQueryOrders {
	private String token = null;
	private FullHttpResponse response = null;
	
	public ProcessQueryOrders(String token){
		this.token = token;
	}
	
	public FullHttpResponse GetHttpResponse(){
		if(response !=  null){
			return response;
		}
		
		JsonUserOrder res = new Order().queryOrderFoodList(token);		
		
		if(res != null){
			JsonUserOrder[] arr = new JsonUserOrder[1];
			arr[0] = res;
//			System.out.println("Query order res :" + res.toString());
			response = HttpResponseFactory.getHttpResponse(OK, ConstValue.gons.toJson(arr));
		}else{
			response = HttpResponseFactory.getHttpResponse(OK, ConstValue.gons.toJson(new JsonUserOrder[0]));
		}
		
		return response;
	}
	
	private class BackJsonObject{
		private String id;
		private List<JsonSubFood> items;
		private int total;
		
		public BackJsonObject(String id,List<JsonSubFood> items,String total){
			this.id = id;
			this.items = items;
			this.total = Integer.parseInt(total);
		}
	}

}
