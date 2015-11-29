package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    private static final int portNumber = Integer.parseInt(ConstValue.APP_PORT);
    
    public static void main(String[] args) throws InterruptedException {
    	serverInit();
    	
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ServerInitializer());
            b.option(ChannelOption.SO_BACKLOG, 1024);  
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(portNumber).sync();
            f.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    private static void serverInit(){
    	//System.out.println("Server Init...");
    	ConstValue constValue = new ConstValue();
    }
}
