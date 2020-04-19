package hcloud.netty.nio;

public class EchoClient_C {

    public static void main(String[] args) {
        new EchoClient("127.0.0.1", 8888).chat("C Client");
    }
}
