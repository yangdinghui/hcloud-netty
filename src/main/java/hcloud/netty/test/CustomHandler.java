package hcloud.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 自定义助手类
 */
//SimpleChannelInboundHandler:  请求入境
public class CustomHandler extends ChannelInboundHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String body = (String) msg;
        System.out.println("THE TIME SERVER RECEIVE ORDER : [" + (++counter) + "] : " + body);
        String currentTime = System.currentTimeMillis() + "";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);


        //获取channel
//        Channel channel = ctx.channel();
//        if (msg instanceof HttpRequest) {
//            SocketAddress socketAddress = channel.remoteAddress();
//            //显示客户端的远程地址
//            System.out.println(socketAddress);
//
//            //定义发送的数据消息
//            ByteBuf content = Unpooled.copiedBuffer("Hello Netty~", CharsetUtil.UTF_8);
//
//            //构建一个http Response
//            FullHttpResponse response =
//                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1
//                            , HttpResponseStatus.OK
//                            , content);
//            //设置响应数据类型和长度
//            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//            response.headers().set(HttpHeaderNames.CONTENT_LENGTH
//                    , content.readableBytes());
//
//            ctx.writeAndFlush(response);
//        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.toString());
        ctx.close();
    }
}
