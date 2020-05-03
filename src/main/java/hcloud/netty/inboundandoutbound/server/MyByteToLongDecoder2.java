package hcloud.netty.inboundandoutbound.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {

    /**
     * @param ctx 上下文对象
     * @param in  入站的ByteBuf
     * @param out List集合将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("[MyByteToLongDecoder2] 被调用");
        //不需要判断是否足够读取,内部已经处理
//        if (in.readableBytes() >= 8) {
        out.add(in.readLong());
//        }
    }
}
