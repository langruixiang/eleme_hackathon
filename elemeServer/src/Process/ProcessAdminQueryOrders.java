package Process;

import java.util.List;

import entityLayer.Order;
import io.netty.handler.codec.http.FullHttpResponse;
import server.ConstValue;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

public class ProcessAdminQueryOrders {
	private String token = null;
	private FullHttpResponse response = null;
	
	public ProcessAdminQueryOrders(String token){
		this.token = token;
	}
	
	public FullHttpResponse GetHttpResponse(){
		if(response !=  null){
			return response;
		}
		
		List<JsonAdminOrder> list =  new Order().getOrderListByAdmin(token);
//		System.out.println("Admid Query" + list.size());
		
		JsonAdminOrder[] arr = list.toArray(new JsonAdminOrder[list.size()]);
		
		response = HttpResponseFactory.getHttpResponse(OK, ConstValue.gons.toJson(arr));		
		return response;
	}
	
	private class BackJsonObject{
	   JsonAdminOrder list ;
		
		public BackJsonObject(JsonAdminOrder list){
			this.list = list;
		}
	}
}
