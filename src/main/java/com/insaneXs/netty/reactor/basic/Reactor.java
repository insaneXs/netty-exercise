package com.insaneXs.netty.reactor.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: insaneXs
 * @Description:
 * @Date: Create at 2018-12-19
 */
public class Reactor implements Runnable{

    final Selector selector;

    final ServerSocketChannel serverSocket;

    Reactor(int port) throws Exception{

        //创建ServerSocketChannel，绑定端口，设置为非阻塞，选择器上注册ACCEPT事件
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();

        serverSocket.bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        sk.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //阻塞，直到注册的事件发生
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()){
                    //任务派发
                    dispatch((SelectionKey)(it.next()));
                }
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    void dispatch(SelectionKey k) {
        //通过将不同的附件绑定到SelectionKey上，实现dispatch统一派发Acceptor和Handler的逻辑
        Runnable r = (Runnable)(k.attachment());
        if (r != null)
            r.run();
    }

    class Acceptor implements Runnable{
        @Override
        public void run() {
            try {
                //ACCEPT负责接收链接
                SocketChannel sc = serverSocket.accept();
                if(sc != null)
                    new Handler(selector, sc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Handler implements Runnable{
        final SocketChannel socket;

        final SelectionKey sk;

        ByteBuffer input = ByteBuffer.allocate(1024);
        ByteBuffer output = ByteBuffer.allocate(1024);

        static final int READING = 0, SENDING = 1;
        int state = READING;

        Handler(Selector sel, SocketChannel c) throws IOException{
            socket = c;
            c.configureBlocking(false);
            // Optionally try first read now
            //返回了新的SelectionKey，将Handler添加为SelectionKey的附件，先注册READ事件
            sk = socket.register(sel, 0);
            sk.attach(this);
            sk.interestOps(SelectionKey.OP_READ);
            sel.wakeup();
        }

        boolean inputIsComplete() {
            return true;
        }
        boolean outputIsComplete() {
            return true;
        }
        void process() {
            //DO SOME THING
        }

        @Override
        public void run() {
            try {
                if (state == READING) read();
                else if (state == SENDING) send();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        void read() throws IOException {
            socket.read(input);
            if (inputIsComplete()) {
                process();
                state = SENDING;
            // Normally also do first write now
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        void send() throws IOException {
            socket.write(output);
            if (outputIsComplete()) sk.cancel();
        }

    }
}
