package hcloud.netty.trydemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author YDH
 * @description 描述
 * @date 2020/2/10 0010
 */
public class TryHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //管理所有连接的客户端
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //消息内容
        String content = msg.text();

        channels.writeAndFlush(content);
    }
}
