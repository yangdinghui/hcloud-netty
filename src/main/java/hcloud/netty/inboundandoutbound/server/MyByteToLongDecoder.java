package hcloud.netty.inboundandoutbound.server;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     * @param ctx 上下文对象
     * @param in  入站的ByteBuf
     * @param out List集合将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("[MyByteToLongDecoder] 被调用");
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
