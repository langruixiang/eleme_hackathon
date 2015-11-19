package Process;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import server.ConstValue;

public class HttpResponseFactory {
    
    public static FullHttpResponse getHttpResponse(HttpResponseStatus status,String jsonString){
    	FullHttpResponse ret = null;
    	try {
			ret = new DefaultFullHttpResponse(HTTP_1_1, status , Unpooled.wrappedBuffer(jsonString.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getNotFoundResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, Unpooled.wrappedBuffer( ConstValue.NotFound.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getTokenUnthorizedResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret = new DefaultFullHttpResponse(HTTP_1_1, UNAUTHORIZED, Unpooled.wrappedBuffer(ConstValue.TokenUnaothorized.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getNoJsonResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, Unpooled.wrappedBuffer(ConstValue.JsonEmpty.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getBadFormatResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, Unpooled.wrappedBuffer(ConstValue.JsonWrongFormat.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getWrongPassWord(){
    	FullHttpResponse ret = null;
    	try {
			ret = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN , Unpooled.wrappedBuffer(ConstValue.ForbiddenLogin.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getCartsNotExistsResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND , Unpooled.wrappedBuffer(ConstValue.CartsNotExists.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getCartsUnAuthorizedResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret = new DefaultFullHttpResponse(HTTP_1_1, UNAUTHORIZED , Unpooled.wrappedBuffer(ConstValue.CartsUnauthorized.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getCartsOverFolwResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret = new  DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN , Unpooled.wrappedBuffer(ConstValue.CartsOverFlow.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getFoodsNotExists(){
    	FullHttpResponse ret = null;
    	try {
			ret = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND , Unpooled.wrappedBuffer(ConstValue.FoodsNotExists.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getOrderCartsUnthorizedResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret =  new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN , Unpooled.wrappedBuffer(ConstValue.OrderCartsUnauthorized.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getFoodsNotEnoughResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret =   new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN , Unpooled.wrappedBuffer(ConstValue.FoodsNotEnough.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    public static FullHttpResponse getOnlyOneOrderResponse(){
    	FullHttpResponse ret = null;
    	try {
			ret =   new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN , Unpooled.wrappedBuffer(ConstValue.OnlyOneOrder.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return ret;
    }

}
