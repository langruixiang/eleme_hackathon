package server;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

import java.util.concurrent.Callable;

import Process.HttpResponseFactory;
import Process.ProcessAddFoods;
import Process.ProcessAddOrders;
import Process.ProcessAdminQueryOrders;
import Process.ProcessCreateCarts;
import Process.ProcessLogin;
import Process.ProcessQueryOrders;
import Process.ProcessQueryStock;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpHeaders.Values;

public class ServerTask implements Runnable{
	private HttpMethod httpMethod = null;
	private String URI = null;
	private String accessToken = null;
	private String jsonString = null;
	private ChannelHandlerContext ctx = null;
	private FullHttpResponse response = null;  
	
	public ServerTask(HttpMethod httpMethod,String URI,String accessToken,String jsonString,ChannelHandlerContext ctx) {
		// TODO Auto-generated constructor stub
		this.httpMethod = httpMethod;
		this.URI = URI;
		this.accessToken = accessToken;
		this.jsonString = jsonString;
		this.ctx = ctx;
	}

	@Override
	public void run(){
       
        
       if(httpMethod == HttpMethod.POST){
        	if(URI.equals("/login")){
        		response = new ProcessLogin(jsonString).GetHttpResponse();
        	}else if(URI.startsWith("/carts")){
        	   if(accessToken != null){
        		   response = new ProcessCreateCarts(accessToken).GetHttpResponce();
        	   }else{
        			response = HttpResponseFactory.getTokenUnthorizedResponse();
        		}           
        	}else if(URI.startsWith("/orders")){
        		response = new ProcessAddOrders(accessToken,jsonString).GetHttpResponse();                		
        	}else{
        		response = HttpResponseFactory.getNotFoundResponse();
        	}                	
        }else if(httpMethod == HttpMethod.GET){
        	if(URI.startsWith("/foods")){
        		if( accessToken != null){
        			response = new ProcessQueryStock(accessToken).GetHttpResponce();
        		}else{
        			response = HttpResponseFactory.getTokenUnthorizedResponse();
        		}               		
        	}else if(URI.startsWith("/orders")){
        		if(accessToken != null){
        			response = new ProcessQueryOrders(accessToken).GetHttpResponse();
        		}else{
        			response = HttpResponseFactory.getTokenUnthorizedResponse();
        		}                		
        	}else if(URI.startsWith("/admin/")){
        		if(accessToken != null){
        			response = new ProcessAdminQueryOrders(accessToken).GetHttpResponse();
        		}else{
        			response = HttpResponseFactory.getTokenUnthorizedResponse();
        		}
        	}else{
        		response = HttpResponseFactory.getNotFoundResponse();
        	}
        }else if(httpMethod == HttpMethod.PATCH){
        	if(URI.startsWith("/carts")){
        		String cartid = "";
        		
        		if(accessToken != null){
        			if(URI.contains("?")){
        				cartid = URI.split("\\?")[0].split("/")[2];
        			}else{
        				cartid = URI.split("/")[2];
        			}
        			response = new ProcessAddFoods(cartid,accessToken,jsonString).GetHttpResponce();
        		}else{
        			response = HttpResponseFactory.getTokenUnthorizedResponse();
        		}
        		
        	}else{
        		response = HttpResponseFactory.getNotFoundResponse();
        	}
        }else{
        	response = HttpResponseFactory.getNotFoundResponse();
        }

        response.headers().set(CONTENT_TYPE, "application/json");  
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());  
        response.headers().set(CONNECTION, Values.KEEP_ALIVE);  
        
        ctx.write(response);  
        ctx.flush();  
//        if(ctx.executor().inEventLoop()){
//        		ctx.write(response);  
//                ctx.flush();  
//        }else{
//        	ctx.executor().execute(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					ctx.write(response);  
//	                ctx.flush();  
//				}
//			});
//        }
	}

}
