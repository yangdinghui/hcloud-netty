package hcloud.netty.test;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 初始化器，channel注册后，会执行里面的相应的初始化方法
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //通过SocketChannel去获得对应的管道
        ChannelPipeline pipeline = channel.pipeline();
        //通过管道添加handler HttpServerCodec助手类
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());

        //添加自定义的handler
        pipeline.addLast("customHandler", new CustomHandler());
    }
}
