package com.insaneXs.netty.reactor.threadpooled;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: insaneXs
 * @Description:
 * @Date: Create at 2018-12-21
 */
public class ThreadPooledHandler implements Runnable{
    final SocketChannel socket;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    static final int PROCESSING = 3;
    int state = READING;

    // uses util.concurrent thread pool
    static ExecutorService pool = Executors.newFixedThreadPool(4);

    public ThreadPooledHandler(Selector sel, SocketChannel c) throws IOException {
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

    synchronized void read() throws IOException { // ...
        socket.read(input);
        if (inputIsComplete()) {
            state = PROCESSING;
            pool.execute(new Processer());
        }
    }

    void send() throws IOException {
        socket.write(output);
        if (outputIsComplete()) sk.cancel();
    }

    //增加Processer角色，处理业务逻辑
    class Processer implements Runnable {
        public void run() { processAndHandOff(); }
    }

    synchronized void processAndHandOff() {
        process();
        state = SENDING; // or rebind attachment
        sk.interestOps(SelectionKey.OP_WRITE);
    }

}
