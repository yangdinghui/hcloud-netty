package hcloud.netty.trydemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author YDH
 * @date 2020/2/10 0010
 * @description 描述
 */
public class TryServer {

    public static void main(String[] args) throws Exception {

        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup child = new NioEventLoopGroup();


        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(parent, child)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TryServerInitializer());

            ChannelFuture channelFuture = server.bind(8090).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            parent.shutdownGracefully();
            child.shutdownGracefully();
        }


    }

}
