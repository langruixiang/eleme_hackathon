package server;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import Process.HttpResponseFactory;
import Process.ProcessAddFoods;
import Process.ProcessAddOrders;
import Process.ProcessAdminQueryOrders;
import Process.ProcessCreateCarts;
import Process.ProcessLogin;
import Process.ProcessQueryOrders;
import Process.ProcessQueryStock;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

public class ServerHandler extends ChannelInboundHandlerAdapter {           
    private HttpMethod httpMethod = null;
    private String URI = null;
    private String accessToken = null;
    
    private ByteBufToBytes reader = null;
    
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        if (msg instanceof HttpRequest) {  
            HttpRequest request = (HttpRequest) msg; 
            
            httpMethod = request.getMethod();
 //           //System.out.println("Method:" + httpMethod.toString());
         
            URI = request.getUri();
 //           //System.out.println("URI:" + URI);
            
           if(URI.contains("access_token")){
        	   accessToken = getToken(URI);
           }else{
        	   accessToken =  request.headers().get("Access-Token");
           }
            
            if (HttpHeaders.isContentLengthSet(request)) {  
                reader = new ByteBufToBytes((int) HttpHeaders.getContentLength(request));  
            }else{
            	reader = new ByteBufToBytes(0);  
            }
        }
  
        if (msg instanceof HttpContent) {
        	
//        	//System.out.println(ConstValue.requestCounter++);
        	
            HttpContent httpContent = (HttpContent) msg;  
            ByteBuf content = httpContent.content();  
        	reader.reading(content);  
            content.release();  
  
            if (reader.isEnd()) {  
                String jsonString = new String(reader.readFull());  
                //System.out.println("JSON String:" + jsonString);  
                
                FullHttpResponse response = null;  
                
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
            }  
        }  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
//         //System.out.println("HttpServerInboundHandler.channelReadComplete");  
        ctx.flush();  
    }  
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
    	cause.printStackTrace();
    }
    
    private String getToken(String URI){
    	String ret = null;
    	
    	if(URI.contains("?") && URI.contains("=")){
    		String accesstoken = URI.split("\\?")[1];
    		if(accesstoken.startsWith("access_token")){
    			ret = accesstoken.split("=")[1];
    		}
    	}else if(URI.contains(" ") && URI.contains(":")){
    		String accesstoken = URI.split(" ")[1];
    		if(accesstoken.startsWith("Access-Token")){
    			ret = accesstoken.split(":")[1];
    		}
    	}    	
    	return ret;
    }
  
}  
