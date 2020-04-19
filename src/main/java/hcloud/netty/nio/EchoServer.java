package hcloud.netty.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class EchoServer {

    private final int PORT;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public EchoServer(int PORT) {
        this.PORT = PORT;
    }

    public void start() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("ECHO SERVICE IS STARTING !!!");
            for (; ; ) {
                int count = selector.select();
                if (count == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    if (key.isAcceptable()) {
                        //如果是接入事件
                        acceptEvent(key);
                    }
                    if (key.isReadable()) {
                        //如果是可读事件
                        readEvent(key);
                    }
                    //最后移除已经处理过的key
                    selectionKeyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理接入事件
     */
    private void acceptEvent(SelectionKey key) {
        try {
//            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            //服务器给客户端的提示
            socketChannel.write(Charset.forName("utf-8").encode("你与其他人不是朋友关系，请注意隐私安全"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理可读事件
     *
     * @param key
     */
    private void readEvent(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            StringBuffer stringBuffer = new StringBuffer();
            while (socketChannel.read(byteBuffer) > 0) {
                // 切换读模式
                byteBuffer.flip();
                CharBuffer decode = Charset.forName("utf-8").decode(byteBuffer);
                stringBuffer.append(decode);
            }
            socketChannel.register(selector, SelectionKey.OP_READ);
            //转发给其他客户端
            if (stringBuffer.toString().length() > 0) {
                broadcastOthersClient(socketChannel, stringBuffer.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 广播给其他客户端
     */
    private void broadcastOthersClient(SocketChannel excludeClient, String msg) {
        //获取注册的所有客户端
        Set<SelectionKey> selectionKeySet = selector.keys();
        selectionKeySet.forEach(selectionKey -> {
            Channel targetChannel = selectionKey.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != excludeClient) {
                SocketChannel socketChannel = (SocketChannel) targetChannel;
                try {
                    //将信息发给其他客户端
                    socketChannel.write(Charset.forName("utf-8").encode(msg));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new EchoServer(8888).start();
    }
}
