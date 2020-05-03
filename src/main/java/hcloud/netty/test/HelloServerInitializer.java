package hcloud.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 初始化器，channel注册后，会执行里面的相应的初始化方法
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //通过SocketChannel去获得对应的管道
        ChannelPipeline pipeline = channel.pipeline();
        ByteBuf byteBuf = Unpooled.copiedBuffer("$_$".getBytes());
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new DelimiterBasedFrameDecoder(2048, byteBuf));

        //添加自定义的handler
        pipeline.addLast("customHandler", new CustomHandler());
    }
}
