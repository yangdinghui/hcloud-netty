package hcloud.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.SocketAddress;

/**
 * 自定义助手类
 */
//SimpleChannelInboundHandler:  请求入境
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //获取channel
        Channel channel = ctx.channel();
        if (msg instanceof HttpRequest) {
            SocketAddress socketAddress = channel.remoteAddress();
            //显示客户端的远程地址
            System.out.println(socketAddress);

            //定义发送的数据消息
            ByteBuf content = Unpooled.copiedBuffer("Hello Netty~", CharsetUtil.UTF_8);

            //构建一个http Response
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1
                            , HttpResponseStatus.OK
                            , content);
            //设置响应数据类型和长度
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH
                    , content.readableBytes());

            ctx.writeAndFlush(response);
        }
    }
}
