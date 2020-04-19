package hcloud.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloServer {
    public static void main(String[] args) throws InterruptedException {
        //主线程组，接收客户端连接，但不做任何处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //从线程组，做任务
        EventLoopGroup worksGroup = new NioEventLoopGroup();
        try {
            //netty服务器的创建，ServerBootstrap是一个启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, worksGroup)        //主从线程组
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HelloServerInitializer());                        //子处理器

            //启动server，并设置8088为启动端口，启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            //监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            worksGroup.shutdownGracefully();
        }

    }
}
