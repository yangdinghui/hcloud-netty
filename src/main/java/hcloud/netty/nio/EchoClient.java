package hcloud.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class EchoClient {

    private final String IP;
    private final int PORT;
    private Selector selector;
    private SocketChannel socketChannel;

    public EchoClient(String IP, int PORT) {
        this.IP = IP;
        this.PORT = PORT;
    }

    public void chat(String nickName) {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(IP, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            //接收服务端内容
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (; ; ) {
                        try {
                            int count = selector.select();
                            if (count == 0) {
                                continue;
                            }
                            Set<SelectionKey> selectionKeys = selector.selectedKeys();
                            Iterator<SelectionKey> iteratorKey = selectionKeys.iterator();
                            while (iteratorKey.hasNext()) {
                                SelectionKey selectionKey = iteratorKey.next();
                                if (selectionKey.isReadable()) {
                                    //处理可读事件
                                    readEvent(selectionKey);
                                }
                                iteratorKey.remove();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            //向服务端发送消息
            System.out.println("请输入聊天内容: ");
            Scanner in = new Scanner(System.in);
            while (in.hasNextLine()) {
                String content = in.nextLine();
                socketChannel.write(Charset.forName("utf-8").encode(nickName + ": " + content));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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
            System.out.println(socketChannel.getRemoteAddress() + ": " + stringBuffer.toString());
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new EchoClient("127.0.0.1", 8888).chat("A Client");
    }
}
