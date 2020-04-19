package hcloud.netty.modules.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //http解码器
        pipeline.addLast(new HttpServerCodec());
        //添加支持大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        //对HTTPMessage的聚合
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        //================================= 以上用于支持http协议 ===================================

        //
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //自定义handler
        pipeline.addLast(new ChatHandler());


    }
}
