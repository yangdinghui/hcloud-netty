package hcloud.netty.modules.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WSServer {

    public static void main(String[] args) throws Exception {
        //1
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();
        try {
            //2
            ServerBootstrap server = new ServerBootstrap();
            server.group(mainGroup, subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WSServerInitializer());
            //3
            ChannelFuture channelFuture = server.bind(8089).sync();
            //4
            channelFuture.channel().closeFuture().sync();
        } finally {
            //5
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }

}
