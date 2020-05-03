package hcloud.netty.trydemo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.logging.SocketHandler;

/**
 * @author YDH
 * @date 2020/2/10 0010
 * @description 描述
 */
public class TryServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //通过SocketChannel获取管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        //添加http服务解码器
        pipeline.addLast(new HttpServerCodec());
        //添加大数据流处理类
        pipeline.addLast(new ChunkedWriteHandler());
        //添加http消息聚合器
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        //================================ 以上是添加对http协议的支持 ==================================

        //============================= 以下是添加对httpWebSocket协议的支持 ============================
        /**
         * websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
         * 本handler会帮你处理一些繁重的复杂的事
         * 会帮你处理握手动作： handshaking（close, ping, pong） ping + pong = 心跳
         * 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
         */
//        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //自定义handler
        pipeline.addLast(new TryHandler());

    }
}
