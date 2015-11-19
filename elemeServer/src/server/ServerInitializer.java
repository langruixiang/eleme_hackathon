package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

       // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码  
        pipeline.addLast(new HttpResponseEncoder());  
        // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码  
        pipeline.addLast(new HttpRequestDecoder());  

        // 自己的逻辑Handler
        pipeline.addLast("handler", new ServerHandler());
    }
}