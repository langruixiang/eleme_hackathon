package Process;

import java.util.List;

import entityLayer.Cart;
import io.netty.handler.codec.http.FullHttpResponse;
import model.Food;
import server.ConstValue;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

public class ProcessQueryStock {
	private String token = null;
	
	private FullHttpResponse response = null;
	
	public ProcessQueryStock(String token){
		this.token = token;
	}
	
	public FullHttpResponse GetHttpResponce(){
		if(response != null){
			return response;
		}
		
		List<Food> res = new Cart().getAllFoodList();
		Food[] arr = res.toArray(new Food[res.size()]);
		
//		System.out.println("Query stock :" + res.toString());
		
		
		response = HttpResponseFactory.getHttpResponse(OK, ConstValue.gons.toJson(arr));
	
		return response;
	}
		
	private class BackJsonObject{
		private Food[] foodList = null;
		
		public BackJsonObject(Food[] list){
			foodList = list;
		}
	}

}
